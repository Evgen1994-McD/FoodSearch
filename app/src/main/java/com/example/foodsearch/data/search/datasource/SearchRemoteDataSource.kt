package com.example.foodsearch.data.search.datasource

import androidx.paging.PagingData
import com.example.foodsearch.data.search.dto.details.RecipeDetailsDto
import com.example.foodsearch.data.search.dto.summary.RecipeSummaryDto
import com.example.foodsearch.data.search.network.NetworkClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Data Source для сетевых операций с рецептами
 */
class SearchRemoteDataSource @Inject constructor(
    val networkClient: NetworkClient
) {
    
    suspend fun searchRecipes(query: String, pageNumber: Int, pageSize: Int): Flow<PagingData<RecipeSummaryDto>> {
        // Логика получения рецептов из сети будет реализована в PagingSource
        throw NotImplementedError("Will be implemented in PagingSource")
    }
    
    suspend fun getRandomRecipes(pageNumber: Int, pageSize: Int, type: String?): Flow<PagingData<RecipeSummaryDto>> {
        // Логика получения случайных рецептов из сети будет реализована в PagingSource
        throw NotImplementedError("Will be implemented in PagingSource")
    }
    
    suspend fun getRecipeDetails(recipeId: Int): RecipeDetailsDto? {
        return try {
            val response = networkClient.getRecipeDetails(recipeId)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
