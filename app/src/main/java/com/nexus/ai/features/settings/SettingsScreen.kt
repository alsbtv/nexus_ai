package com.nexus.ai.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                title = { Text("Settings", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Medium) },
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
                .padding(horizontal = 16.dp)
        ) {
            item { SettingsSection(title = "AI & MODELS") }
            item { SettingsRow(title = "Model Selection", value = "Auto", icon = Icons.Outlined.Star) }
            item { SettingsRow(title = "Thinking Level", value = "Medium", icon = Icons.Outlined.Psychology) }
            
            item { SettingsSection(title = "MEMORY & DATA") }
            item { SettingsRow(title = "Project Memory", icon = Icons.Outlined.Folder) }
            item { SettingsRow(title = "Storage", value = "48 MB", icon = Icons.Outlined.Storage) }
            
            item { SettingsSection(title = "PRIVACY & SECURITY") }
            item { SettingsRow(title = "Privacy Controls", icon = Icons.Outlined.Security) }
            item { SettingsRow(title = "Active Sessions", value = "1 device", icon = Icons.Outlined.Devices) }
        }
    }
}

@Composable
fun SettingsSection(title: String) {
    Text(
        text = title,
        color = TextMuted,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp, start = 8.dp)
    )
}

@Composable
fun SettingsRow(title: String, value: String? = null, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 16.dp, horizontal = 8.dp),
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
    HorizontalDivider(color = BorderColor, thickness = 1.dp)
}
