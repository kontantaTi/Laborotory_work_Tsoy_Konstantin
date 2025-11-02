package com.example.tsoy_konstantin_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tsoy_konstantin_app.ui.theme.Tsoy_Konstantin_AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tsoy_Konstantin_AppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    FruitApp()
                }
            }
        }
    }
}

@Composable
fun FruitApp() {
    val navController = rememberNavController()
    val viewModel: FruitViewModel = viewModel()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            FruitListScreen(
                fruits = viewModel.fruits,
                onFruitClick = { fruit ->
                    viewModel.selectFruit(fruit)
                    navController.navigate("detail")
                }
            )
        }
        composable("detail") {
            FruitDetailScreen(
                selectedFruit = viewModel.selectedFruit,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

class FruitViewModel : ViewModel() {
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
    var selectedFruit: String? = null
        private set

    fun selectFruit(fruit: String) {
        selectedFruit = fruit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FruitListScreen(fruits: List<String>, onFruitClick: (String) -> Unit) {
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
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(innerPadding)
        ) {
            items(fruits) { fruit ->
                FruitCard(
                    name = fruit,
                    modifier = Modifier.clickable { onFruitClick(fruit) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FruitDetailScreen(selectedFruit: String?, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(selectedFruit ?: "Fruit Detail") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
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
                .background(Color(0xFFF5F5F5))
                .padding(innerPadding),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(
                text = "You selected: ${selectedFruit ?: "Nothing"}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )
        }
    }
}

@Composable
fun FruitCard(name: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
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
fun PreviewFruitList() {
    Tsoy_Konstantin_AppTheme {
        FruitListScreen(fruits = listOf("Apple", "Banana", "Kiwi"), onFruitClick = {})
    }
}
