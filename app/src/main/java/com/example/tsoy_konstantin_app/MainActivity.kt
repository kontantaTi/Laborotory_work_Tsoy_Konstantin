package com.example.tsoy_konstantin_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tsoy_konstantin_app.ui.theme.Tsoy_Konstantin_AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tsoy_Konstantin_AppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GreetingApp()
                }
            }
        }
    }
}

@Composable
fun GreetingApp() {
    // состояние для имени и для текста приветствия
    var name by remember { mutableStateOf("") }
    var greetingText by remember { mutableStateOf("Hello!") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = greetingText,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3F51B5)
        )

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter your name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                greetingText = if (name.isNotBlank()) "Hello, $name!" else "Hello, stranger!"
            }
        ) {
            Text("Greet Me")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingAppPreview() {
    Tsoy_Konstantin_AppTheme {
        GreetingApp()
    }
}
