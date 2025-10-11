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
    
    val id: Int = savedStateHandle.get<Int>("id") ?: -1
    private var currentRecipe: RecipeDetails? = null

    private val _uiState = MutableStateFlow<DetailsSearchScreenState>(DetailsSearchScreenState.Loading)
    val uiState: StateFlow<DetailsSearchScreenState> = _uiState.asStateFlow()

    private val _isLiked = MutableStateFlow(false)
    val isLiked: StateFlow<Boolean> = _isLiked.asStateFlow()

    suspend fun tryGetRecipeFromDataBase() {
        val recipe = searchInteractor.getRecipeDetailsById(id)
        _isLiked.value = recipe?.isLike == true
    }

    suspend fun replaceRecipe(recipeDetails: RecipeDetails) {
        searchInteractor.insertRecipeDetails(recipeDetails)
    }

    fun like() = viewModelScope.launch {
        val recipe = currentRecipe?.copy(isLike = true)
        recipe?.let { replaceRecipe(it) }
        tryGetRecipeFromDataBase()
    }

    fun disLike() = viewModelScope.launch {
        val recipe = currentRecipe?.copy(isLike = false)
        recipe?.let { replaceRecipe(it) }
        tryGetRecipeFromDataBase()
    }

    fun getDetailsRecipeInfo(recipeId: Int = id) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                tryGetRecipeFromDataBase()
            } catch (e: Exception) {
                Log.d("error", " $  ${e.message}")
            }

            _uiState.value = DetailsSearchScreenState.Loading
            val pair = searchInteractor.searchRecipeDetailsInfo(recipeId)
            when {
                pair.first == null && pair.second == "Exception" -> {
                    _uiState.value = DetailsSearchScreenState.ErrorNoEnternet(pair.second)
                }

                pair.first == null && pair.second == null -> {
                    _uiState.value = DetailsSearchScreenState.ErrorNotFound(null)
                }

                pair.first != null -> {
                    currentRecipe = pair.first
                    _uiState.value = DetailsSearchScreenState.SearchResults(pair.first)
                }
            }
        }
    }
}
