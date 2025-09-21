package com.example.foodsearch.data.search.network

import com.example.foodsearch.data.search.dto.Response
import com.example.foodsearch.data.search.dto.details.RecipeDetailsDto
import com.example.foodsearch.data.search.dto.summary.RecipeSummaryDto

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
    suspend fun doRecipeCardRequest(dto: Any): Response
    suspend fun doRecipeDetailsInfoRequest(dto: Any): RecipeDetailsDto?
    suspend fun doRandomRecipe(): Response
}