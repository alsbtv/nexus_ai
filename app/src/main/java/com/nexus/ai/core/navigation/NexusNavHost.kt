package com.nexus.ai.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nexus.ai.core.puter.PuterEngine
import com.nexus.ai.core.theme.*
import com.nexus.ai.features.chat.ChatScreen
import com.nexus.ai.features.chat.ChatViewModel
import com.nexus.ai.features.settings.SettingsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NexusNavHost(viewModel: ChatViewModel, puterEngine: PuterEngine, onPuterSignIn: () -> Unit) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var expandedMenuId by remember { mutableStateOf<String?>(null) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = DeepSurface, drawerContentColor = TextPrimary, modifier = Modifier.width(320.dp)) {
                Spacer(Modifier.height(48.dp))
                Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp).background(SecondarySurface, RoundedCornerShape(16.dp)).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Search, "Search", tint = TextMuted, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Search chat content...", color = TextMuted, fontSize = 14.sp)
                }
                Spacer(Modifier.height(16.dp))
                
                // بيانات حقيقية قادمة من Room DB لاحقاً (مؤقتاً فارغة)
                LazyColumn(Modifier.weight(1f)) {
                    item {
                        Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text("No chats yet", color = TextMuted, fontSize = 14.sp)
                        }
                    }
                }
                
                HorizontalDivider(color = BorderColor)
                Row(
                    Modifier.fillMaxWidth().clickable { scope.launch { drawerState.close() }; navController.navigate(Routes.SETTINGS) }.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(32.dp).clip(CircleShape).background(AccentBlue), contentAlignment = Alignment.Center) {
                        Text("W", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Spacer(Modifier.width(12.dp))
                    Text("Wesam Alsubhi", color = TextPrimary, fontSize = 15.sp, modifier = Modifier.weight(1f))
                }
            }
        }
    ) {
        NavHost(navController = navController, startDestination = Routes.CHAT, modifier = Modifier.fillMaxSize()) {
            composable(Routes.CHAT) {
                ChatScreen(
                    viewModel = viewModel,
                    puterEngine = puterEngine,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
            composable(Routes.SETTINGS) {
                SettingsScreen(
                    onBack = { navController.popBackStack() },
                    onPuterSignIn = onPuterSignIn,
                    puterState = puterEngine.authState
                )
            }
        }
    }
}
