package com.nexus.ai.features.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.ai.core.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = OledBlack)
            )
        },
        containerColor = OledBlack
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // PROFILE SECTION
            item {
                SettingsGroup(title = "Profile") {
                    SettingsRow(title = "Account settings", icon = Icons.Outlined.Person)
                    HorizontalDivider(color = BorderColor, modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsRow(title = "Data controls", icon = Icons.Outlined.Dataset)
                }
            }

            // APP SECTION
            item {
                SettingsGroup(title = "App") {
                    SettingsRow(title = "Language", value = "English", icon = Icons.Outlined.Language)
                    HorizontalDivider(color = BorderColor, modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsRow(title = "Appearance", value = "Dark", icon = Icons.Outlined.DarkMode)
                    HorizontalDivider(color = BorderColor, modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsRow(title = "Font size", icon = Icons.Outlined.TextFields)
                    HorizontalDivider(color = BorderColor, modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsRow(title = "Personalization", icon = Icons.Outlined.Brush)
                }
            }

            // AUDIO SECTION
            item {
                SettingsGroup(title = "Audio") {
                    SettingsRow(title = "Main language", value = "Use App language", icon = Icons.Outlined.Mic)
                    HorizontalDivider(color = BorderColor, modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsRow(title = "Voice", icon = Icons.Outlined.VolumeUp)
                }
            }

            // ABOUT SECTION
            item {
                SettingsGroup(title = "About") {
                    SettingsRow(title = "Check for updates", value = "2.7.5(296)", icon = Icons.Outlined.Info)
                    HorizontalDivider(color = BorderColor, modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsRow(title = "Service agreement", icon = Icons.Outlined.Description)
                    HorizontalDivider(color = BorderColor, modifier = Modifier.padding(horizontal = 16.dp))
                    SettingsRow(title = "Help & Feedback", icon = Icons.Outlined.HelpOutline)
                }
            }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun SettingsGroup(title: String, content: @Composable () -> Unit) {
    Column {
        Text(
            text = title,
            color = TextMuted,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        Surface(
            color = SecondarySurface,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun SettingsRow(title: String, value: String? = null, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 18.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, color = TextPrimary, fontSize = 16.sp, modifier = Modifier.weight(1f))
        if (value != null) {
            Text(value, color = TextSecondary, fontSize = 15.sp)
            Spacer(modifier = Modifier.width(8.dp))
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp))
    }
}
