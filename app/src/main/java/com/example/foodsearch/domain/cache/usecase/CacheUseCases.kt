package com.example.foodsearch.domain.cache.usecase

import androidx.paging.PagingData
import com.example.foodsearch.domain.common.Result
import com.example.foodsearch.domain.common.safeSuspendCall
import com.example.foodsearch.domain.models.RecipeDetails
import com.example.foodsearch.domain.models.RecipeSummary
import com.example.foodsearch.domain.search.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use Case для сохранения рецептов в кеш
 */
class SaveRecipesToCacheUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    
    suspend operator fun invoke(recipes: List<RecipeSummary>): Result<Unit> {
        return safeSuspendCall {
            searchRepository.saveRecipesToCache(recipes)
        }
    }
}

/**
 * Use Case для получения рецептов из кеша
 */
class GetCachedRecipesUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    
    suspend operator fun invoke(query: String?): Flow<PagingData<RecipeSummary>> {
        return searchRepository.getRecipeSummaryFromMemory(query)
    }
}

/**
 * Use Case для получения рецепта из кеша по ID
 */
class GetCachedRecipeByIdUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    
    suspend operator fun invoke(recipeId: Int): Result<RecipeDetails?> {
        return safeSuspendCall {
            searchRepository.getRecipeDetailsFromMemoryById(recipeId)
        }
    }
}

/**
 * Use Case для очистки кеша
 */
class ClearCacheUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    
    suspend operator fun invoke(): Result<Unit> {
        return safeSuspendCall {
            searchRepository.clearCache()
        }
    }
}

/**
 * Use Case для удаления конкретного рецепта из кеша
 */
class DeleteRecipeFromCacheUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    
    suspend operator fun invoke(recipeId: Int): Result<Unit> {
        return safeSuspendCall {
            searchRepository.deleteRecipeById(recipeId)
        }
    }
}

/**
 * Use Case для сохранения рецепта в кеш при просмотре
 */
class SaveRecipeToCacheUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    
    suspend operator fun invoke(recipe: RecipeSummary): Result<Unit> {
        return safeSuspendCall {
            // Конвертируем RecipeSummary в RecipeDetails для сохранения
            val recipeDetails = RecipeDetails(
                id = recipe.id,
                image = recipe.image,
                imageType = null,
                title = recipe.title,
                readyInMinutes = recipe.readyInMinutes,
                servings = recipe.servings,
                sourceUrl = null,
                vegetarian = null,
                vegan = null,
                glutenFree = null,
                dairyFree = null,
                veryHealthy = null,
                cheap = null,
                veryPopular = null,
                sustainable = null,
                lowFodmap = null,
                weightWatcherSmartPoints = null,
                gaps = null,
                preparationMinutes = null,
                cookingMinutes = null,
                aggregateLikes = null,
                healthScore = null,
                creditsText = null,
                license = null,
                sourceName = null,
                pricePerServing = null,
                extendedIngredients = null,
                summary = recipe.summary,
                cuisines = null,
                dishTypes = null,
                diets = null,
                occasions = null,
                instructions = null,
                analyzedInstructions = null,
                spoonacularScore = null,
                spoonacularSourceUrl = null,
                isLike = false
            )
            searchRepository.insertRecipeDetails(recipeDetails)
        }
    }
}
