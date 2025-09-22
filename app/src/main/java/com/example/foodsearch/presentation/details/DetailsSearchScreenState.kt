package com.example.foodsearch.presentation.details

import com.example.foodsearch.domain.models.RecipeDetails
import com.example.foodsearch.domain.models.RecipeSummary

sealed class DetailsSearchScreenState {


    object Loading : DetailsSearchScreenState()
    data class SearchResults(val data: RecipeDetails?) : DetailsSearchScreenState()
    data class ErrorNotFound(val message: String?) : DetailsSearchScreenState()
    data class ErrorNoEnternet(val message: String?) : DetailsSearchScreenState()


}