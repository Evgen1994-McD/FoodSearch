package com.example.foodsearch.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.foodsearch.domain.models.RecipeSummary
import com.example.foodsearch.domain.search.SearchInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchInteractor: SearchInteractor,
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
        // Observe search query changes and trigger search
        searchQuery
            .debounce(2000)
            .distinctUntilChanged()
            .onEach { query ->
                if (query.isNotEmpty()) {
                    searchRecipes(query)
                }
            }
            .launchIn(viewModelScope)
    }

    fun searchRecipes(query: String) {
        _uiState.value = SearchScreenState.Loading

        viewModelScope.launch(Dispatchers.Main) {
            try {
                val pagingFlow = searchInteractor.searchRecipe(query)
                _currentPagingFlow.value = pagingFlow
                _uiState.value = SearchScreenState.SearchResults(PagingData.empty())
                
                // Рецепты из поиска будут сохранены в кеш при клике на них
                // через метод saveRecipeToCache в MainActivity
            } catch (e: Exception) {
                _uiState.value = SearchScreenState.ErrorNotFound(PagingData.empty())
            }
        }
    }

    fun getRandomRecipes(query: String?) {
        _uiState.value = SearchScreenState.Loading

        viewModelScope.launch(Dispatchers.Main) {
            try {
                val pagingFlow = searchInteractor.getRandomRecipes(query)
                _currentPagingFlow.value = pagingFlow
                _uiState.value = SearchScreenState.SearchResults(PagingData.empty())
                
                // Сохраняем рецепты из категорий в кеш для офлайн доступа
                // Это будет происходить автоматически при загрузке деталей рецепта
            } catch (e: Exception) {
                _uiState.value = SearchScreenState.ErrorNotFound(PagingData.empty())
            }
        }
    }

    fun getRecipeFromDb(query: String?) = viewModelScope.launch {
        try {
            val pagingFlow = searchInteractor.getRecipeFromMemory(query)
            _currentPagingFlow.value = pagingFlow
            _uiState.value = SearchScreenState.SearchResults(PagingData.empty())
        } catch (e: Exception) {
            _uiState.value = SearchScreenState.ErrorNotFound(PagingData.empty())
        }
    }
    
    fun updateSearchQuery(query: String) {
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
}




