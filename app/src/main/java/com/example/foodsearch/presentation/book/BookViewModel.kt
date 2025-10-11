package com.example.foodsearch.presentation.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodsearch.domain.models.RecipeDetails
import com.example.foodsearch.domain.search.SearchInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val searchInteractor: SearchInteractor
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
            try {
                val recipes = searchInteractor.getFavoriteRecipes()
                _favoriteRecipes.value = recipes
            } catch (e: Exception) {
                _favoriteRecipes.value = emptyList()
            }
        }
    }
    
    fun loadAllRecipes() {
        viewModelScope.launch {
            try {
                val recipes = searchInteractor.getAllRecipes()
                _allRecipes.value = recipes
            } catch (e: Exception) {
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
    fun saveRecipeToCache(recipe: com.example.foodsearch.domain.models.RecipeDetails) {
        viewModelScope.launch {
            try {
                searchInteractor.insertRecipeDetails(recipe)
            } catch (e: Exception) {
                // Игнорируем ошибки сохранения
            }
        }
    }
}
