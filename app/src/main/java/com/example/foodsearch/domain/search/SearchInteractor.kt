package com.example.foodsearch.domain.search

import com.example.foodsearch.domain.models.Recipe
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {

    fun searchRecipe(expression: String): Flow<Pair<List<Recipe>?, String?>>
    suspend fun searchRecipeCard(id: Int): String?
}