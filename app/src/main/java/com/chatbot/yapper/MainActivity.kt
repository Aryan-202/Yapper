package com.chatbot.yapper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chatbot.yapper.ui.theme.YapperTheme
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YapperTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    ChatScreen(
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    val chatHistory = remember { mutableStateListOf<Message>() }
    val coroutineScope = rememberCoroutineScope()

    val generativeModel = remember {
        GenerativeModel(
            modelName = "gemini-3-flash-preview",
            apiKey = BuildConfig.GEMINI_API_KEY
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true
        ) {
            items(chatHistory) { message ->
                ChatMessage(
                    text = message.text,
                    isFromUser = message.isFromUser
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(text = "Type your message here...")
                },
                shape = RoundedCornerShape(24.dp),
                trailingIcon = {
                    Text(
                        text = "Send",
                        color = Color.Blue,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable {
                                if (inputText.isNotBlank()) {
                                    val userText = inputText
                                    chatHistory.add(0, Message(text = userText, isFromUser = true))
                                    inputText = ""

                                    coroutineScope.launch {
                                        try {
                                            val response = generativeModel.generateContent(userText)
                                            response.text?.let { outputText ->
                                                chatHistory.add(0, Message(text = outputText, isFromUser = false))
                                            }
                                        } catch (e: Exception) {
                                            chatHistory.add(0, Message(text = "Error: ${e.localizedMessage}", isFromUser = false))
                                        }
                                    }
                                }
                            }
                    )
                }
            )
        }
    }
}

data class Message(
    val text: String,
    val isFromUser: Boolean
)

@Composable
fun ChatMessage(
    modifier: Modifier = Modifier,
    text: String,
    isFromUser: Boolean,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
            horizontalArrangement = if (isFromUser) Arrangement.End else Arrangement.Start
    ) {
        Text(
            text = text,
            modifier = Modifier
                .background(
                    color = if (isFromUser) Color.Blue else Color.LightGray,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp),
            color = if (isFromUser) Color.White else Color.Black
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ChatScreenPreview() {
    ChatScreen()
}
