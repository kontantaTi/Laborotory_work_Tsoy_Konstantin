package com.example.tsoy_konstantin_app

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.tsoy_konstantin_app.ui.theme.Tsoy_Konstantin_AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

// DataStore delegate
val Context.dataStore by preferencesDataStore(name = "settings")

// ---------------- MainActivity ----------------
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
        scheduleFavoriteCheck()
    }

    private fun scheduleFavoriteCheck() {
        val workRequest = PeriodicWorkRequestBuilder<FavoriteCheckWorker>(
            15, TimeUnit.MINUTES
        ).build()
        WorkManager.getInstance(this).enqueue(workRequest)
    }
}

class FruitViewModel(private val context: Context) : ViewModel() {

    val fruits = listOf(
        "Apple", "Banana", "Orange", "Grapes", "Watermelon",
        "Pineapple", "Kiwi", "Strawberry"
    )

    var selectedFruit: String? = null
        private set

    private val FAVORITES_KEY = stringPreferencesKey("favorites")

    val favorites: Flow<Set<String>> = context.dataStore.data
        .map { prefs -> prefs[FAVORITES_KEY]?.split(",")?.filter { it.isNotEmpty() }?.toSet() ?: emptySet() }

    fun selectFruit(fruit: String) {
        selectedFruit = fruit
    }

    private suspend fun setFavorites(newFavorites: Set<String>) {
        context.dataStore.edit { prefs ->
            prefs[FAVORITES_KEY] = newFavorites.joinToString(",")
        }
    }

    fun toggleFavorite(fruit: String) {
        viewModelScope.launch {
            val current = favorites.first()
            val updated = if (fruit in current) current - fruit else current + fruit
            setFavorites(updated)
        }
    }
}

class FruitViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FruitViewModel(context) as T
    }
}

// ---------------- Composable UI ----------------
@Composable
fun FruitApp() {
    val navController = rememberNavController()
    val viewModel: FruitViewModel = viewModel(factory = FruitViewModelFactory(LocalContext.current))

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            FruitListScreen(
                fruits = viewModel.fruits,
                favorites = viewModel.favorites.collectAsState(initial = emptySet()).value,
                onFruitClick = { fruit ->
                    viewModel.selectFruit(fruit)
                    navController.navigate("detail")
                },
                onToggleFavorite = { viewModel.toggleFavorite(it) }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FruitListScreen(
    fruits: List<String>,
    favorites: Set<String>,
    onFruitClick: (String) -> Unit,
    onToggleFavorite: (String) -> Unit
) {
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
                    isFavorite = fruit in favorites,
                    onClick = { onFruitClick(fruit) },
                    onToggleFavorite = { onToggleFavorite(fruit) }
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
            contentAlignment = Alignment.Center
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
fun FruitCard(
    name: String,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                    contentDescription = null,
                    tint = if (isFavorite) Color(0xFFFFC107) else Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewList() {
    Tsoy_Konstantin_AppTheme {
        FruitListScreen(
            fruits = listOf("Apple", "Banana"),
            favorites = setOf("Banana"),
            onFruitClick = {},
            onToggleFavorite = {}
        )
    }
}
