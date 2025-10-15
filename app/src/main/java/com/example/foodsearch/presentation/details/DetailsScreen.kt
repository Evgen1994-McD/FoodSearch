package com.example.foodsearch.presentation.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.foodsearch.R
import com.example.foodsearch.domain.models.RecipeDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    recipeId: Int,
    navController: NavController,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isLiked by viewModel.isLiked.collectAsStateWithLifecycle()
    
    var ingredientsExpanded by remember { mutableStateOf(true) }
    var instructionsExpanded by remember { mutableStateOf(true) }
    
    LaunchedEffect(recipeId) {
        viewModel.getDetailsRecipeInfo(recipeId)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 16.dp), // Уменьшаем отступ сверху
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Recipe Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 20.dp)
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Кнопка удаления текущего рецепта
            IconButton(
                onClick = { viewModel.clearCache() }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Recipe",
                    tint = Color.Red
                )
            }
        }
        
        when (uiState) {
            is DetailsSearchScreenState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            is DetailsSearchScreenState.ErrorNotFound -> {
                ErrorState()
            }
            
            is DetailsSearchScreenState.ErrorNoEnternet -> {
                ErrorState()
            }
            
            is DetailsSearchScreenState.SearchResults -> {
                val searchResults = (uiState as DetailsSearchScreenState.SearchResults).data
                RecipeDetailsContent(
                    recipe = searchResults,
                    isLiked = isLiked,
                    onLikeClick = { viewModel.like() },
                    onDislikeClick = { viewModel.disLike() },
                    ingredientsExpanded = ingredientsExpanded,
                    instructionsExpanded = instructionsExpanded,
                    onIngredientsExpandedChange = { ingredientsExpanded = it },
                    onInstructionsExpandedChange = { instructionsExpanded = it }
                )
            }
            
            else -> {}
        }
    }
}

