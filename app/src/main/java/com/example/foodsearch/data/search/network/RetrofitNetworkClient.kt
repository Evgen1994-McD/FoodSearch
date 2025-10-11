package com.example.foodsearch.data.search.network

import com.example.foodsearch.data.search.dto.Response
import com.example.foodsearch.data.search.dto.details.RecipeDetailsDto
import com.example.foodsearch.data.search.dto.details.RecipeDetailsRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val spoonacularApi: SpoonacularApi) : NetworkClient {
    private val apiKey45 = "91225f5085c54860b6c9d5d0298b460b"
    private val apiKey11 = "9c69449bd45d4cb0abc8dedbcff5867c"
    private val apiKey4 = "132e04b6aa4d4bddb00fc04b0fe73967"
    private val apiKey43 = "fc9779e7ffdb4a0394b69e0af48c6392"
    private val apiKey22 = "1f90d4229d854fb2a5f83b7c55c7d068"
    private val apiKey3 = "7be5813cd34e4ee381ede45891148d22"
    private val apiKey33 = "c3a4ad3a8035467c80953f24b8f3cc83"
    private val apiKey21 = "3c4e3432dd7e432b826d041e71b923b8"
    private val apiKey223 = "e75cda5befa3464ca0c178e307ae9620"
    private val apiKey0 = "03f1f1d6fced433a92729d7f1a99fdd0"
    private val apiKey25 = "54e75716e30a4ce784b258783f6f4eac"
    private val apiKey = "b14c803d4ea443d3b6acd83d37e71677"

    override suspend fun doRequest(dto: String, pageNumber: Int, pageSize: Int): Response {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    spoonacularApi.getRecipes(dto, true, pageNumber, pageSize, apiKey = apiKey)
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
            return spoonacularApi.getRecipeInfo(dto.id, apiKey)
        }
    }

    override suspend fun doRandomRecipe(pageNumber: Int, pageSize: Int, type: String?): Response {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    spoonacularApi.getRandomRecipes(apiKey = apiKey, pageNumber, pageSize, type)
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }

    override suspend fun getRecipeDetails(id: Int): retrofit2.Response<RecipeDetailsDto> {
        return withContext(Dispatchers.IO) {
            try {
                spoonacularApi.getRecipeDetails(id, apiKey)
            } catch (e: Throwable) {
                retrofit2.Response.error(500, okhttp3.ResponseBody.create(null, e.message ?: "Unknown error"))
            }
        }
    }

}