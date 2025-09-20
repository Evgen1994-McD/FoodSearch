package com.example.foodsearch.data.search.network

import com.example.foodsearch.data.search.dto.RecipeCardRequest
import com.example.foodsearch.data.search.dto.RecipeSearchRequest
import com.example.foodsearch.data.search.dto.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val spoonacularApi: SpoonacularApi) : NetworkClient {
private val apiKey = "91225f5085c54860b6c9d5d0298b460b"
private val apiKey2 = "9c69449bd45d4cb0abc8dedbcff5867c"


    override suspend fun doRequest(dto: Any): Response {
        if (dto !is RecipeSearchRequest) {
            return Response().apply { resultCode = 400 }
        }

        return withContext(Dispatchers.IO) {
            try {


                val response = spoonacularApi.getRecipes(dto.expression, true, apiKey = apiKey2)
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


}