@Composable
fun RecipeDetailsContent(
    recipe: RecipeDetails?,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    onDislikeClick: () -> Unit,
    ingredientsExpanded: Boolean,
    instructionsExpanded: Boolean,
    onIngredientsExpandedChange: (Boolean) -> Unit,
    onInstructionsExpandedChange: (Boolean) -> Unit
) {
    // Логирование для отладки
    LaunchedEffect(recipe) {
        recipe?.let {
            android.util.Log.d("DetailsScreen", "Recipe loaded: ${it.title}")
            android.util.Log.d("DetailsScreen", "Ingredients count: ${it.extendedIngredients?.size ?: 0}")
            android.util.Log.d("DetailsScreen", "Instructions count: ${it.analyzedInstructions?.size ?: 0}")
        }
    }

    if (recipe == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Recipe not found",
                fontSize = 18.sp,
                color = Color.Gray
            )
        }
        return
    }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Recipe Image
            AsyncImage(
                model = recipe.image,
                contentDescription = recipe.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.ic_ph_kitchen),
                placeholder = painterResource(R.drawable.ic_ph_kitchen)
            )
        }
        
        item {
            // Recipe Title and Like Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recipe.title ?: "",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                
                IconButton(
                    onClick = if (isLiked) onDislikeClick else onLikeClick
                ) {
                    Icon(
                        painter = painterResource(
                            if (isLiked) R.drawable.ic_like_red_51dp else R.drawable.ic_like_51dp
                        ),
                        contentDescription = if (isLiked) "Unlike" else "Like",
                        tint = if (isLiked) Color.Red else Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
        
        item {
            // Recipe Info Cards
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    InfoCard(
                        icon = R.drawable.ic_servings_24,
                        title = stringResource(R.string.servings),
                        value = recipe?.servings?.toString() ?: "0"
                    )
                }
                item {
                    InfoCard(
                        icon = R.drawable.ic_time_24,
                        title = stringResource(R.string.minutes),
                        value = recipe?.readyInMinutes?.toString() ?: "0"
                    )
                }
                item {
                    InfoCard(
                        icon = R.drawable.ic_price_40,
                        title = stringResource(R.string.cost),
                        value = recipe?.pricePerServing?.toString() ?: "0"
                    )
                }
            }
        }
        
        item {
            // Tags
            val tags = getTags(recipe)
            if (tags.isNotEmpty()) {
                Text(
                    text = "Tags: ${tags.joinToString(", ")}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
        
        item {
            // Dish Types
            if (!recipe?.dishTypes.isNullOrEmpty()) {
                Text(
                    text = "Dish Types: ${recipe?.dishTypes?.joinToString(", ")}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
        
        item {
            // Ingredients Section
            IngredientsSection(
                ingredients = recipe?.extendedIngredients ?: emptyList(),
                expanded = ingredientsExpanded,
                onExpandedChange = onIngredientsExpandedChange
            )
        }
        
        item {
            // Instructions Section
            InstructionsSection(
                instructions = recipe?.analyzedInstructions?.flatMap { it.steps } ?: emptyList(),
                expanded = instructionsExpanded,
                onExpandedChange = onInstructionsExpandedChange
            )
        }
    }
}

@Composable
fun InfoCard(
    icon: Int,
    title: String,
    value: String
) {
    Card(
        modifier = Modifier.width(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun IngredientsSection(
    ingredients: List<com.example.foodsearch.domain.models.OtherModels.Ingredient>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)) // Светло-серый фон
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpandedChange(!expanded) }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ingredients (${ingredients.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier.rotate(if (expanded) 0f else 0f)
                )
            }
            
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(animationSpec = tween(300)),
                exit = shrinkVertically(animationSpec = tween(300))
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    if (ingredients.isEmpty()) {
                        Text(
                            text = "No ingredients available",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    } else {
                        ingredients.forEach { ingredient ->
                            IngredientItem(ingredient = ingredient)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IngredientItem(
    ingredient: com.example.foodsearch.domain.models.OtherModels.Ingredient
) {
    val imageUrl = if (ingredient.image.isNotEmpty()) {
        // Проверяем, является ли URL полным или относительным
        if (ingredient.image.startsWith("http")) {
            ingredient.image
        } else {
            // Если это относительный путь, добавляем базовый URL Spoonacular
            "https://spoonacular.com/cdn/ingredients_100x100/${ingredient.image}"
        }
    } else null
    
    val painter = rememberAsyncImagePainter(
        model = imageUrl,
        error = painterResource(R.drawable.ic_ph_kitchen),
        placeholder = painterResource(R.drawable.ic_ph_kitchen)
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFBFC)) // Очень светлый фон для элементов
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
        Image(
            painter = painter,
            contentDescription = ingredient.name,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ingredient.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = "${ingredient.amount} ${ingredient.unit}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun InstructionsSection(
    instructions: List<com.example.foodsearch.domain.models.OtherModels.Step>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)) // Светло-серый фон
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpandedChange(!expanded) }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Instructions (${instructions.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand"
                )
            }
            
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(animationSpec = tween(300)),
                exit = shrinkVertically(animationSpec = tween(300))
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    if (instructions.isEmpty()) {
                        Text(
                            text = "No instructions available",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    } else {
                        instructions.forEachIndexed { index, step ->
                            InstructionItem(
                                stepNumber = index + 1,
                                instruction = step.step
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InstructionItem(
    stepNumber: Int,
    instruction: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFBFC)) // Очень светлый фон для элементов
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stepNumber.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = instruction,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

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
                painter = painterResource(R.drawable.ic_404),
                contentDescription = "Error",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Recipe not found",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun getTags(recipe: RecipeDetails?): List<String> {
    val tags = mutableListOf<String>()

    if (recipe?.vegetarian == true) tags.add("vegetarian")
    if (recipe?.vegan == true) tags.add("vegan")
    if (recipe?.glutenFree == true) tags.add("gluten-free")
    if (recipe?.dairyFree == true) tags.add("dairy-free")
    if (recipe?.veryHealthy == true) tags.add("very healthy")
    if (recipe?.cheap == true) tags.add("cheap")
    if (recipe?.veryPopular == true) tags.add("very popular")
    if (recipe?.sustainable == true) tags.add("sustainable")
    if (recipe?.lowFodmap == true) tags.add("low FODMAP")

    return tags
}


