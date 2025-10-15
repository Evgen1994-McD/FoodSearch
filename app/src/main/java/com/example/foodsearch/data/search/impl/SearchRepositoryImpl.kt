package com.example.foodsearch.data.search.impl

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.foodsearch.data.db.DbRecipePagingSource
import com.example.foodsearch.data.search.datasource.SearchLocalDataSource
import com.example.foodsearch.data.search.datasource.SearchRemoteDataSource
import com.example.foodsearch.data.search.dto.random.RandomPagingSource
import com.example.foodsearch.data.search.dto.summary.RecipesPagingSource
import com.example.foodsearch.data.search.mapper.RecipeDtoMapper
import com.example.foodsearch.domain.models.RecipeDetails
import com.example.foodsearch.domain.models.RecipeSummary
import com.example.foodsearch.domain.search.SearchRepository
import com.example.foodsearch.utils.NetworkUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Упрощенная реализация SearchRepository с разделением ответственности
 */
class SearchRepositoryImpl @Inject constructor(
    private val remoteDataSource: SearchRemoteDataSource,
    private val localDataSource: SearchLocalDataSource,
    private val recipeDtoMapper: RecipeDtoMapper,
    private val networkUtils: NetworkUtils,
) : SearchRepository {

    override suspend fun insertRecipeDetails(recipe: RecipeDetails) {
        localDataSource.insertRecipeDetails(recipe)
    }

    override suspend fun insertRecipeSummary(recipe: RecipeSummary) {
        localDataSource.insertRecipeSummary(recipe)
    }

    override suspend fun getRecipeSummaryFromMemory(query: String?): Flow<PagingData<RecipeSummary>> {
        Log.d("SearchRepositoryImpl", "getRecipeSummaryFromMemory called with query: '$query'")
        
        val recipeCount = localDataSource.getRecipeCount()
        Log.d("SearchRepositoryImpl", "Found $recipeCount recipes in database")
        
        return Pager(
            config = PagingConfig(
                pageSize = 4,
                prefetchDistance = 1
            ),
            pagingSourceFactory = {
                Log.d("SearchRepositoryImpl", "Creating DbRecipePagingSource with query: '$query'")
                DbRecipePagingSource(
                    localDataSource.mainDb,
                    localDataSource.recipeSummaryDbConvertor,
                    query = query
                )
            }
        ).flow
            .catch { e ->
                Log.e("SearchRepositoryImpl", "Error in getRecipeSummaryFromMemory", e)
                emit(PagingData.empty())
            }
    }

    override suspend fun getRecipeDetailsFromMemoryById(id: Int): RecipeDetails? {
        return localDataSource.getRecipeDetailsFromMemoryById(id)
    }

    override fun getRandomRecipes(query: String?): Flow<PagingData<RecipeSummary>> {
        return Pager(
            config = PagingConfig(
                pageSize = 4,
                prefetchDistance = 1
            ),
            pagingSourceFactory = { 
                RandomPagingSource(remoteDataSource.networkClient, query) 
            }
        ).flow.map { pagingData ->
            pagingData.map { dto ->
                recipeDtoMapper.mapToDomain(dto)
            }
        }
        .catch { e ->
            emit(PagingData.empty())
        }
    }

    override fun searchRecipe(expression: String): Flow<PagingData<RecipeSummary>> {
        return Pager(
            config = PagingConfig(
                pageSize = 4,
                prefetchDistance = 1
            ),
            pagingSourceFactory = { 
                RecipesPagingSource(remoteDataSource.networkClient, expression) 
            }
        ).flow.map { pagingData ->
            pagingData.map { dto ->
                recipeDtoMapper.mapToDomain(dto)
            }
        }
        .catch { e ->
            Log.e("Observe_State", "Error searching recipes", e)
            emit(PagingData.empty())
        }
    }

    override suspend fun getFavoriteRecipes(): List<RecipeDetails> {
        return localDataSource.getFavoriteRecipes()
    }

    override suspend fun getAllRecipes(): List<RecipeDetails> {
        return localDataSource.getAllRecipes()
    }

    override suspend fun searchRecipeDetailsInfo(id: Int): RecipeDetails? {
        // Сначала пытаемся получить из кеша
        val cachedRecipe = getRecipeDetailsFromMemoryById(id)
        if (cachedRecipe != null) {
            Log.d("SearchRepositoryImpl", "Recipe $id found in cache")
            
            // Если в кеше нет ингредиентов и инструкций, загружаем из сети
            if ((cachedRecipe.extendedIngredients?.isEmpty() != false) && 
                (cachedRecipe.analyzedInstructions?.isEmpty() != false)) {
                Log.d("SearchRepositoryImpl", "Cached recipe has no ingredients/instructions, loading from network")
                localDataSource.deleteRecipeById(id)
            } else {
                return cachedRecipe
            }
        }
        
        // Если в кеше нет, загружаем из сети
        return try {
            Log.d("SearchRepositoryImpl", "Loading recipe $id from network")
            val dto = remoteDataSource.getRecipeDetails(id)
            if (dto != null) {
                val recipeDetails = recipeDtoMapper.mapToDomain(dto)
                // Сохраняем в кеш для будущего использования
                insertRecipeDetails(recipeDetails)
                Log.d("SearchRepositoryImpl", "Recipe $id saved to cache")
                recipeDetails
            } else {
                Log.w("SearchRepositoryImpl", "Recipe $id not found in API response")
                null
            }
        } catch (e: Exception) {
            Log.e("SearchRepositoryImpl", "Error loading recipe $id from network", e)
            null
        }
    }

    override suspend fun saveRecipesToCache(recipes: List<RecipeSummary>) {
        localDataSource.saveRecipesToCache(recipes)
    }
    
    override suspend fun clearCache() {
        localDataSource.clearCache()
    }
    
    override suspend fun deleteRecipeById(id: Int) {
        localDataSource.deleteRecipeById(id)
    }
    
    override suspend fun getRecipesWithNetworkCheck(query: String, pageNumber: Int, pageSize: Int): Flow<PagingData<RecipeSummary>> {
        Log.d("SearchRepositoryImpl", "getRecipesWithNetworkCheck called with query: '$query', page: $pageNumber, size: $pageSize")
        val isNetworkAvailable = networkUtils.isNetworkAvailable()
        Log.d("SearchRepositoryImpl", "Network available: $isNetworkAvailable")
        
        return if (isNetworkAvailable) {
            Log.d("SearchRepositoryImpl", "Network available, loading from API")
            searchRecipe(query)
        } else {
            Log.d("SearchRepositoryImpl", "No network, loading from cache")
            getRecipeSummaryFromMemory(query)
        }
    }
    
    override suspend fun getRandomRecipesWithNetworkCheck(pageNumber: Int, pageSize: Int, type: String?): Flow<PagingData<RecipeSummary>> {
        return if (networkUtils.isNetworkAvailable()) {
            Log.d("SearchRepositoryImpl", "Network available, loading random recipes from API")
            getRandomRecipes(type)
        } else {
            Log.d("SearchRepositoryImpl", "No network, loading random recipes from cache")
            getRecipeSummaryFromMemory(type)
        }
    }
}
