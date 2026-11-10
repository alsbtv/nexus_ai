package com.nexus.ai

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.nexus.ai.core.navigation.NexusNavHost
import com.nexus.ai.core.puter.PuterEngine
import com.nexus.ai.core.theme.NexusTheme
import com.nexus.ai.features.settings.SettingsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // شاشة البداية لمنع الشاشة الرمادية
        installSplashScreen()
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // تهيئة Puter في الخلفية
        val puterEngine = PuterEngine(this)
        puterEngine.init { }

        setContent {
            NexusTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    // هنا نمرر puterEngine للـ Navigation أو الـ ViewModel لاحقاً
                    NexusNavHost(
                        puterEngine = puterEngine,
                        onPuterSignIn = { puterEngine.openSignInDialog() }
                    )
                }
            }
        }
    }
}
