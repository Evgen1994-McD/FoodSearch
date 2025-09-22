package com.example.foodsearch.data.search.network

import com.example.foodsearch.data.search.dto.Response
import com.example.foodsearch.data.search.dto.details.RecipeDetailsDto

interface NetworkClient {
    suspend fun doRequest(
        dto: String,
        pageNumber:Int, pageSize:Int): Response
    suspend fun doRecipeCardRequest(dto: Any): Response
    suspend fun doRecipeDetailsInfoRequest(dto: Any): RecipeDetailsDto?
    suspend fun doRandomRecipe( pageNumber:Int, pageSize:Int): Response
}