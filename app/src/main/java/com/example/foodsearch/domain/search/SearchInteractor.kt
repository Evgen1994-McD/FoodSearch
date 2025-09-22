package com.example.foodsearch.domain.search

import androidx.paging.PagingData
import com.example.foodsearch.domain.models.RecipeDetails
import com.example.foodsearch.domain.models.RecipeSummary
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {

    fun searchRecipe(expression: String):  Flow<PagingData<RecipeSummary>>

    fun getRandomRecipes():Flow<PagingData<RecipeSummary>>
    suspend fun searchRecipeCard(id: Int): String?
    suspend fun searchRecipeDetailsInfo(id: Int): Pair<RecipeDetails?, String?>


    suspend fun getRecipeFromMemory(query:String?): Flow<PagingData<RecipeSummary>>
    suspend fun insertRecipeDetails(recipeDetails: RecipeDetails)
    suspend fun getRecipeDetailsById(id: Int): RecipeDetails?
}