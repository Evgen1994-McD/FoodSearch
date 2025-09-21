package com.example.foodsearch.domain.search

import com.example.foodsearch.domain.models.RecipeDetails
import com.example.foodsearch.domain.models.RecipeSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface SearchRepository {

     fun searchRecipe(expression: String):Flow<List<RecipeSummary>?>
    suspend fun searchRecipeCard(id: Int): String?
    suspend fun searchRecipeDetailsInfo(id: Int): RecipeDetails?
    fun getRandomRecipes(): Flow<List<RecipeSummary>?>

}