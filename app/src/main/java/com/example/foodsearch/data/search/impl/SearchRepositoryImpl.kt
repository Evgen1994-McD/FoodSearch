package com.example.foodsearch.data.search.impl

import android.util.Log
import com.example.foodsearch.data.search.dto.RecipeResponse
import com.example.foodsearch.data.search.dto.RecipeSearchRequest
import com.example.foodsearch.data.search.network.NetworkClient
import com.example.foodsearch.domain.models.Recipe
import com.example.foodsearch.domain.search.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val networkClient: NetworkClient
):SearchRepository
{
    override fun searchRecipe(expression: String):Flow<List<Recipe>?> = flow {

        val response = networkClient.doRequest(RecipeSearchRequest(expression))
        when (response.resultCode) {
            200 -> {
                with(response as RecipeResponse) {
                    val data = results.map{ it ->
                        Recipe(
                            it.id,
                            it.title,
                            it.image,
                            it.servings,
                            it.readyInMinutes,
                            it.summary


                        )
                    }
                    emit(data)



                }
            }
            400 -> {
                emit(emptyList())
            }
            else -> emit(null)
            /*
            эмичу эмпти лист чтобы отработать ошибку отсутствия интернета
             */
        }
    }



}