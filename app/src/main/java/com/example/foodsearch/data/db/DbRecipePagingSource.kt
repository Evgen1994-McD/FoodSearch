package com.example.foodsearch.data.db

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.foodsearch.data.db.converters.RecipeSummaryDbConvertor
import com.example.foodsearch.domain.models.RecipeSummary
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import java.io.IOException

class DbRecipePagingSource(
    private val db: MainDb,
    private val converter: RecipeSummaryDbConvertor,
    private val query: String?
) : PagingSource<Int, RecipeSummary>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecipeSummary> {
        val pageIndex = params.key ?: 0 // Начальная страница равна нулю
        val limit = params.loadSize // Количество элементов на странице

        return try {
            // Определяем смещение для выборки данных
            val offset = pageIndex * limit

            // Выполняем запрос к базе данных
            val result = if (query.isNullOrEmpty()){
                db.recipeSummaryDao().getAllRecipes(offset, limit)
            } else {
                db.recipeSummaryDao().getRecipesByName(query,offset, limit)
            }



            // Преобразовываем данные с помощью конвертера
            val mappedEntities = result.map { entity -> converter.map(entity) }

            // Определяем ключ для следующей страницы
            val nextKey = if (mappedEntities.isEmpty()) null else pageIndex + 1

            LoadResult.Page(
                data = mappedEntities,
                prevKey = null, // Только последовательная пагинация вперед
                nextKey = nextKey
            )
        } catch (ex: IOException) {
            LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RecipeSummary>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}