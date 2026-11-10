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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nexus.ai.core.theme.*
import com.nexus.ai.features.chat.ChatScreen
import com.nexus.ai.features.settings.SettingsScreen
import kotlinx.coroutines.launch

data class ChatHistory(val title: String, val isPinned: Boolean, val timeGroup: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NexusNavHost() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // بيانات تجريبية للتصميم (ديناميكية وجاهزة للربط مع Room لاحقاً)
    val history = listOf(
        ChatHistory("هل نحن وحدنا ؟", true, "Pinned"),
        ChatHistory("البداية لا النهاية", false, "Yesterday"),
        ChatHistory("استلام مخطط Nexus", false, "Yesterday"),
        ChatHistory("ترتيب نص Supabase", false, "Yesterday"),
        ChatHistory("شرح Get repos", false, "7 Days"),
        ChatHistory("تحليل كود CreatureAtlas", false, "7 Days")
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = DeepSurface,
                drawerContentColor = TextPrimary,
                modifier = Modifier.width(320.dp)
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                
                // Search Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(SecondarySurface, RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Search, contentDescription = "Search", tint = TextMuted, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Search chat content...", color = TextMuted, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                // Dynamic Chat List
                LazyColumn(modifier = Modifier.weight(1f)) {
                    val grouped = history.groupBy { it.timeGroup }
                    grouped.forEach { (group, chats) ->
                        item {
                            Text(
                                text = group,
                                color = TextMuted,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                            )
                        }
                        items(chats) { chat ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { scope.launch { drawerState.close() } }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = chat.title,
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    modifier = Modifier.weight(1f),
                                    maxLines = 1
                                )
                                Icon(Icons.Default.MoreVert, contentDescription = "Options", tint = TextMuted, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
                
                HorizontalDivider(color = BorderColor)
                
                // User Profile (Bottom)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { 
                            scope.launch { drawerState.close() }
                            navController.navigate(Routes.SETTINGS) 
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(AccentBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("W", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Wesam Alsubhi", color = TextPrimary, fontSize = 15.sp, modifier = Modifier.weight(1f))
                    Icon(Icons.Default.MoreVert, contentDescription = "Profile Options", tint = TextMuted, modifier = Modifier.size(20.dp))
                }
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
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
            composable(Routes.SETTINGS) {
                SettingsScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
