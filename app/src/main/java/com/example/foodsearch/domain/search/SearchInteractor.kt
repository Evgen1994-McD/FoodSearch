package com.example.foodsearch.domain.search

import com.example.foodsearch.domain.models.RecipeDetails
import com.example.foodsearch.domain.models.RecipeSummary
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {

    fun searchRecipe(expression: String): Flow<Pair<List<RecipeSummary>?, String?>>
    suspend fun searchRecipeCard(id: Int): String?
    suspend fun searchRecipeDetailsInfo(id: Int): Pair<RecipeDetails?, String?>
}