package com.example.foodsearch.domain.search

import androidx.paging.PagingData
import com.example.foodsearch.domain.models.RecipeDetails
import com.example.foodsearch.domain.models.RecipeSummary
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {

    fun searchRecipe(expression: String): Flow<PagingData<RecipeSummary>>
    fun getRandomRecipes(query: String?): Flow<PagingData<RecipeSummary>>
    suspend fun searchRecipeDetailsInfo(id: Int): RecipeDetails?
    suspend fun getRecipeFromMemory(query: String?): Flow<PagingData<RecipeSummary>>
    suspend fun insertRecipeDetails(recipeDetails: RecipeDetails)
    suspend fun getRecipeDetailsById(id: Int): RecipeDetails?
    suspend fun getFavoriteRecipes(): List<RecipeDetails>
    suspend fun getAllRecipes(): List<RecipeDetails>
    suspend fun saveRecipesToCache(recipes: List<RecipeSummary>)
    
    suspend fun clearCache()
    suspend fun deleteRecipeById(id: Int)
    
    suspend fun getRecipesWithNetworkCheck(query: String, pageNumber: Int, pageSize: Int): Flow<PagingData<RecipeSummary>>
    
    suspend fun getRandomRecipesWithNetworkCheck(pageNumber: Int, pageSize: Int, type: String?): Flow<PagingData<RecipeSummary>>
}