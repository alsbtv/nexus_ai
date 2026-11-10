package com.nexus.ai.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nexus.ai.core.theme.*
import com.nexus.ai.features.chat.ChatScreen
import com.nexus.ai.features.settings.SettingsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NexusNavHost() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = DeepSurface,
                drawerContentColor = TextPrimary,
                modifier = Modifier.width(300.dp)
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                
                // New Chat Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(ElevatedSurface, RoundedCornerShape(28.dp))
                        .clickable { scope.launch { drawerState.close() } }
                        .padding(vertical = 14.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, contentDescription = "New Chat", tint = TextPrimary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("New Chat", color = TextPrimary, fontSize = 16.sp)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = BorderColor)
                
                // Spacer with weight pushes the settings to the bottom
                Spacer(modifier = Modifier.weight(1f))
                
                HorizontalDivider(color = BorderColor)
                
                // Settings row at the very bottom
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope.launch { drawerState.close() }
                            navController.navigate(Routes.SETTINGS)
                        }
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Settings, contentDescription = "Settings", tint = TextSecondary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Settings", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.CHAT,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Routes.CHAT) {
                ChatScreen(
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onSettingsClick = { navController.navigate(Routes.SETTINGS) }
                )
            }
            composable(Routes.SETTINGS) {
                SettingsScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
