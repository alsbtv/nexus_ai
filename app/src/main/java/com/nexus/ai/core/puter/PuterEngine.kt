package com.nexus.ai.core.puter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONArray
import kotlin.coroutines.resume

data class PuterModel(val id: String, val name: String, val provider: String)

class PuterEngine(private val context: Context) {
    private var webView: WebView? = null
    private var isReady = false

    var authState by mutableStateOf("Not Logged In")
        private set
    var availableModels by mutableStateOf<List<PuterModel>>(emptyList())
        private set

    @SuppressLint("SetJavaScriptEnabled")
    fun init(onReady: () -> Unit) {
        if (webView != null) return
        webView = WebView(context).apply {
            setBackgroundColor(Color.TRANSPARENT)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.setSupportMultipleWindows(true)
            addJavascriptInterface(Bridge(), "AndroidBridge")
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    isReady = true
                    onReady()
                }
            }
            loadDataWithBaseURL(
                "https://nexus.ai",
                getBridgeHtml(),
                "text/html",
                "UTF-8",
                null
            )
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
                if (typeof puter !== 'undefined') {
                    puter.auth.getUser().then(user => {
                        AndroidBridge.onAuthState(user ? "Logged In: " + user.username : "Not Logged In");
                    }).catch(e => AndroidBridge.onAuthState("Not Logged In"));
                }
            }
            function fetchModels() {
                puter.ai.listModels().then(models => {
                    AndroidBridge.onModelsReceived(JSON.stringify(models));
                }).catch(e => console.error(e));
            }
            function triggerSignIn() {
                puter.auth.signIn().then(() => { checkAuth(); }).catch(e => console.error(e));
            }
        </script></head><body onload="checkAuth()"></body></html>
    """.trimIndent()

    inner class Bridge {
        @JavascriptInterface
        fun onAuthState(state: String) { authState = state }
        @JavascriptInterface
        fun onModelsReceived(json: String) {
            val list = mutableListOf<PuterModel>()
            try {
                val arr = JSONArray(json)
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    list.add(PuterModel(obj.getString("id"), obj.optString("name", obj.getString("id")), obj.optString("provider", "Puter")))
                }
                availableModels = list
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun openSignInDialog() {
        webView?.evaluateJavascript("triggerSignIn()", null)
    }

    fun fetchModels() {
        webView?.evaluateJavascript("fetchModels()", null)
    }
}
