package com.example.foodsearch.data.search.network

import com.example.foodsearch.data.search.dto.card.RecipeCardRequest
import com.example.foodsearch.data.search.dto.Response
import com.example.foodsearch.data.search.dto.details.RecipeDetailsDto
import com.example.foodsearch.data.search.dto.details.RecipeDetailsRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val spoonacularApi: SpoonacularApi) : NetworkClient {
    private val apiKey2 = "91225f5085c54860b6c9d5d0298b460b"
    private val apiKey3 = "9c69449bd45d4cb0abc8dedbcff5867c"
    private val apiKey = "132e04b6aa4d4bddb00fc04b0fe73967"
    private val apiKey4 = "fc9779e7ffdb4a0394b69e0af48c6392"


    override suspend fun doRequest(dto: String, pageNumber: Int, pageSize: Int): Response {
        return withContext(Dispatchers.IO) {
            try {
                val response = spoonacularApi.getRecipes(dto, true, pageNumber, pageSize, apiKey = apiKey2)
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }

    override suspend fun doRecipeCardRequest(dto: Any): Response {
        if (dto !is RecipeCardRequest) {
            return Response().apply { resultCode = 400 }
        }

        return withContext(Dispatchers.IO) {
            try {


                val response = spoonacularApi.getRecipeCard(dto.id, apiKey2)
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }

    override suspend fun doRecipeDetailsInfoRequest(dto: Any): RecipeDetailsDto? {
        if (dto !is RecipeDetailsRequest) {
            return null
        } else {
            return spoonacularApi.getRecipeInfo(dto.id, apiKey2)
        }


    }

    override suspend fun doRandomRecipe(): Response {
        return withContext(Dispatchers.IO) {
            try {


                val response = spoonacularApi.getRandomRecipes(apiKey = apiKey2, number = 5)
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }

}