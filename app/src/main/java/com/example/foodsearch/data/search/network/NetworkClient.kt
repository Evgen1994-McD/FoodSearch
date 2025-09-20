package com.example.foodsearch.data.search.network

import com.example.foodsearch.data.search.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
    suspend fun doRecipeCardRequest(dto: Any): Response
    suspend fun doRecipeDetailsInfoRequest(dto: Any): Response
}