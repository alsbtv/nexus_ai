package com.nexus.ai.features.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.ai.core.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onMenuClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Nexus", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                        Text(" · Auto ▾", color = TextSecondary, fontSize = 18.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Outlined.Movie, contentDescription = "Studio", tint = TextPrimary)
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Outlined.AddCircleOutline, contentDescription = "New Chat", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = OledBlack)
            )
        },
        bottomBar = {
            // Floating Input Bar Skeleton
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 16.dp)
                    .background(ElevatedSurface, RoundedCornerShape(28.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Message Nexus...",
                    color = TextMuted,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                
                // Think Button
                Row(
                    modifier = Modifier
                        .background(SecondarySurface, RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("✦ Think", color = TextSecondary, fontSize = 14.sp)
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                Icon(Icons.Default.Add, contentDescription = "Attach", tint = TextSecondary)
                Spacer(modifier = Modifier.width(12.dp))
                
                // Disabled Send Button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(AccentBlue, CircleShape)
                        .alpha(0.4f), // Disabled state
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.ArrowUpward, contentDescription = "Send", tint = Color.White)
                }
            }
        },
        containerColor = OledBlack
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "How can I help you today?",
                color = TextSecondary,
                fontSize = 18.sp
            )
        }
    }
}
