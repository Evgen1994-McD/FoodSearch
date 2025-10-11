package com.example.foodsearch.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
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
    
    // Store the current PagingData flow
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
                val flow = searchInteractor.searchRecipe(query)
                    .cachedIn(viewModelScope)
                
                _currentPagingFlow.value = flow
                _uiState.value = SearchScreenState.SearchResults(PagingData.empty())
            } catch (e: Exception) {
                _uiState.value = SearchScreenState.ErrorNotFound(PagingData.empty())
            }
        }
    }

    fun getRandomRecipes(query: String?) {
        _uiState.value = SearchScreenState.Loading

        viewModelScope.launch(Dispatchers.Main) {
            try {
                val flow = searchInteractor.getRandomRecipes(query)
                    .cachedIn(viewModelScope)
                
                _currentPagingFlow.value = flow
                _uiState.value = SearchScreenState.SearchResults(PagingData.empty())
            } catch (e: Exception) {
                _uiState.value = SearchScreenState.ErrorNotFound(PagingData.empty())
            }
        }
    }

    fun getRecipeFromDb(query: String?) = viewModelScope.launch {
        searchInteractor.getRecipeFromMemory(query)
            .cachedIn(viewModelScope)
            .collectLatest { data ->
                _uiState.value = SearchScreenState.SearchResults(data)
            }
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun setRandomSearchComplete() {
        _isRandomSearchComplete.value = true
    }
}




