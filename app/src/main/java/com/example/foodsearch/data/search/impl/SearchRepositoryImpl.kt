package com.example.foodsearch.data.search.impl

import android.util.Log
import com.example.foodsearch.data.search.dto.card.RecipeCardRequest
import com.example.foodsearch.data.search.dto.card.RecipeCardResponse
import com.example.foodsearch.data.search.dto.details.RecipeDetailsDto
import com.example.foodsearch.data.search.dto.details.RecipeDetailsRequest
import com.example.foodsearch.data.search.dto.summary.RecipeSummryResponse
import com.example.foodsearch.data.search.dto.summary.RecipeSummarySearchRequest
import com.example.foodsearch.data.search.network.NetworkClient
import com.example.foodsearch.domain.models.RecipeDetails
import com.example.foodsearch.domain.models.RecipeSummary
import com.example.foodsearch.domain.search.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val networkClient: NetworkClient
):SearchRepository {
    override fun searchRecipe(expression: String): Flow<List<RecipeSummary>?> = flow {

        val response = networkClient.doRequest(RecipeSummarySearchRequest(expression))
        when (response.resultCode) {
            200 -> {
                with(response as RecipeSummryResponse) {
                    val data = results.map { it ->
                        RecipeSummary(
                            it.id,
                            it.image,
                            it.title,
                            it.readyInMinutes,
                            it.servings,
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

    override suspend fun searchRecipeCard(id: Int): String? {
        val response = networkClient.doRecipeCardRequest(RecipeCardRequest(id))
        when (response.resultCode) {
            200 -> {
                with(response as RecipeCardResponse) {
                    val uri = url
                    return uri
                }
            }

            else -> return null
        }

    }

    override suspend fun searchRecipeDetailsInfo(id: Int): RecipeDetails? {
        val response = networkClient.doRecipeDetailsInfoRequest(RecipeDetailsRequest(id))

                  val recipeDetailsDto = response as RecipeDetailsDto

                    val data =
                        RecipeDetails(
                            id = recipeDetailsDto.id,
                            image = recipeDetailsDto.image,
                            imageType = recipeDetailsDto.imageType,
                            title = recipeDetailsDto.title,
                            readyInMinutes = recipeDetailsDto.readyInMinutes,
                            servings = recipeDetailsDto.servings,
                            sourceUrl = recipeDetailsDto.sourceUrl,
                            vegetarian = recipeDetailsDto.vegetarian,
                            vegan = recipeDetailsDto.vegan,
                            glutenFree = recipeDetailsDto.glutenFree,
                            dairyFree = recipeDetailsDto.dairyFree,
                            veryHealthy = recipeDetailsDto.veryHealthy,
                            cheap = recipeDetailsDto.cheap,
                            veryPopular = recipeDetailsDto.veryPopular,
                            sustainable = recipeDetailsDto.sustainable,
                            lowFodmap = recipeDetailsDto.lowFodmap,
                            weightWatcherSmartPoints = recipeDetailsDto.weightWatcherSmartPoints,
                            gaps = recipeDetailsDto.gaps,
                            preparationMinutes = recipeDetailsDto.preparationMinutes,
                            cookingMinutes = recipeDetailsDto.cookingMinutes,
                            aggregateLikes = recipeDetailsDto.aggregateLikes,
                            healthScore = recipeDetailsDto.healthScore,
                            creditsText = recipeDetailsDto.creditsText,
                            license = recipeDetailsDto.license,
                            sourceName = recipeDetailsDto.sourceName,
                            pricePerServing = recipeDetailsDto.pricePerServing,
                            extendedIngredients = recipeDetailsDto.extendedIngredients,
                            summary = recipeDetailsDto.summary,
                            cuisines = recipeDetailsDto.cuisines,
                            dishTypes = recipeDetailsDto.dishTypes,
                            diets = recipeDetailsDto.diets,
                            occasions = recipeDetailsDto.occasions,
                            instructions = recipeDetailsDto.instructions,
                            analyzedInstructions = recipeDetailsDto.analyzedInstructions,
                            spoonacularScore = recipeDetailsDto.spoonacularScore,
                            spoonacularSourceUrl = recipeDetailsDto.spoonacularSourceUrl
                        )

                    return data


//                }
            }




}

