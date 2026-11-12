package com.nexus.ai.core.puter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Message
import android.util.Base64
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray

data class PuterModel(val id: String, val name: String, val provider: String)

class PuterEngine(private val context: Context) {
    private var hiddenWebView: WebView? = null
    private var loginDialog: Dialog? = null

    var authState by mutableStateOf("Not Logged In")
        private set
    var availableModels by mutableStateOf<List<PuterModel>>(emptyList())
        private set

    private val _chatStream = MutableSharedFlow<String>()
    val chatStream = _chatStream.asSharedFlow()

    private val _isTyping = MutableStateFlow(false)
    val isTyping = _isTyping.asStateFlow()

    @SuppressLint("SetJavaScriptEnabled")
    fun init() {
        if (hiddenWebView != null) return
        hiddenWebView = WebView(context).apply {
            setBackgroundColor(Color.TRANSPARENT)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            addJavascriptInterface(Bridge(), "AndroidBridge")
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    checkAuth()
                    fetchModels()
                }
            }
            loadDataWithBaseURL("https://nexus.ai", getBridgeHtml(), "text/html", "UTF-8", null)
        }
    }

    private fun getBridgeHtml(): String = """
        <!DOCTYPE html><html><head>
        <script src="https://js.puter.com/v2/"></script>
        <script>
            Object.defineProperty(navigator, 'userActivation', {
                get: function() { return { isActive: true, hasBeenActive: true }; }
            });
            function checkAuth() {
                puter.auth.getUser().then(user => {
                    AndroidBridge.onAuthState(user ? "Logged In: " + user.username : "Not Logged In");
                }).catch(e => AndroidBridge.onAuthState("Not Logged In"));
            }
            function fetchModels() {
                puter.ai.listModels().then(models => {
                    AndroidBridge.onModelsReceived(JSON.stringify(models));
                }).catch(e => console.error(e));
            }
            async function sendMessage(base64Prompt, model, temp) {
                try {
                    const prompt = atob(base64Prompt); // فك التشفير
                    const resp = await puter.ai.chat(prompt, { model: model, temperature: parseFloat(temp), stream: true });
                    for await (const part of resp) {
                        if (part?.text) {
                            AndroidBridge.onStreamChunk(part.text);
                        }
                    }
                    AndroidBridge.onStreamComplete();
                } catch (e) {
                    AndroidBridge.onStreamError(e.toString());
                }
            }
        </script></head><body></body></html>
    """.trimIndent()

    inner class Bridge {
        @JavascriptInterface fun onAuthState(state: String) { authState = state }
        @JavascriptInterface fun onModelsReceived(json: String) {
            try {
                val arr = JSONArray(json)
                val list = mutableListOf<PuterModel>()
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    list.add(PuterModel(obj.getString("id"), obj.optString("name", obj.getString("id")), obj.optString("provider", "Puter")))
                }
                availableModels = list
            } catch (e: Exception) { e.printStackTrace() }
        }
        @JavascriptInterface fun onStreamChunk(chunk: String) { _chatStream.tryEmit(chunk) }
        @JavascriptInterface fun onStreamComplete() { _isTyping.value = false }
        @JavascriptInterface fun onStreamError(error: String) { 
            _chatStream.tryEmit("\n[Error: $error]")
            _isTyping.value = false 
        }
        @JavascriptInterface fun onLoginSuccess() {
            loginDialog?.dismiss()
            checkAuth()
            fetchModels()
        }
    }

    fun checkAuth() { hiddenWebView?.evaluateJavascript("checkAuth()", null) }
    fun fetchModels() { hiddenWebView?.evaluateJavascript("fetchModels()", null) }

    fun sendMessage(prompt: String, model: String, temp: Double) {
        _isTyping.value = true
        // تشفير النص بـ Base64 لتجنب أخطاء علامات الاقتباس
        val base64Prompt = Base64.encodeToString(prompt.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
        hiddenWebView?.evaluateJavascript("sendMessage('$base64Prompt', '$model', $temp)", null)
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun showLoginDialog() {
        if (loginDialog?.isShowing == true) return
        val popupWebView = WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.setSupportMultipleWindows(true)
            webChromeClient = object : WebChromeClient() {
                override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
                    val newWebView = WebView(context)
                    val transport = resultMsg?.obj as WebView.WebViewTransport
                    transport.webView = newWebView
                    resultMsg.sendToTarget()
                    return true
                }
            }
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    // حقن كود للتحقق من تسجيل الدخول وإغلاق النافذة تلقائياً
                    view?.evaluateJavascript("""
                        setInterval(() => {
                            if (typeof puter !== 'undefined' && puter.auth) {
                                puter.auth.isLoggedIn().then(loggedIn => {
                                    if (loggedIn) AndroidBridge.onLoginSuccess();
                                }).catch(e => {});
                            }
                        }, 1500);
                    """, null)
                }
            }
            loadUrl("https://puter.com/action/sign-in")
        }

        loginDialog = Dialog(context).apply {
            setContentView(popupWebView)
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            window?.setBackgroundDrawableResource(android.R.color.black)
            show()
        }
    }
}
