package com.example.foodsearch.presentation.book.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodsearch.domain.models.RecipeSummary
import com.example.foodsearch.domain.search.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: SearchRepository
):ViewModel() {


    private val mutableScreenState = MutableLiveData<List<RecipeSummary>>()
    val getLiveData: LiveData<List<RecipeSummary>> get() = mutableScreenState


    fun getRecipes()=viewModelScope.launch {
        mutableScreenState.postValue(repository.getFavoriteRecipes())
    }







}