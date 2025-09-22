package com.example.foodsearch.data.search.dto.random

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.foodsearch.data.search.dto.summary.RecipeSummaryDto
import com.example.foodsearch.data.search.network.NetworkClient
import java.io.IOException

class RandomPagingSource(
    private val client: NetworkClient,
private val type: String?
) : PagingSource<Int, RecipeSummaryDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecipeSummaryDto> {
        val pageNumber = params.key ?: 1
        val pageSize = params.loadSize

        return try {
            val response = client.doRandomRecipe(pageNumber, pageSize,type)

            if (response.resultCode == 200) {
                val body = response as RecipeRandomResponse
                val recipes = body.recipes

                LoadResult.Page(
                    data = recipes,
                    prevKey = if (pageNumber > 1) pageNumber - 1 else null,
                    nextKey = if (recipes.isNotEmpty()) pageNumber + 1 else null
                )
            } else {
                LoadResult.Error(Exception("Network error: ${response.resultCode}\""))
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RecipeSummaryDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
