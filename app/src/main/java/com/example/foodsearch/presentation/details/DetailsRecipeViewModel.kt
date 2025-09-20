package com.example.foodsearch.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodsearch.domain.search.SearchInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class DetailsRecipeViewModel @Inject constructor(
    private val searchInteractor: SearchInteractor
): ViewModel() {
private val id = 631852
    private val mutableScreenState = MutableLiveData<String?>()
    val getLiveData: LiveData<String?> get() = mutableScreenState

   fun getRecipeCard(){
       mutableScreenState.value = viewModelScope.launch {
           searchInteractor.searchRecipeCard(id)
       }.toString()
    }



}