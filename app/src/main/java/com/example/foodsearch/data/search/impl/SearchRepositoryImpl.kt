package com.example.foodsearch.data.search.impl


import com.example.foodsearch.data.db.MainDb
import com.example.foodsearch.data.db.converters.RecipeDetailsDbConvertor
import com.example.foodsearch.data.db.converters.RecipeSummaryDbConvertor
import com.example.foodsearch.data.search.dto.card.RecipeCardRequest
import com.example.foodsearch.data.search.dto.card.RecipeCardResponse
import com.example.foodsearch.data.search.dto.details.RecipeDetailsDto
import com.example.foodsearch.data.search.dto.details.RecipeDetailsRequest
import com.example.foodsearch.data.search.dto.random.RecipeRandomResponse
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
    private val networkClient: NetworkClient,
    private val mainDb: MainDb,
    private val recipeSummaryDbConvertor: RecipeSummaryDbConvertor,
    private val recipeDetailsDbConvertor: RecipeDetailsDbConvertor
):SearchRepository {

    private suspend fun insertRecipeDetails(recipe: RecipeDetails) {

        val recipeToSave = recipeDetailsDbConvertor.map(recipe)
        mainDb.recipeDetailsDao().insertRecipe(recipeToSave)
    }


    private suspend fun insertRecipeSummary(recipe: RecipeSummary) {

       val recipeToSave = recipeSummaryDbConvertor.map(recipe)
        mainDb.recipeSummaryDao().insertRecipe(recipeToSave)
    }

    private suspend fun getRecipeSummaryFromMemory(): List<RecipeSummary>? {
        return  mainDb.recipeSummaryDao().getAllRecipes().map{ entity->
            recipeSummaryDbConvertor.map(entity)
        }
    }


    private suspend fun getRecipeDetailsFromMemoryById(id: Int): RecipeDetails?{
        val recipe = mainDb.recipeDetailsDao().getRecipeById(id)
        return recipeDetailsDbConvertor.map( recipe[0])


    }



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

                emit(getRecipeSummaryFromMemory())
            }

            else -> emit(getRecipeSummaryFromMemory())
            /*
            эмичу эмпти лист чтобы отработать ошибку отсутствия интернета
             */
        }
    }

    override fun getRandomRecipes(): Flow<List<RecipeSummary>?> = flow {

        val response = networkClient.doRandomRecipe()
        when (response.resultCode) {
            200 -> {
                with(response as RecipeRandomResponse) {
                    val data = recipes.map { it ->
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

                emit(getRecipeSummaryFromMemory())

            }

            else ->

                emit(getRecipeSummaryFromMemory())

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
} catch (e:Exception){

    return getRecipeDetailsFromMemoryById(id)
}




    }





}

