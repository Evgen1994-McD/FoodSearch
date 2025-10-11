package com.example.foodsearch.presentation.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.foodsearch.R
import com.example.foodsearch.domain.models.RecipeSummary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onRecipeClick: (RecipeSummary) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentPagingFlow by viewModel.currentPagingFlow.collectAsStateWithLifecycle()
    
    var searchText by remember { mutableStateOf("") }
    
    LaunchedEffect(Unit) {
        if (!viewModel.isRandomSearchComplete.value) {
            viewModel.getRandomRecipes(null)
            viewModel.setRandomSearchComplete()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchText,
            onValueChange = { newText ->
                searchText = newText
                viewModel.updateSearchQuery(newText)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            placeholder = { Text("Search recipes...") },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_search_19),
                    contentDescription = "Search"
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray
            )
        )
        
        // Categories
        CategorySection(
            onCategoryClick = { category ->
                viewModel.getRandomRecipes(category)
            }
        )
        
        // Content
        when (uiState) {
            is SearchScreenState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            is SearchScreenState.ErrorNotFound -> {
                ErrorState()
            }
            
            is SearchScreenState.SearchResults -> {
                currentPagingFlow?.let { pagingFlow ->
                    val lazyPagingItems = pagingFlow.collectAsLazyPagingItems()
                    RecipeList(
                        lazyPagingItems = lazyPagingItems,
                        onRecipeClick = onRecipeClick
                    )
                } ?: run {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No recipes found",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategorySection(
    onCategoryClick: (String) -> Unit
) {
    val breadText = stringResource(R.string.bread)
    val breakfastText = stringResource(R.string.breakfast)
    val dessertText = stringResource(R.string.dessert)
    val saladText = stringResource(R.string.salad)
    val snackText = stringResource(R.string.snack)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 15.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.categories),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CategoryItem(
                    icon = R.drawable.ic_categories_foor,
                    label = breadText,
                    onClick = { onCategoryClick(breadText) }
                )
                CategoryItem(
                    icon = R.drawable.ic_categories_foor,
                    label = breakfastText,
                    onClick = { onCategoryClick(breakfastText) }
                )
                CategoryItem(
                    icon = R.drawable.ic_categories_foor,
                    label = dessertText,
                    onClick = { onCategoryClick(dessertText) }
                )
                CategoryItem(
                    icon = R.drawable.ic_categories_foor,
                    label = saladText,
                    onClick = { onCategoryClick(saladText) }
                )
                CategoryItem(
                    icon = R.drawable.ic_categories_foor,
                    label = snackText,
                    onClick = { onCategoryClick(snackText) }
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    icon: Int,
    label: String,
    onClick: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick }
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = label,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

// RecipeList и RecipeItem временно удалены для упрощения

@Composable
fun ErrorState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ph_not_found),
                contentDescription = "Not found",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.nothing_to_show),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun RecipeList(
    lazyPagingItems: LazyPagingItems<RecipeSummary>,
    onRecipeClick: (RecipeSummary) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count = lazyPagingItems.itemCount,
            key = { index -> lazyPagingItems[index]?.id ?: index }
        ) { index ->
            val recipe = lazyPagingItems[index]
            recipe?.let {
                RecipeItem(
                    recipe = it,
                    onClick = { onRecipeClick(it) }
                )
            }
        }
        
        // Loading state
        if (lazyPagingItems.loadState.append is androidx.paging.LoadState.Loading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        
        // Error state
        if (lazyPagingItems.loadState.append is androidx.paging.LoadState.Error) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error loading recipes",
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeItem(
    recipe: RecipeSummary,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Recipe Image
            AsyncImage(
                model = recipe.image,
                contentDescription = recipe.title,
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
                    text = recipe.title,
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
                        text = "${recipe.readyInMinutes} ${stringResource(R.string.minutes)}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = "${recipe.servings} ${stringResource(R.string.servings)}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
