package com.chatbot.yapper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chatbot.yapper.ui.theme.YapperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YapperTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Chat Screen",
            modifier = Modifier.padding(16.dp)
        )

    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    YapperTheme {
        Greeting("Android")
    }
}