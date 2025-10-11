package com.example.foodsearch.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foodsearch.R
import com.example.foodsearch.domain.models.RecipeSummary
import com.example.foodsearch.presentation.book.BookScreen
import com.example.foodsearch.presentation.details.DetailsScreen
import com.example.foodsearch.presentation.search.SearchScreen
import com.example.foodsearch.ui.theme.FoodSearchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            FoodSearchTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = getCurrentScreenTitle(navController),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = "search"
            ) {
                composable("search") {
                    SearchScreen(
                        onRecipeClick = { recipe ->
                            navController.navigate("details/${recipe.id}")
                        }
                    )
                }
                
                composable("book") {
                    BookScreen()
                }
                
                composable("details/{recipeId}") { backStackEntry ->
                    val recipeId = backStackEntry.arguments?.getString("recipeId")?.toIntOrNull() ?: 0
                    DetailsScreen(recipeId = recipeId)
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    NavigationBar(
        containerColor = Color.White
    ) {
        NavigationBarItem(
            icon = { 
                Icon(
                    painter = painterResource(R.drawable.ic_search_19),
                    contentDescription = "Search"
                )
            },
            label = { Text("Search") },
            selected = currentRoute == "search",
            onClick = { 
                navController.navigate("search") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
        )
        
        NavigationBarItem(
            icon = { 
                Icon(
                    painter = painterResource(R.drawable.ic_book_24),
                    contentDescription = "Book"
                )
            },
            label = { Text("Book") },
            selected = currentRoute == "book",
            onClick = { 
                navController.navigate("book") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
        )
    }
}

@Composable
fun getCurrentScreenTitle(navController: NavHostController): String {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    return when {
        currentRoute == "search" -> "Food Search"
        currentRoute == "book" -> "My Recipes"
        currentRoute?.startsWith("details/") == true -> "Recipe Details"
        else -> "Food Search"
    }
}