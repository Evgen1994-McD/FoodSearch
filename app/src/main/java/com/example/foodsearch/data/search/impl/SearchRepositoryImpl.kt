package com.example.foodsearch.data.search.impl

import com.example.foodsearch.data.search.network.NetworkClient
import com.example.foodsearch.domain.search.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val networkClient: NetworkClient
):SearchRepository
{
    override fun searchRecipe(expression: String):Flow<List<Recipe>?> = flow {
        val response = networkClient.doRequest(RecipeSearchRequest(expression))
        when (response.resultCode) {
            200 -> {
                with(response as Response) {
                    val data = results.map{ it ->
                        Recipe(
                            it.id,
                            it.title,
                            it.image,

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