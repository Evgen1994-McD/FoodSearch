package com.example.foodsearch.data.search.network

import com.example.foodsearch.data.search.dto.RecipeCardResponse
import com.example.foodsearch.data.search.dto.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Интерфейс API
interface SpoonacularApi {
    @GET("recipes/complexSearch")
    suspend fun getRecipes(
        @Query("query") query: String,
        @Query("addRecipeInformation") addRecipeInformation: Boolean,
        @Query("apiKey") apiKey: String // Передаем как отдельный параметр
    ): RecipeResponse

    @GET("recipes/{recipeId}/card")
    suspend fun getRecipeCard(
        @Path("recipeId") recipeId: Int,
        @Query("apiKey") apiKey: String // Передаем как отдельный параметр
    ): RecipeCardResponse
}



