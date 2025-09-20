package com.example.foodsearch.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodsearch.domain.search.SearchInteractor
import com.example.foodsearch.presentation.search.SearchScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class DetailsRecipeViewModel @Inject constructor(
    private val searchInteractor: SearchInteractor,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    //private val id = 631852
    val id: Int = savedStateHandle.get<Int>("id") ?: -1

    private val mutableScreenState = MutableLiveData<DetailsSearchScreenState?>()
    val getLiveData: LiveData<DetailsSearchScreenState?> get() = mutableScreenState

//   fun getRecipeCard() = viewModelScope.launch {
//       mutableScreenState.postValue( searchInteractor.searchRecipeCard(id))
//       }.toString()



    fun getDetailsRecipeInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            mutableScreenState.postValue(DetailsSearchScreenState.Loading) // при начале запроса - выставляем лоадинг в тру
            val pair = searchInteractor.searchRecipeDetailsInfo(id)
            when {
                pair.first == null && pair.second == "Exception" -> {
                    mutableScreenState.postValue(DetailsSearchScreenState.ErrorNoEnternet(pair.second))
                }
                pair.first == null && pair.second == null -> {
                    mutableScreenState.postValue(DetailsSearchScreenState.ErrorNotFound(null))
                }
                pair.first != null -> {
                    mutableScreenState.postValue(DetailsSearchScreenState.SearchResults(pair.first))
                }
            }
        }
    }

    }








