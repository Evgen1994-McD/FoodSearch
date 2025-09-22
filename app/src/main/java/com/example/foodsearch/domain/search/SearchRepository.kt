package com.example.foodsearch.domain.search

import androidx.paging.PagingData
import com.example.foodsearch.data.db.entity.RecipeSummaryEntity
import com.example.foodsearch.data.search.dto.summary.RecipeSummaryDto
import com.example.foodsearch.domain.models.RecipeDetails
import com.example.foodsearch.domain.models.RecipeSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface SearchRepository {

     fun searchRecipe(expression: String): Flow<PagingData<RecipeSummary>>
    suspend fun searchRecipeCard(id: Int): String?
    suspend fun searchRecipeDetailsInfo(id: Int): RecipeDetails?
    fun getRandomRecipes(): Flow<PagingData<RecipeSummary>>

    suspend fun insertRecipeDetails(recipe: RecipeDetails)
    suspend fun insertRecipeSummary(recipe: RecipeSummary)
    suspend fun getRecipeSummaryFromMemory(query:String?): Flow<PagingData<RecipeSummary>>
    suspend fun getRecipeDetailsFromMemoryById(id: Int): RecipeDetails?
    suspend fun getFavoriteRecipes(): List<RecipeSummary>
}