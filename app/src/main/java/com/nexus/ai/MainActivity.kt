package com.nexus.ai

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nexus.ai.core.navigation.NexusNavHost
import com.nexus.ai.core.puter.PuterEngine
import com.nexus.ai.core.theme.NexusTheme
import com.nexus.ai.features.chat.ChatViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        val puterEngine = PuterEngine(this)
        puterEngine.init()

        setContent {
            NexusTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val chatViewModel: ChatViewModel = viewModel()
                    remember { chatViewModel.puterEngine = puterEngine; true }
                    
                    NexusNavHost(
                        viewModel = chatViewModel,
                        puterEngine = puterEngine,
                        onPuterSignIn = { puterEngine.showLoginDialog() }
                    )
                }
            }
        }
    }
}
