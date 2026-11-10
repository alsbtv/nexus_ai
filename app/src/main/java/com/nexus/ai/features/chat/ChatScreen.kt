package com.nexus.ai.features.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.ai.core.theme.*

data class ChatMessage(val id: String, val text: String, val isUser: Boolean, val isThinking: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    messages: List<ChatMessage> = emptyList(),
    onMenuClick: () -> Unit,
    onModelClick: () -> Unit,
    currentModel: String = "Auto"
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 40.dp, bottom = 8.dp, start = 4.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onMenuClick) { Icon(Icons.Default.Menu, "Menu", tint = TextPrimary) }
                Spacer(modifier = Modifier.width(4.dp))
                Row(modifier = Modifier.clickable { onModelClick() }.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("Nexus · $currentModel", color = TextSecondary, fontSize = 16.sp)
                    Icon(Icons.Outlined.KeyboardArrowDown, "Select Model", tint = TextSecondary, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { }) { Icon(Icons.Outlined.Movie, "Studio", tint = TextPrimary) }
                IconButton(onClick = { }) { Icon(Icons.Outlined.AddCircleOutline, "New", tint = TextPrimary) }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .imePadding() // رفع الشريط مع لوحة المفاتيح
                    .background(SecondarySurface, RoundedCornerShape(24.dp))
                    .padding(horizontal = 16.dp, vertical = 14.dp), // تكبير الحشو الداخلي
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Type a message...", color = TextMuted, fontSize = 16.sp, modifier = Modifier.weight(1f))
                Row(modifier = Modifier.background(ElevatedSurface, RoundedCornerShape(16.dp)).padding(horizontal = 10.dp, vertical = 6.dp)) {
                    Text("✦ Think", color = AccentBlue, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Icon(Icons.Outlined.Add, "Attach", tint = TextSecondary, modifier = Modifier.size(26.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Icon(Icons.Outlined.Mic, "Mic", tint = TextSecondary, modifier = Modifier.size(26.dp))
            }
        },
        containerColor = OledBlack
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            if (messages.isEmpty()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.AutoAwesome, null, tint = AccentBlue, modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(16.dp))
                    Text("How can I help you today?", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Medium)
                }
            } else {
                LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    items(messages) { msg -> MessageItem(msg) }
                }
            }
        }
    }
}

@Composable
fun MessageItem(message: ChatMessage) {
    if (message.isUser) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Box(Modifier.clip(RoundedCornerShape(20.dp)).background(UserBubble).padding(16.dp)) {
                Text(message.text, color = TextPrimary, fontSize = 15.sp)
            }
        }
    } else {
        Column(Modifier.fillMaxWidth()) {
            if (message.isThinking) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Thought for 1 seconds", color = TextSecondary, fontSize = 14.sp)
                    Icon(Icons.Outlined.ChevronRight, null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                }
                Spacer(Modifier.height(8.dp))
            }
            Text(message.text, color = TextPrimary, fontSize = 15.sp, lineHeight = 24.sp)
        }
    }
}
