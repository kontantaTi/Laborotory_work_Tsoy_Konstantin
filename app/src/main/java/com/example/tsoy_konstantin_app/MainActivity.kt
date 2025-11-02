package com.example.tsoy_konstantin_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
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
                    FruitListApp()
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FruitListApp() {
    val fruits = listOf(
        "Apple",
        "Banana",
        "Orange",
        "Grapes",
        "Watermelon",
        "Pineapple",
        "Kiwi",
        "Strawberry"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fruit List") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF3F51B5),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(8.dp)
        ) {
            items(fruits.size) { index ->
                FruitCard(name = fruits[index])
            }
        }
    }
}

@Composable
fun FruitCard(name: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(16.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF333333)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingAppPreview() {
    Tsoy_Konstantin_AppTheme {
        FruitListApp()
    }
}
