package com.example.foodsearch.presentation.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodsearch.domain.models.RecipeDetails
import com.example.foodsearch.domain.search.SearchInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val searchInteractor: SearchInteractor,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    
    val id: Int = savedStateHandle.get<String>("recipeId")?.toIntOrNull() ?: -1
    private var currentRecipe: RecipeDetails? = null
    
    init {
        Log.d("DetailsViewModel", "Initialized with id: $id")
    }

    private val _uiState = MutableStateFlow<DetailsSearchScreenState>(DetailsSearchScreenState.Loading)
    val uiState: StateFlow<DetailsSearchScreenState> = _uiState.asStateFlow()

    private val _isLiked = MutableStateFlow(false)
    val isLiked: StateFlow<Boolean> = _isLiked.asStateFlow()

    suspend fun tryGetRecipeFromDataBase() {
        try {
            val recipe = searchInteractor.getRecipeDetailsById(id)
            _isLiked.value = recipe?.isLike == true
        } catch (e: Exception) {
            Log.e("DetailsViewModel", "Error in tryGetRecipeFromDataBase()", e)
            _isLiked.value = false
        }
    }

    suspend fun replaceRecipe(recipeDetails: RecipeDetails) {
        try {
            searchInteractor.insertRecipeDetails(recipeDetails)
        } catch (e: Exception) {
            Log.e("DetailsViewModel", "Error in replaceRecipe()", e)
            throw e
        }
    }

    fun like() = viewModelScope.launch {
        try {
            currentRecipe?.let { recipe ->
                val updatedRecipe = recipe.copy(isLike = true)
                replaceRecipe(updatedRecipe)
                currentRecipe = updatedRecipe
                _isLiked.value = true
                Log.d("DetailsViewModel", "Recipe liked: ${recipe.title}")
            }
        } catch (e: Exception) {
            Log.e("DetailsViewModel", "Error in like()", e)
        }
    }

    fun disLike() = viewModelScope.launch {
        try {
            currentRecipe?.let { recipe ->
                val updatedRecipe = recipe.copy(isLike = false)
                replaceRecipe(updatedRecipe)
                currentRecipe = updatedRecipe
                _isLiked.value = false
                Log.d("DetailsViewModel", "Recipe disliked: ${recipe.title}")
            }
        } catch (e: Exception) {
            Log.e("DetailsViewModel", "Error in disLike()", e)
        }
    }

    fun getDetailsRecipeInfo(recipeId: Int = id) {
        Log.d("DetailsViewModel", "getDetailsRecipeInfo called with recipeId: $recipeId")
        
        if (recipeId <= 0) {
            Log.e("DetailsViewModel", "Invalid recipeId: $recipeId")
            _uiState.value = DetailsSearchScreenState.ErrorNotFound(null)
            return
        }
        
        viewModelScope.launch(Dispatchers.IO) {
            try {
                tryGetRecipeFromDataBase()
            } catch (e: Exception) {
                Log.d("DetailsViewModel", "Error getting recipe from database: ${e.message}")
            }

            _uiState.value = DetailsSearchScreenState.Loading
            val recipe = searchInteractor.searchRecipeDetailsInfo(recipeId)
            
            if (recipe != null) {
                currentRecipe = recipe
                _uiState.value = DetailsSearchScreenState.SearchResults(recipe)
                Log.d("DetailsViewModel", "Recipe $recipeId loaded successfully")
            } else {
                // Проверяем, есть ли рецепт в кеше как последняя попытка
                val cachedRecipe = searchInteractor.getRecipeDetailsById(recipeId)
                if (cachedRecipe != null) {
                    currentRecipe = cachedRecipe
                    _uiState.value = DetailsSearchScreenState.SearchResults(cachedRecipe)
                    Log.d("DetailsViewModel", "Recipe $recipeId loaded from cache as fallback")
                } else {
                    _uiState.value = DetailsSearchScreenState.ErrorNotFound(null)
                    Log.w("DetailsViewModel", "Recipe $recipeId not found in cache or network")
                }
            }
        }
    }
}
