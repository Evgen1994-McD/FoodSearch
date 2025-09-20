package com.example.foodsearch.presentation.search

import com.example.foodsearch.domain.models.RecipeSummary

sealed class SearchScreenState {

        object Loading : SearchScreenState()
        data class SearchResults(val data: List<RecipeSummary>?) : SearchScreenState()
        data class ErrorNotFound(val message: String?) : SearchScreenState()
        data class ErrorNoEnternet(val message: String?) : SearchScreenState()


}