package com.example.foodsearch.data.search.dto.summary

import android.util.Log
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

        return try {
            Log.d("NETWORK_REQUEST", "Sending request for query: $query, Page: $pageNumber")
            val response = client.doRequest(query,  pageNumber, pageSize)

            if (response.resultCode == 200) {
                val body = response as RecipeSummryResponse
                val recipes = body.results

                LoadResult.Page(
                    data = recipes,
                    prevKey = if (pageNumber > 1) pageNumber - 1 else null,
                    nextKey = if (recipes.isNotEmpty()) pageNumber + 1 else null
                )
            } else {
                LoadResult.Error(Exception("Server returned a non-success status code"))
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