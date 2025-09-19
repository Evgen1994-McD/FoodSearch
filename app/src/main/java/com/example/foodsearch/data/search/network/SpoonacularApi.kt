package com.example.foodsearch.data.search.network

import com.example.foodsearch.data.search.dto.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Query

// Интерфейс API
interface SpoonacularApi {
    @GET("recipes/complexSearch")
    suspend fun getRecipes(
        @Query("query") query: String,
        @Query("apiKey") apiKey: String // Передаем как отдельный параметр
//        @Query("maxFat") maxFat: Int?,
//        @Query("number") number: Int?
    ): RecipeResponse
}


