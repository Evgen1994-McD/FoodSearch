package com.example.foodsearch.data.search.dto.summary

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.foodsearch.data.search.network.NetworkClient
import java.io.IOException


class RecipesPagingSource(
    private val client: NetworkClient,
    private val query: String
) : PagingSource<Int, RecipeSummaryDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecipeSummaryDto> {
        val pageNumber = params.key ?: 1
        val pageSize = params.loadSize
        
        android.util.Log.d("RecipesPagingSource", "Loading page $pageNumber with size $pageSize for query: '$query'")

        return try {
            android.util.Log.d("RecipesPagingSource", "Making API request...")
            val response = client.doRequest(query, pageNumber, pageSize)
            android.util.Log.d("RecipesPagingSource", "API response code: ${response.resultCode}")

            if (response.resultCode == 200) {
                val body = response as RecipeSummryResponse
                val recipes = body.results
                android.util.Log.d("RecipesPagingSource", "Got ${recipes.size} recipes from API")

                LoadResult.Page(
                    data = recipes,
                    prevKey = if (pageNumber > 1) pageNumber - 1 else null,
                    nextKey = if (recipes.isNotEmpty()) pageNumber + 1 else null
                )
            } else {
                android.util.Log.e("RecipesPagingSource", "API error: ${response.resultCode}")
                LoadResult.Error(Exception("Network error: ${response.resultCode}"))
            }
        } catch (e: IOException) {
            android.util.Log.e("RecipesPagingSource", "IOException", e)
            LoadResult.Error(e)
        } catch (e: Exception) {
            android.util.Log.e("RecipesPagingSource", "Exception", e)
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