package com.example.foodsearch.data.search.impl


import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.foodsearch.data.db.MainDb
import com.example.foodsearch.data.db.converters.RecipeDetailsDbConvertor
import com.example.foodsearch.data.db.converters.RecipeSummaryDbConvertor
import com.example.foodsearch.data.search.dto.card.RecipeCardRequest
import com.example.foodsearch.data.search.dto.card.RecipeCardResponse
import com.example.foodsearch.data.search.dto.details.RecipeDetailsDto
import com.example.foodsearch.data.search.dto.details.RecipeDetailsRequest
import com.example.foodsearch.data.search.dto.random.RandomPagingSource
import com.example.foodsearch.data.search.dto.summary.RecipeSummaryDto
import com.example.foodsearch.data.search.dto.summary.RecipesPagingSource
import com.example.foodsearch.data.search.network.NetworkClient
import com.example.foodsearch.domain.models.RecipeDetails
import com.example.foodsearch.domain.models.RecipeSummary
import com.example.foodsearch.domain.search.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val networkClient: NetworkClient,
    private val mainDb: MainDb,
    private val recipeSummaryDbConvertor: RecipeSummaryDbConvertor,
    private val recipeDetailsDbConvertor: RecipeDetailsDbConvertor
) : SearchRepository {

    override suspend fun searchRecipeFromDbByTitle(title: String): List<RecipeSummary>? {
        val tempList = mainDb.recipeSummaryDao().getRecipesByName(title).map {
            recipeSummaryDbConvertor.map(it)
        }
        return tempList
    }


    override suspend fun insertRecipeDetails(recipe: RecipeDetails) {

        val recipeToSave = recipeDetailsDbConvertor.map(recipe)
        mainDb.recipeDetailsDao().insertRecipe(recipeToSave)
    }


    override suspend fun insertRecipeSummary(recipe: RecipeSummary) {

        val recipeToSave = recipeSummaryDbConvertor.map(recipe)
        mainDb.recipeSummaryDao().insertRecipe(recipeToSave)
    }



    override suspend fun getRecipeSummaryFromMemory(): List<RecipeSummary>? {
        return mainDb.recipeSummaryDao().getAllRecipes().map { entity ->
            recipeSummaryDbConvertor.map(entity)

        }
    }

    override suspend fun getRecipeDetailsFromMemoryById(id: Int): RecipeDetails? {
        val recipe = mainDb.recipeDetailsDao().getRecipeById(id)
        return recipeDetailsDbConvertor.map(recipe[0])


    }




    override fun getRandomRecipes(): Flow<PagingData<RecipeSummary>> {

        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = { RandomPagingSource(networkClient) }
        ).flow.map { pagingData ->
            pagingData.map { dto ->
                mapToDomain(dto)
            }
        }
//            .catch { e ->
//                Log.e("SearchRandomError", "Error searching recipes", e)
//                emit(PagingData.empty())
//
//            }






    }


    override fun searchRecipe(expression: String): Flow<PagingData<RecipeSummary>> {

        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = { RecipesPagingSource(networkClient, expression) }
        ).flow.map { pagingData ->
            pagingData.map { dto ->
                mapToDomain(dto)
            }

        }

//            .catch { e ->
//                Log.e("SearchError", "Error searching recipes", e)
//                emit(PagingData.empty())
//
//            }

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

        try {


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
            val recipeSummaryToSave = RecipeSummary(
                data.id,
                data.image,
                data.title,
                data.readyInMinutes,
                data.servings,
                data.summary
            )
            insertRecipeSummary(recipeSummaryToSave)
            insertRecipeDetails(data)


            return data
        } catch (e: Exception) {

            return getRecipeDetailsFromMemoryById(id)
        }


    }



   private fun mapToDomain(dto: RecipeSummaryDto): RecipeSummary {
        return RecipeSummary(
            dto.id,
            dto.image,
            dto.title,
            dto.readyInMinutes,
            dto.servings,
            dto.summary
        )
    }






}

