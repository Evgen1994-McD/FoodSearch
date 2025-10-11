package com.example.foodsearch.presentation.search

import androidx.paging.PagingData
import com.example.foodsearch.domain.models.RecipeSummary

sealed class SearchScreenState {
    object Loading : SearchScreenState()
    data class SearchResults(val data: PagingData<RecipeSummary>) : SearchScreenState()
    data class ErrorNotFound(val error: PagingData<RecipeSummary>) : SearchScreenState()
}