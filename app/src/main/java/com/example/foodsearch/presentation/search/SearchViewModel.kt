package com.example.foodsearch.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.foodsearch.domain.cache.usecase.SaveRecipeToCacheUseCase
import com.example.foodsearch.domain.common.Result
import com.example.foodsearch.domain.models.RecipeSummary
import com.example.foodsearch.domain.search.usecase.GetRandomRecipesUseCase
import com.example.foodsearch.domain.search.usecase.SearchRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRecipesUseCase: SearchRecipesUseCase,
    private val getRandomRecipesUseCase: GetRandomRecipesUseCase,
    private val saveRecipeToCacheUseCase: SaveRecipeToCacheUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchScreenState>(SearchScreenState.Loading)
    val uiState: StateFlow<SearchScreenState> = _uiState.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _isRandomSearchComplete = MutableStateFlow(false)
    val isRandomSearchComplete: StateFlow<Boolean> = _isRandomSearchComplete.asStateFlow()
    
    private val _currentPagingFlow = MutableStateFlow<Flow<PagingData<RecipeSummary>>?>(null)
    val currentPagingFlow: StateFlow<Flow<PagingData<RecipeSummary>>?> = _currentPagingFlow.asStateFlow()
    
    init {
        searchQuery
            .debounce(500)
            .distinctUntilChanged()
            .onEach { query ->
                if (query.isNotEmpty()) {
                    searchRecipes(query)
                }
            }
            .launchIn(viewModelScope)
            
        initializeOnStartup()
    }

    fun searchRecipes(query: String) {
        _uiState.value = SearchScreenState.Loading

        viewModelScope.launch(Dispatchers.Main) {
            try {
                val pagingFlow = searchRecipesUseCase(query)
                _currentPagingFlow.value = pagingFlow
                _uiState.value = SearchScreenState.SearchReady
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error searching recipes", e)
                _uiState.value = SearchScreenState.OfflineMode
            }
        }
    }

    fun getRandomRecipes(query: String?) {
        _uiState.value = SearchScreenState.Loading

        viewModelScope.launch(Dispatchers.Main) {
            try {
                val pagingFlow = getRandomRecipesUseCase(query)
                _currentPagingFlow.value = pagingFlow
                _uiState.value = SearchScreenState.SearchReady
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error getting random recipes", e)
                _uiState.value = SearchScreenState.OfflineMode
            }
        }
    }
    
    fun updateSearchQuery(query: String) {
        Log.d("SearchViewModel", "updateSearchQuery called with: '$query'")
        _searchQuery.value = query
    }
    
    fun setRandomSearchComplete() {
        _isRandomSearchComplete.value = true
    }
    
    // Метод для сохранения рецепта в кеш при клике
    fun saveRecipeToCache(recipe: RecipeSummary) {
        viewModelScope.launch(Dispatchers.IO) {
            saveRecipeToCacheUseCase(recipe)
                .onError { error ->
                    Log.e("SearchViewModel", "Error saving recipe to cache", error)
                }
        }
    }
    
    private fun initializeOnStartup() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                Log.d("SearchViewModel", "Initializing on startup")
                // Загружаем случайные рецепты при запуске
                getRandomRecipes(null)
                setRandomSearchComplete()
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error during startup initialization", e)
                _uiState.value = SearchScreenState.OfflineMode
            }
        }
    }
}