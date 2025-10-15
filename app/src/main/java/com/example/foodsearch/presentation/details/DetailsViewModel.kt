package com.example.foodsearch.presentation.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodsearch.domain.cache.usecase.DeleteRecipeFromCacheUseCase
import com.example.foodsearch.domain.cache.usecase.GetCachedRecipeByIdUseCase
import com.example.foodsearch.domain.common.Result
import com.example.foodsearch.domain.models.RecipeDetails
import com.example.foodsearch.domain.search.usecase.GetRecipeDetailsUseCase
import com.example.foodsearch.domain.search.usecase.RemoveRecipeFromFavoritesUseCase
import com.example.foodsearch.domain.search.usecase.SaveRecipeToFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getRecipeDetailsUseCase: GetRecipeDetailsUseCase,
    private val saveRecipeToFavoritesUseCase: SaveRecipeToFavoritesUseCase,
    private val removeRecipeFromFavoritesUseCase: RemoveRecipeFromFavoritesUseCase,
    private val getCachedRecipeByIdUseCase: GetCachedRecipeByIdUseCase,
    private val deleteRecipeFromCacheUseCase: DeleteRecipeFromCacheUseCase,
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
        getCachedRecipeByIdUseCase(id)
            .onSuccess { recipe ->
                _isLiked.value = recipe?.isLike == true
            }
            .onError { error ->
                Log.e("DetailsViewModel", "Error getting recipe from database", error)
                _isLiked.value = false
            }
    }

    fun like() = viewModelScope.launch {
        currentRecipe?.let { recipe ->
            saveRecipeToFavoritesUseCase(recipe)
                .onSuccess {
                    currentRecipe = recipe.copy(isLike = true)
                    _isLiked.value = true
                    Log.d("DetailsViewModel", "Recipe liked: ${recipe.title}")
                }
                .onError { error ->
                    Log.e("DetailsViewModel", "Error liking recipe", error)
                }
        }
    }

    fun disLike() = viewModelScope.launch {
        currentRecipe?.let { recipe ->
            removeRecipeFromFavoritesUseCase(recipe)
                .onSuccess {
                    currentRecipe = recipe.copy(isLike = false)
                    _isLiked.value = false
                    Log.d("DetailsViewModel", "Recipe disliked: ${recipe.title}")
                }
                .onError { error ->
                    Log.e("DetailsViewModel", "Error disliking recipe", error)
                }
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
            
            getRecipeDetailsUseCase(recipeId)
                .onSuccess { recipe ->
                    currentRecipe = recipe
                    _uiState.value = DetailsSearchScreenState.SearchResults(recipe)
                    Log.d("DetailsViewModel", "Recipe $recipeId loaded successfully")
                    Log.d("DetailsViewModel", "Ingredients count: ${recipe.extendedIngredients?.size ?: 0}")
                    Log.d("DetailsViewModel", "Instructions count: ${recipe.analyzedInstructions?.size ?: 0}")
                }
                .onError { error ->
                    Log.e("DetailsViewModel", "Error loading recipe from network", error)
                    
                    // Проверяем, есть ли рецепт в кеше как последняя попытка
                    getCachedRecipeByIdUseCase(recipeId)
                        .onSuccess { cachedRecipe ->
                            if (cachedRecipe != null) {
                                currentRecipe = cachedRecipe
                                _uiState.value = DetailsSearchScreenState.SearchResults(cachedRecipe)
                                Log.d("DetailsViewModel", "Recipe $recipeId loaded from cache as fallback")
                            } else {
                                _uiState.value = DetailsSearchScreenState.ErrorNotFound(null)
                                Log.w("DetailsViewModel", "Recipe $recipeId not found in cache or network")
                            }
                        }
                        .onError { cacheError ->
                            Log.e("DetailsViewModel", "Error loading recipe from cache", cacheError)
                            _uiState.value = DetailsSearchScreenState.ErrorNotFound(null)
                        }
                }
        }
    }
    
    fun clearCache() = viewModelScope.launch {
        deleteRecipeFromCacheUseCase(id)
            .onSuccess {
                Log.d("DetailsViewModel", "Recipe $id deleted from cache")
            }
            .onError { error ->
                Log.e("DetailsViewModel", "Error deleting recipe from cache", error)
            }
    }
}