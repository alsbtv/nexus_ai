package com.nexus.ai.features.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexus.ai.core.puter.PuterEngine
import com.nexus.ai.core.puter.PuterModel
import kotlinx.coroutines.launch
import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val isThinking: Boolean = false
)

class ChatViewModel : ViewModel() {
    var puterEngine: PuterEngine? = null
        set(value) {
            field = value
            setupPuter()
        }

    val messages = mutableStateListOf<ChatMessage>()
    var inputText by mutableStateOf("")
    var currentModel by mutableStateOf("Auto")
    var thinkingLevel by mutableStateOf("Medium")
    var isLoading by mutableStateOf(false)
    private var isFirstChunk = true

    private fun setupPuter() {
        puterEngine?.let { engine ->
            viewModelScope.launch {
                engine.chatStream.collect { chunk ->
                    if (messages.isNotEmpty()) {
                        val lastMsg = messages.last()
                        if (!lastMsg.isUser) {
                            if (isFirstChunk) {
                                messages[messages.lastIndex] = lastMsg.copy(text = chunk)
                                isFirstChunk = false
                            } else {
                                messages[messages.lastIndex] = lastMsg.copy(text = lastMsg.text + chunk)
                            }
                        }
                    }
                }
            }
            viewModelScope.launch {
                engine.isTyping.collect { typing ->
                    isLoading = typing
                    if (!typing) {
                        // إذا انتهى التحميل ولم يتم إضافة أي رد
                        if (isFirstChunk && messages.isNotEmpty() && !messages.last().isUser) {
                            messages.removeAt(messages.lastIndex)
                        }
                    }
                }
            }
        }
    }

    fun getTemperature(): Double {
        return when (thinkingLevel) {
            "Fast" -> 0.2
            "Deep" -> 0.9
            else -> 0.7
        }
    }

    fun sendMessage() {
        val text = inputText.trim()
        if (text.isEmpty() || isLoading) return
        
        messages.add(ChatMessage(text = text, isUser = true))
        // إضافة رسالة فارغة مؤقتة (لن تظهر أزرارها بفضل التعديل في ChatScreen)
        messages.add(ChatMessage(text = "", isUser = false))
        inputText = ""
        isFirstChunk = true
        
        val modelId = if (currentModel == "Auto") "gpt-4o-mini" else currentModel
        puterEngine?.sendMessage(text, modelId, getTemperature())
    }

    fun updateModel(model: PuterModel) {
        currentModel = model.id
    }
    
    fun updateThinkingLevel(level: String) {
        thinkingLevel = level
    }
}
