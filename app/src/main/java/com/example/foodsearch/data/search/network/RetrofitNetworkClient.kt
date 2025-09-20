package com.example.foodsearch.data.search.network

import com.example.foodsearch.data.search.dto.card.RecipeCardRequest
import com.example.foodsearch.data.search.dto.summary.RecipeSummarySearchRequest
import com.example.foodsearch.data.search.dto.Response
import com.example.foodsearch.data.search.dto.details.RecipeDetailsRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val spoonacularApi: SpoonacularApi) : NetworkClient {
private val apiKey2 = "91225f5085c54860b6c9d5d0298b460b"
private val apiKey = "9c69449bd45d4cb0abc8dedbcff5867c"


    override suspend fun doRequest(dto: Any): Response {
        if (dto !is RecipeSummarySearchRequest) {
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

    override suspend fun doRecipeDetailsInfoRequest(dto: Any): Response {
        if (dto !is RecipeDetailsRequest) {
            return Response().apply { resultCode = 400 }
        }

        return withContext(Dispatchers.IO) {
            try {


                val response = spoonacularApi.getRecipeInfo(dto.id, apiKey2)
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }


}