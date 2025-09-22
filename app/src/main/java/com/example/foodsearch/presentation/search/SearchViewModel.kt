package com.example.foodsearch.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.foodsearch.domain.models.RecipeSummary
import com.example.foodsearch.domain.search.SearchInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val mutableScreenState = MutableLiveData<SearchScreenState>()
    val getLiveData: LiveData<SearchScreenState> get() = mutableScreenState

    fun searchRecipes(query: String) {
        mutableScreenState.postValue(SearchScreenState.Loading)

        viewModelScope.launch(Dispatchers.Main) {
            try {
                val flow = searchInteractor.searchRecipe(query)
                    .cachedIn(viewModelScope)

                flow.collectLatest { data ->
                    mutableScreenState.value = SearchScreenState.SearchResults(data)
                }
            } catch (e: Exception) {
                mutableScreenState.value = SearchScreenState.ErrorNotFound(PagingData.empty())
            }
        }
    }


    fun getRandomRecipes() {
        mutableScreenState.postValue(SearchScreenState.Loading)

        viewModelScope.launch(Dispatchers.Main) {
            try {
                val flow = searchInteractor.getRandomRecipes()

                    .cachedIn(viewModelScope)

                flow.collectLatest { data ->
                    mutableScreenState.value = SearchScreenState.SearchResults(data)
                }
            } catch (e: Exception) {
                mutableScreenState.value = SearchScreenState.ErrorNotFound(PagingData.empty())
            }
        }
    }
     fun getRecipeFromDb(query: String?)=viewModelScope.launch{
        searchInteractor.getRecipeFromMemory(query)
            .cachedIn(viewModelScope)
            .collectLatest { data ->
                mutableScreenState.value = SearchScreenState.SearchResults(data)
            }
    }

}



