package com.example.foodsearch.presentation.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.foodsearch.data.search.dto.summary.RecipeSummaryDto
import com.example.foodsearch.domain.models.RecipeSummary
import com.example.foodsearch.domain.search.SearchInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchInteractor: SearchInteractor
) : ViewModel() {
    private val mutableScreenState = MutableLiveData<SearchScreenState>()
    val getLiveData: LiveData<SearchScreenState> get() = mutableScreenState


    fun searchRecipes(query: String): Flow<PagingData<RecipeSummary>> {
        return searchInteractor.searchRecipe(query)
    }


    fun getRandomRecipes(){
        viewModelScope.launch(Dispatchers.IO){
            mutableScreenState.postValue(SearchScreenState.Loading) // при начале запроса - выставляем лоадинг в тру
            searchInteractor.getRandomRecipes()
                .collect{ pair->

                    if(pair.first==null && pair.second == "Exception" ){
                        mutableScreenState.postValue(SearchScreenState.ErrorNoEnternet(pair.second.toString()))
                    }
                    if(pair.first.isNullOrEmpty() && pair.second==null){
                        mutableScreenState.postValue(SearchScreenState.ErrorNotFound(null))
                    }

                    else if (!pair.first.isNullOrEmpty()) {
                        mutableScreenState.postValue(SearchScreenState.SearchResults(pair.first))
                    }
                }


        }
    }





}