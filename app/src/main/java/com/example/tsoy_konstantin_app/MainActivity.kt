package com.example.tsoy_konstantin_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.tsoy_konstantin_app.ui.theme.Tsoy_Konstantin_AppTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tsoy_Konstantin_AppTheme {
                FruitApp()
            }
        }
    }
}

@Composable
fun FruitApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "list") {
        composable("list") { FruitListScreen(navController) }
        composable("detail/{fruitName}") { backStackEntry ->
            val fruitName = backStackEntry.arguments?.getString("fruitName") ?: "Unknown"
            FruitDetailScreen(fruitName, navController)
        }
    }
}

// ---------- ViewModel ----------
class FruitViewModel : ViewModel() {
    val fruits = listOf("Apple", "Banana", "Orange", "Grapes", "Watermelon", "Pineapple", "Kiwi", "Strawberry")
}

// ---------- Screens ----------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FruitListScreen(navController: NavController, viewModel: FruitViewModel = viewModel()) {
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
            items(viewModel.fruits.size) { index ->
                FruitCard(
                    name = viewModel.fruits[index],
                    onClick = {
                        navController.navigate("detail/${viewModel.fruits[index]}")
                    }
                )
            }
        }
    }
}

@Composable
fun FruitCard(name: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FruitDetailScreen(fruitName: String, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(fruitName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF3F51B5),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Details about $fruitName",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingAppPreview() {
    Tsoy_Konstantin_AppTheme {
        FruitListScreen(navController = rememberNavController())
    }
}
