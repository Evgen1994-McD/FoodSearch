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
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
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
                    val searchViewModel: com.example.foodsearch.presentation.search.SearchViewModel = hiltViewModel()
                    SearchScreen(
                        onRecipeClick = { recipe ->
                            val recipeId = recipe.id ?: 0
                            // Сохраняем рецепт в кеш перед переходом к деталям
                            searchViewModel.saveRecipeToCache(recipe)
                            navController.navigate("details/$recipeId")
                        }
                    )
                }
                
                composable("book") {
                    BookScreen(navController = navController)
                }
                
                composable("details/{recipeId}") { backStackEntry ->
                    val recipeId = backStackEntry.arguments?.getString("recipeId")?.toIntOrNull() ?: 0
                    DetailsScreen(recipeId = recipeId, navController = navController)
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
                    contentDescription = "Search",
                    tint = Color.Black
                )
            },
            label = { 
                Text(
                    text = "Search",
                    color = Color.Black
                ) 
            },
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
                    contentDescription = "Book",
                    tint = Color.Black
                )
            },
            label = { 
                Text(
                    text = "Book",
                    color = Color.Black
                ) 
            },
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
