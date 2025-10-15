package com.example.foodsearch.presentation.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodsearch.domain.cache.usecase.SaveRecipeToCacheUseCase
import com.example.foodsearch.domain.common.Result
import com.example.foodsearch.domain.models.RecipeDetails
import com.example.foodsearch.domain.search.usecase.GetAllRecipesUseCase
import com.example.foodsearch.domain.search.usecase.GetFavoriteRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val getFavoriteRecipesUseCase: GetFavoriteRecipesUseCase,
    private val getAllRecipesUseCase: GetAllRecipesUseCase,
    private val saveRecipeToCacheUseCase: SaveRecipeToCacheUseCase,
) : ViewModel() {
    
    private val _currentTabPosition = MutableStateFlow(0)
    val currentTabPosition: StateFlow<Int> = _currentTabPosition.asStateFlow()
    
    private val _favoriteRecipes = MutableStateFlow<List<RecipeDetails>>(emptyList())
    val favoriteRecipes: StateFlow<List<RecipeDetails>> = _favoriteRecipes.asStateFlow()
    
    private val _allRecipes = MutableStateFlow<List<RecipeDetails>>(emptyList())
    val allRecipes: StateFlow<List<RecipeDetails>> = _allRecipes.asStateFlow()

    fun setCurrentTabPosition(newPosition: Int) {
        _currentTabPosition.value = newPosition
    }
    
    fun loadFavoriteRecipes() {
        viewModelScope.launch {
            getFavoriteRecipesUseCase()
                .onSuccess { recipes ->
                    _favoriteRecipes.value = recipes
                }
                .onError { error ->
                    _favoriteRecipes.value = emptyList()
                }
        }
    }
    
    fun loadAllRecipes() {
        viewModelScope.launch {
            getAllRecipesUseCase()
                .onSuccess { recipes ->
                    _allRecipes.value = recipes
                }
                .onError { error ->
                    _allRecipes.value = emptyList()
                }
        }
    }
    
    fun refreshFavoriteRecipes() {
        loadFavoriteRecipes()
    }
    
    fun refreshAllRecipes() {
        loadAllRecipes()
    }
    
    // Метод для сохранения рецепта в кеш при добавлении в избранное
    fun saveRecipeToCache(recipe: RecipeDetails) {
        viewModelScope.launch {
            // Конвертируем RecipeDetails в RecipeSummary для сохранения
            val recipeSummary = com.example.foodsearch.domain.models.RecipeSummary(
                id = recipe.id,
                image = recipe.image,
                title = recipe.title,
                readyInMinutes = recipe.readyInMinutes,
                servings = recipe.servings,
                summary = recipe.summary
            )
            
            saveRecipeToCacheUseCase(recipeSummary)
                .onError { error ->
                    // Игнорируем ошибки сохранения
                }
        }
    }
}