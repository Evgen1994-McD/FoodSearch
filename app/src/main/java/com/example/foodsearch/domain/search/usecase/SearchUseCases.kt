package com.example.foodsearch.domain.search.usecase

import androidx.paging.PagingData
import com.example.foodsearch.domain.common.Result
import com.example.foodsearch.domain.common.safeSuspendCall
import com.example.foodsearch.domain.models.RecipeDetails
import com.example.foodsearch.domain.models.RecipeSummary
import com.example.foodsearch.domain.search.SearchRepository
import com.example.foodsearch.utils.NetworkUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use Case для поиска рецептов по запросу
 */
class SearchRecipesUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
    private val networkUtils: NetworkUtils
) {
    
    suspend operator fun invoke(query: String): Flow<PagingData<RecipeSummary>> {
        return if (networkUtils.isNetworkAvailable()) {
            searchRepository.getRecipesWithNetworkCheck(query, 1, 4)
        } else {
            searchRepository.getRecipeSummaryFromMemory(query)
        }
    }
}

/**
 * Use Case для получения случайных рецептов
 */
class GetRandomRecipesUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
    private val networkUtils: NetworkUtils
) {
    
    suspend operator fun invoke(query: String?): Flow<PagingData<RecipeSummary>> {
        return if (networkUtils.isNetworkAvailable()) {
            searchRepository.getRandomRecipesWithNetworkCheck(1, 4, query)
        } else {
            searchRepository.getRecipeSummaryFromMemory(query)
        }
    }
}

/**
 * Use Case для получения детальной информации о рецепте
 */
class GetRecipeDetailsUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    
    suspend operator fun invoke(recipeId: Int): Result<RecipeDetails> {
        return safeSuspendCall {
            searchRepository.searchRecipeDetailsInfo(recipeId)
        }.map { recipe ->
            recipe ?: throw Exception("Recipe not found")
        }
    }
}

/**
 * Use Case для добавления рецепта в избранное
 */
class SaveRecipeToFavoritesUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    
    suspend operator fun invoke(recipe: RecipeDetails): Result<Unit> {
        return safeSuspendCall {
            val favoriteRecipe = recipe.copy(isLike = true)
            searchRepository.insertRecipeDetails(favoriteRecipe)
        }
    }
}

/**
 * Use Case для удаления рецепта из избранного
 */
class RemoveRecipeFromFavoritesUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    
    suspend operator fun invoke(recipe: RecipeDetails): Result<Unit> {
        return safeSuspendCall {
            val unfavoriteRecipe = recipe.copy(isLike = false)
            searchRepository.insertRecipeDetails(unfavoriteRecipe)
        }
    }
}

/**
 * Use Case для получения списка избранных рецептов
 */
class GetFavoriteRecipesUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    
    suspend operator fun invoke(): Result<List<RecipeDetails>> {
        return safeSuspendCall {
            searchRepository.getFavoriteRecipes()
        }
    }
}

/**
 * Use Case для получения всех сохраненных рецептов
 */
class GetAllRecipesUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    
    suspend operator fun invoke(): Result<List<RecipeDetails>> {
        return safeSuspendCall {
            searchRepository.getAllRecipes()
        }
    }
}
