package com.example.foodsearch.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.foodsearch.domain.models.RecipeSummary
import com.example.foodsearch.domain.search.SearchInteractor
import com.example.foodsearch.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchInteractor: SearchInteractor,
    private val networkUtils: NetworkUtils,
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
        Log.d("SearchViewModel", "ViewModel initialized")
        // Observe search query changes and trigger search
        searchQuery
            .debounce(500) // Уменьшаем debounce до 500ms для более быстрого поиска
            .distinctUntilChanged()
            .onEach { query ->
                Log.d("SearchViewModel", "Search query changed: '$query'")
                if (query.isNotEmpty()) {
                    Log.d("SearchViewModel", "Triggering search for: '$query'")
                    searchRecipes(query)
                }
            }
            .launchIn(viewModelScope)
            
        // Инициализация при запуске - проверяем сеть и загружаем соответствующие данные
        initializeOnStartup()
    }

    fun searchRecipes(query: String) {
        Log.d("SearchViewModel", "searchRecipes called with query: '$query'")
        _uiState.value = SearchScreenState.Loading

        viewModelScope.launch(Dispatchers.Main) {
            try {
                Log.d("SearchViewModel", "Starting search with network check")
                val isNetworkAvailable = networkUtils.isNetworkAvailable()
                Log.d("SearchViewModel", "Network available: $isNetworkAvailable")
                
                if (isNetworkAvailable) {
                    try {
                        val pagingFlow = searchInteractor.getRecipesWithNetworkCheck(query, 1, 4)
                        Log.d("SearchViewModel", "Got paging flow from interactor")
                        _currentPagingFlow.value = pagingFlow
                        _uiState.value = SearchScreenState.SearchReady
                    } catch (e: Exception) {
                        Log.e("SearchViewModel", "Network request failed, showing offline mode", e)
                        _uiState.value = SearchScreenState.OfflineMode
                    }
                } else {
                    Log.d("SearchViewModel", "No network, showing offline mode")
                    _uiState.value = SearchScreenState.OfflineMode
                }
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error in searchRecipes", e)
                _uiState.value = SearchScreenState.OfflineMode
            }
        }
    }

    fun getRandomRecipes(query: String?) {
        Log.d("SearchViewModel", "getRandomRecipes called with query: '$query'")
        _uiState.value = SearchScreenState.Loading

        viewModelScope.launch(Dispatchers.Main) {
            try {
                val isNetworkAvailable = networkUtils.isNetworkAvailable()
                Log.d("SearchViewModel", "Network available for random recipes: $isNetworkAvailable")
                
                if (isNetworkAvailable) {
                    try {
                        val pagingFlow = searchInteractor.getRandomRecipesWithNetworkCheck(1, 4, query)
                        Log.d("SearchViewModel", "Got random recipes paging flow")
                        _currentPagingFlow.value = pagingFlow
                        _uiState.value = SearchScreenState.SearchReady
                    } catch (e: Exception) {
                        Log.e("SearchViewModel", "Random recipes network request failed, showing offline mode", e)
                        _uiState.value = SearchScreenState.OfflineMode
                    }
                } else {
                    Log.d("SearchViewModel", "No network, showing offline mode")
                    _uiState.value = SearchScreenState.OfflineMode
                }
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error in getRandomRecipes", e)
                _uiState.value = SearchScreenState.OfflineMode
            }
        }
    }
    
    fun getRecipeFromDb(query: String?) = viewModelScope.launch {
        Log.d("SearchViewModel", "getRecipeFromDb called with query: '$query'")
        try {
            val pagingFlow = searchInteractor.getRecipeFromMemory(query)
            Log.d("SearchViewModel", "Got cached recipes paging flow: ${pagingFlow != null}")
            _currentPagingFlow.value = pagingFlow
            
            // Всегда устанавливаем OfflineMode для кешированных рецептов
            Log.d("SearchViewModel", "Setting offline mode for cached recipes")
            _uiState.value = SearchScreenState.OfflineMode
            Log.d("SearchViewModel", "UI state set to OfflineMode")
        } catch (e: Exception) {
            Log.e("SearchViewModel", "Error in getRecipeFromDb", e)
            // Если нет кешированных рецептов, показываем соответствующее состояние
            val isNetworkAvailable = networkUtils.isNetworkAvailable()
            if (!isNetworkAvailable) {
                Log.d("SearchViewModel", "No network and no cache, showing offline state")
                _uiState.value = SearchScreenState.OfflineMode
            } else {
                Log.d("SearchViewModel", "Network available but no cache, showing error")
                _uiState.value = SearchScreenState.ErrorNotFound(PagingData.empty())
            }
        }
        Log.d("SearchViewModel", "getRecipeFromDb completed")
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
            try {
                // Конвертируем RecipeSummary в RecipeDetails для сохранения
                val recipeDetails = com.example.foodsearch.domain.models.RecipeDetails(
                    id = recipe.id,
                    image = recipe.image,
                    imageType = null,
                    title = recipe.title,
                    readyInMinutes = recipe.readyInMinutes,
                    servings = recipe.servings,
                    sourceUrl = null,
                    vegetarian = null,
                    vegan = null,
                    glutenFree = null,
                    dairyFree = null,
                    veryHealthy = null,
                    cheap = null,
                    veryPopular = null,
                    sustainable = null,
                    lowFodmap = null,
                    weightWatcherSmartPoints = null,
                    gaps = null,
                    preparationMinutes = null,
                    cookingMinutes = null,
                    aggregateLikes = null,
                    healthScore = null,
                    creditsText = null,
                    license = null,
                    sourceName = null,
                    pricePerServing = null,
                    extendedIngredients = null,
                    summary = recipe.summary,
                    cuisines = null,
                    dishTypes = null,
                    diets = null,
                    occasions = null,
                    instructions = null,
                    analyzedInstructions = null,
                    spoonacularScore = null,
                    spoonacularSourceUrl = null,
                    isLike = false
                )
                searchInteractor.insertRecipeDetails(recipeDetails)
            } catch (e: Exception) {
                // Игнорируем ошибки сохранения, так как это не критично
            }
        }
    }
    
    private fun initializeOnStartup() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                Log.d("SearchViewModel", "Initializing on startup")
                val isNetworkAvailable = networkUtils.isNetworkAvailable()
                Log.d("SearchViewModel", "Network available on startup: $isNetworkAvailable")
                
                if (!isNetworkAvailable) {
                    Log.d("SearchViewModel", "No network on startup, showing offline mode")
                    _uiState.value = SearchScreenState.OfflineMode
                } else {
                    Log.d("SearchViewModel", "Network available on startup, loading random recipes")
                    // Есть сеть - загружаем случайные рецепты
                    getRandomRecipes(null)
                    setRandomSearchComplete()
                }
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error during startup initialization", e)
                // В случае ошибки показываем офлайн режим
                _uiState.value = SearchScreenState.OfflineMode
            }
        }
    }
}




