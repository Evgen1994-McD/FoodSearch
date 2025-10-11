package com.example.foodsearch.data.search.network

import com.example.foodsearch.data.search.dto.details.RecipeDetailsDto
import com.example.foodsearch.data.search.dto.random.RecipeRandomResponse
import com.example.foodsearch.data.search.dto.summary.RecipeSummryResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Интерфейс API
interface SpoonacularApi {
    @GET("recipes/complexSearch")
    suspend fun getRecipes(
        @Query("query") query: String,
        @Query("addRecipeInformation") addRecipeInformation: Boolean,
        @Query("pageNumber") pageNumber: Int, // Номер страницы
        @Query("pageSize") pageSize: Int,     // Размер страницы
        @Query("apiKey") apiKey: String       // Передаем как отдельный параметр
    ): RecipeSummryResponse


    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("apiKey") apiKey: String, // Передаем как отдельный параметр
        @Query("pageNumber") pageNumber: Int, // Номер страницы
        @Query("pageSize") pageSize: Int,     // Размер страницы
        @Query("type") type: String?     // Размер страницы
    ): RecipeRandomResponse


    @GET("recipes/{recipeId}/information")
    suspend fun getRecipeInfo(
        @Path("recipeId") recipeId: Int,
        @Query("apiKey") apiKey: String // Передаем как отдельный параметр
    ): RecipeDetailsDto

    @GET("recipes/{recipeId}/information")
    suspend fun getRecipeDetails(
        @Path("recipeId") recipeId: Int,
        @Query("apiKey") apiKey: String // Передаем как отдельный параметр
    ): retrofit2.Response<RecipeDetailsDto>

//
}



