package com.example.foodsearch.presentation.book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.foodsearch.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(
    navController: androidx.navigation.NavController,
    viewModel: BookViewModel = hiltViewModel()
) {
    val currentTabPosition by viewModel.currentTabPosition.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(
        initialPage = currentTabPosition,
        pageCount = { 2 }
    )
    
    LaunchedEffect(pagerState.currentPage) {
        viewModel.setCurrentTabPosition(pagerState.currentPage)
    }
    
    LaunchedEffect(currentTabPosition) {
        if (currentTabPosition != pagerState.currentPage) {
            pagerState.animateScrollToPage(currentTabPosition)
        }
    }
    
    LaunchedEffect(Unit) {
        viewModel.loadFavoriteRecipes()
        viewModel.loadAllRecipes()
    }
    
    LaunchedEffect(pagerState.currentPage) {
        // Обновляем списки при переключении вкладок
        when (pagerState.currentPage) {
            0 -> viewModel.refreshFavoriteRecipes()
            1 -> viewModel.refreshAllRecipes()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Tab Row
        TabRow(
            selectedTabIndex = pagerState.currentPage
        ) {
            Tab(
                selected = pagerState.currentPage == 0,
                onClick = {
                    viewModel.setCurrentTabPosition(0)
                },
                text = {
                    Text(
                        text = stringResource(R.string.favorite),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            )
            Tab(
                selected = pagerState.currentPage == 1,
                onClick = {
                    viewModel.setCurrentTabPosition(1)
                },
                text = {
                    Text(
                        text = stringResource(R.string.book_menu),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            )
        }
        
        // Content
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> FavoriteScreen(
                    onRecipeClick = { recipe ->
                        val recipeId = recipe.id ?: 0
                        navController.navigate("details/$recipeId")
                    }
                )
                1 -> SavedScreen(
                    onRecipeClick = { recipe ->
                        val recipeId = recipe.id ?: 0
                        navController.navigate("details/$recipeId")
                    }
                )
            }
        }
    }
}

@Composable
fun FavoriteScreen(
    onRecipeClick: (com.example.foodsearch.domain.models.RecipeDetails) -> Unit,
    viewModel: BookViewModel = hiltViewModel()
) {
    val favoriteRecipes by viewModel.favoriteRecipes.collectAsStateWithLifecycle()
    
    if (favoriteRecipes.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No favorite recipes yet",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Like recipes to add them here",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(favoriteRecipes) { recipe ->
                FavoriteRecipeItem(
                    recipe = recipe,
                    onClick = { onRecipeClick(recipe) }
                )
            }
        }
    }
}

@Composable
fun SavedScreen(
    onRecipeClick: (com.example.foodsearch.domain.models.RecipeDetails) -> Unit,
    viewModel: BookViewModel = hiltViewModel()
) {
    val allRecipes by viewModel.allRecipes.collectAsStateWithLifecycle()
    
    if (allRecipes.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No recipes viewed yet",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "View recipe details to add them here",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(allRecipes) { recipe ->
                SavedRecipeItem(
                    recipe = recipe,
                    onClick = { onRecipeClick(recipe) }
                )
            }
        }
    }
}

@Composable
fun FavoriteRecipeItem(
    recipe: com.example.foodsearch.domain.models.RecipeDetails,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Recipe Image
            AsyncImage(
                model = recipe.image ?: "",
                contentDescription = recipe.title ?: "Recipe image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Recipe Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = recipe.title ?: "No title",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${recipe.readyInMinutes ?: 0} min",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = "${recipe.servings ?: 0} servings",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
            
            // Like Icon
            Icon(
                painter = painterResource(R.drawable.ic_like_red_51dp),
                contentDescription = "Liked",
                tint = Color.Red,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun SavedRecipeItem(
    recipe: com.example.foodsearch.domain.models.RecipeDetails,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Recipe Image
            AsyncImage(
                model = recipe.image ?: "",
                contentDescription = recipe.title ?: "Recipe image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Recipe Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = recipe.title ?: "No title",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${recipe.readyInMinutes ?: 0} min",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = "${recipe.servings ?: 0} servings",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
            
            // Status Icon
            Icon(
                painter = painterResource(
                    if (recipe.isLike == true) R.drawable.ic_like_red_51dp else R.drawable.ic_like_51dp
                ),
                contentDescription = if (recipe.isLike == true) "Liked" else "Not liked",
                tint = if (recipe.isLike == true) Color.Red else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}