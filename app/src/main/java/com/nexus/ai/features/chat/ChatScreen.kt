package com.nexus.ai.features.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexus.ai.core.puter.PuterEngine
import com.nexus.ai.core.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    puterEngine: PuterEngine?,
    onMenuClick: () -> Unit
) {
    var showModelSheet by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 40.dp, bottom = 8.dp, start = 4.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onMenuClick) { Icon(Icons.Default.Menu, "Menu", tint = TextPrimary) }
                Spacer(modifier = Modifier.width(4.dp))
                Row(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable { showModelSheet = true }.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Nexus · ${viewModel.currentModel}", color = TextSecondary, fontSize = 16.sp)
                    Icon(Icons.Outlined.KeyboardArrowDown, "Select Model", tint = TextSecondary, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { }) { Icon(Icons.Outlined.Movie, "Studio", tint = TextPrimary) }
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding() // يرفع الشريط مع لوحة المفاتيح
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // شريط الإدخال
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SecondarySurface, RoundedCornerShape(24.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = viewModel.inputText,
                        onValueChange = { viewModel.inputText = it },
                        textStyle = TextStyle(color = TextPrimary, fontSize = 16.sp),
                        modifier = Modifier.weight(1f),
                        decorationBox = { innerTextField ->
                            if (viewModel.inputText.isEmpty()) {
                                Text("Type a message...", color = TextMuted, fontSize = 16.sp)
                            }
                            innerTextField()
                        }
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // زر Think
                    Row(
                        modifier = Modifier.clip(RoundedCornerShape(16.dp)).clickable {
                            viewModel.updateThinkingLevel(
                                when(viewModel.thinkingLevel) {
                                    "Fast" -> "Medium"
                                    "Medium" -> "Deep"
                                    else -> "Fast"
                                }
                            )
                        }.background(ElevatedSurface).padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("✦ ${viewModel.thinkingLevel}", color = AccentBlue, fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))
                    
                    if (viewModel.inputText.isNotBlank()) {
                        IconButton(onClick = { viewModel.sendMessage() }) {
                            Icon(Icons.Default.Send, "Send", tint = AccentBlue)
                        }
                    } else {
                        Icon(Icons.Outlined.Add, "Attach", tint = TextSecondary, modifier = Modifier.size(26.dp))
                    }
                }
            }
        },
        containerColor = OledBlack
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            if (viewModel.messages.isEmpty()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.AutoAwesome, null, tint = AccentBlue, modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(16.dp))
                    Text("How can I help you today?", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Medium)
                }
            } else {
                LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    items(viewModel.messages) { msg -> MessageItem(msg) }
                    if (viewModel.isLoading) {
                        item {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = TextSecondary, strokeWidth = 2.dp)
                                Spacer(Modifier.width(8.dp))
                                Text("Thinking...", color = TextSecondary, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }

    // قائمة النماذج (Bottom Sheet)
    if (showModelSheet) {
        ModalBottomSheet(
            onDismissRequest = { showModelSheet = false },
            containerColor = DeepSurface,
            dragHandle = { BottomSheetDefaults.DragHandle(color = TextMuted) }
        ) {
            Column(Modifier.padding(16.dp).fillMaxWidth().heightIn(max = 500.dp)) {
                Text("Select Model", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                
                // Auto Option
                Row(
                    Modifier.fillMaxWidth().clickable { viewModel.currentModel = "Auto"; showModelSheet = false }.padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Auto", color = TextPrimary, fontSize = 16.sp, modifier = Modifier.weight(1f))
                    if (viewModel.currentModel == "Auto") Icon(Icons.Outlined.CheckCircle, null, tint = AccentBlue)
                }
                HorizontalDivider(color = BorderColor)
                
                LazyColumn {
                    items(puterEngine?.availableModels ?: emptyList()) { model ->
                        Row(
                            Modifier.fillMaxWidth().clickable { viewModel.updateModel(model); showModelSheet = false }.padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(model.name, color = TextPrimary, fontSize = 16.sp)
                                Text(model.provider, color = TextSecondary, fontSize = 12.sp)
                            }
                            if (viewModel.currentModel == model.id) Icon(Icons.Outlined.CheckCircle, null, tint = AccentBlue)
                        }
                        HorizontalDivider(color = BorderColor)
                    }
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
            
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.ContentCopy, "Copy", tint = TextMuted, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(16.dp))
                Icon(Icons.Outlined.ThumbUp, "Like", tint = TextMuted, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(16.dp))
                Icon(Icons.Outlined.ThumbDown, "Dislike", tint = TextMuted, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(16.dp))
                Icon(Icons.Outlined.VolumeUp, "Speak", tint = TextMuted, modifier = Modifier.size(20.dp))
            }
        }
    }
}
