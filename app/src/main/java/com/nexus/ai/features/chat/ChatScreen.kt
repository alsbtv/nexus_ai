package com.nexus.ai.features.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.ai.core.theme.*

data class ChatMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val isThinking: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    messages: List<ChatMessage> = emptyList(), // ديناميكي: يظهر عندما نربط Puter
    onMenuClick: () -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 8.dp, start = 4.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextPrimary)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nexus · Auto ▾", color = TextSecondary, fontSize = 16.sp)
                
                Spacer(modifier = Modifier.weight(1f))
                
                IconButton(onClick = { }) {
                    Icon(Icons.Outlined.Movie, contentDescription = "Studio", tint = TextPrimary)
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Outlined.AddCircleOutline, contentDescription = "New Chat", tint = TextPrimary)
                }
            }
        },
        bottomBar = {
            // Input Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .background(SecondarySurface, RoundedCornerShape(24.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Type a message or hold to speak",
                    color = TextMuted,
                    fontSize = 15.sp,
                    modifier = Modifier.weight(1f)
                )
                
                // Think Chip
                Row(
                    modifier = Modifier
                        .background(ElevatedSurface, RoundedCornerShape(16.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.BlurOn, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Think", color = AccentBlue, fontSize = 13.sp)
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                Icon(Icons.Outlined.Add, contentDescription = "Attach", tint = TextSecondary, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Icon(Icons.Outlined.Mic, contentDescription = "Mic", tint = TextSecondary, modifier = Modifier.size(24.dp))
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
            if (messages.isEmpty()) {
                // Empty State
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.AutoAwesome,
                        contentDescription = "Logo",
                        tint = AccentBlue,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "How can I help you today?",
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                // Dynamic Message List (جاهز لبيانات Puter)
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    items(messages) { msg ->
                        MessageItem(msg)
                    }
                }
            }
        }
    }
}

@Composable
fun MessageItem(message: ChatMessage) {
    if (message.isUser) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(UserBubble)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(message.text, color = TextPrimary, fontSize = 15.sp)
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (message.isThinking) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Thought for 1 seconds", color = TextSecondary, fontSize = 14.sp)
                    Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(message.text, color = TextPrimary, fontSize = 15.sp, lineHeight = 24.sp)
            
            Spacer(modifier = Modifier.height(12.dp))
            // Action Buttons
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.ContentCopy, contentDescription = "Copy", tint = TextMuted, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Outlined.ThumbUp, contentDescription = "Like", tint = TextMuted, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Outlined.ThumbDown, contentDescription = "Dislike", tint = TextMuted, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Outlined.VolumeUp, contentDescription = "Speak", tint = TextMuted, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Outlined.Refresh, contentDescription = "Regenerate", tint = TextMuted, modifier = Modifier.size(20.dp))
            }
        }
    }
}
