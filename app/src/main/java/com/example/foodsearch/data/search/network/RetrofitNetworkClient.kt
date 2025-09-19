package com.example.foodsearch.data.search.network

import com.example.foodsearch.data.search.dto.RecipeSearchRequest
import com.example.foodsearch.data.search.dto.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val spoonacularApi: SpoonacularApi) : NetworkClient {
private val apiKey = "91225f5085c54860b6c9d5d0298b460b"

    override suspend fun doRequest(dto: Any): Response {
        if (dto !is RecipeSearchRequest) {
            return Response().apply { resultCode = 400 }
        }

        return withContext(Dispatchers.IO) {
            try {


                val response = spoonacularApi.getRecipes(dto.expression, apiKey = apiKey)
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }
}