package com.example.foodsearch.data.search.impl


import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.foodsearch.data.db.DbRecipePagingSource
import com.example.foodsearch.data.db.MainDb
import com.example.foodsearch.data.db.converters.RecipeDetailsDbConvertor
import com.example.foodsearch.data.db.converters.RecipeSummaryDbConvertor
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
    private val recipeDetailsDbConvertor: RecipeDetailsDbConvertor,
) : SearchRepository {


    override suspend fun insertRecipeDetails(recipe: RecipeDetails) {

        val recipeToSave = recipeDetailsDbConvertor.map(recipe)
        mainDb.recipeDetailsDao().insertRecipe(recipeToSave)
    }

    override suspend fun insertRecipeSummary(recipe: RecipeSummary) {

        val recipeToSave = recipeSummaryDbConvertor.map(recipe)
        mainDb.recipeSummaryDao().insertRecipe(recipeToSave)
    }

    override suspend fun getRecipeSummaryFromMemory(query: String?): Flow<PagingData<RecipeSummary>> {
        return Pager(

            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = {
                DbRecipePagingSource(
                    mainDb,
                    recipeSummaryDbConvertor,
                    query = query
                )
            }
        ).flow

            .catch { e ->
                emit(PagingData.empty())

            }


    }

    override suspend fun getRecipeDetailsFromMemoryById(id: Int): RecipeDetails? {
        val recipe = mainDb.recipeDetailsDao().getRecipeById(id)
        return recipeDetailsDbConvertor.map(recipe[0])

    }

    override fun getRandomRecipes(query: String?): Flow<PagingData<RecipeSummary>> {

        return Pager(

            config = PagingConfig(pageSize = 1),
            pagingSourceFactory = { RandomPagingSource(networkClient,query) }
        ).flow.map { pagingData ->
            pagingData.map { dto ->
                mapToDomain(dto)
            }

        }
            .catch { e ->
                emit(PagingData.empty())

            }


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

            .catch { e ->
                Log.e("Observe_State", "Error searching recipes", e)
                emit(PagingData.empty())

            }

    }


    override suspend fun getFavoriteRecipes(): List<RecipeDetails> {
        return mainDb.recipeDetailsDao().getFavoriteRecipes(true).map {
            recipeDetailsDbConvertor.map(it)
        }
    }


    override suspend fun getAllRecipes(): List<RecipeDetails> {
        return mainDb.recipeDetailsDao().getAllRecipes().map {
            recipeDetailsDbConvertor.map(it)
        }
    }


    override suspend fun searchRecipeDetailsInfo(id: Int): RecipeDetails? {
        // Сначала пытаемся получить из кеша
        val cachedRecipe = getRecipeDetailsFromMemoryById(id)
        if (cachedRecipe != null) {
            Log.d("SearchRepositoryImpl", "Recipe $id found in cache")
            return cachedRecipe
        }
        
        // Если в кеше нет, загружаем из сети
        return try {
            Log.d("SearchRepositoryImpl", "Loading recipe $id from network")
            val response = networkClient.getRecipeDetails(id)
            if (response.isSuccessful) {
                val dto = response.body()
                if (dto != null) {
                    val recipeDetails = mapToDomain(dto)
                    // Сохраняем в кеш для будущего использования
                    insertRecipeDetails(recipeDetails)
                    Log.d("SearchRepositoryImpl", "Recipe $id saved to cache")
                    recipeDetails
                } else {
                    Log.w("SearchRepositoryImpl", "Recipe $id not found in API response")
                    null
                }
            } else {
                Log.w("SearchRepositoryImpl", "Failed to load recipe $id: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("SearchRepositoryImpl", "Error loading recipe $id from network", e)
            // Возвращаем null, так как нет данных ни в кеше, ни в сети
            null
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

    private fun mapToDomain(dto: RecipeDetailsDto): RecipeDetails {
        Log.d("SearchRepositoryImpl", "Mapping RecipeDetailsDto to domain model")
        Log.d("SearchRepositoryImpl", "Ingredients count: ${dto.extendedIngredients?.size ?: 0}")
        Log.d("SearchRepositoryImpl", "Instructions count: ${dto.analyzedInstructions?.size ?: 0}")
        
        return RecipeDetails(
            id = dto.id,
            image = dto.image,
            imageType = dto.imageType,
            title = dto.title,
            readyInMinutes = dto.readyInMinutes,
            servings = dto.servings,
            sourceUrl = dto.sourceUrl,
            vegetarian = dto.vegetarian,
            vegan = dto.vegan,
            glutenFree = dto.glutenFree,
            dairyFree = dto.dairyFree,
            veryHealthy = dto.veryHealthy,
            cheap = dto.cheap,
            veryPopular = dto.veryPopular,
            sustainable = dto.sustainable,
            lowFodmap = dto.lowFodmap,
            weightWatcherSmartPoints = dto.weightWatcherSmartPoints,
            gaps = dto.gaps,
            preparationMinutes = dto.preparationMinutes,
            cookingMinutes = dto.cookingMinutes,
            aggregateLikes = dto.aggregateLikes,
            healthScore = dto.healthScore,
            creditsText = dto.creditsText,
            license = dto.license,
            sourceName = dto.sourceName,
            pricePerServing = dto.pricePerServing,
            extendedIngredients = dto.extendedIngredients?.map { mapIngredientToDomain(it) },
            summary = dto.summary,
            cuisines = dto.cuisines,
            dishTypes = dto.dishTypes,
            diets = dto.diets,
            occasions = dto.occasions,
            instructions = dto.instructions,
            analyzedInstructions = dto.analyzedInstructions?.map { mapAnalyzedInstructionToDomain(it) },
            spoonacularScore = dto.spoonacularScore,
            spoonacularSourceUrl = dto.spoonacularSourceUrl,
            isLike = false
        )
    }
    
    private fun mapIngredientToDomain(dto: com.example.foodsearch.data.search.dto.details.IngredientDto): com.example.foodsearch.domain.models.OtherModels.Ingredient {
        Log.d("SearchRepositoryImpl", "Mapping ingredient: ${dto.name}, amount: ${dto.amount} ${dto.unit}")
        return com.example.foodsearch.domain.models.OtherModels.Ingredient(
            id = dto.id ?: 0,
            aisle = dto.aisle ?: "",
            image = dto.image ?: "",
            consistency = dto.consistency ?: "",
            name = dto.name ?: "",
            nameClean = dto.nameClean ?: "",
            original = dto.original ?: "",
            originalName = dto.originalName ?: "",
            amount = dto.amount ?: 0.0,
            unit = dto.unit ?: "",
            meta = dto.meta ?: emptyList(),
            measures = mapMeasuresToDomain(dto.measures)
        )
    }
    
    private fun mapMeasuresToDomain(dto: com.example.foodsearch.data.search.dto.details.MeasuresDto?): com.example.foodsearch.domain.models.OtherModels.Measures {
        return com.example.foodsearch.domain.models.OtherModels.Measures(
            us = com.example.foodsearch.domain.models.OtherModels.UsMeasures(
                amount = dto?.us?.amount ?: 0.0,
                unitShort = dto?.us?.unitShort ?: "",
                unitLong = dto?.us?.unitLong ?: ""
            ),
            metric = com.example.foodsearch.domain.models.OtherModels.MetricMeasures(
                amount = dto?.metric?.amount ?: 0.0,
                unitShort = dto?.metric?.unitShort ?: "",
                unitLong = dto?.metric?.unitLong ?: ""
            )
        )
    }
    
    private fun mapAnalyzedInstructionToDomain(dto: com.example.foodsearch.data.search.dto.details.AnalyzedInstructionDto): com.example.foodsearch.domain.models.OtherModels.AnalyzedInstruction {
        Log.d("SearchRepositoryImpl", "Mapping instruction: ${dto.name}, steps count: ${dto.steps?.size ?: 0}")
        return com.example.foodsearch.domain.models.OtherModels.AnalyzedInstruction(
            name = dto.name ?: "",
            steps = dto.steps?.map { mapStepToDomain(it) } ?: emptyList()
        )
    }
    
    private fun mapStepToDomain(dto: com.example.foodsearch.data.search.dto.details.StepDto): com.example.foodsearch.domain.models.OtherModels.Step {
        Log.d("SearchRepositoryImpl", "Mapping step ${dto.number}: ${dto.step?.take(50)}...")
        return com.example.foodsearch.domain.models.OtherModels.Step(
            number = dto.number ?: 0,
            step = dto.step ?: "",
            ingredients = dto.ingredients?.map { mapIngredientToDomain(it) } ?: emptyList(),
            equipment = dto.equipment?.map { mapEquipmentToDomain(it) } ?: emptyList()
        )
    }
    
    private fun mapEquipmentToDomain(dto: com.example.foodsearch.data.search.dto.details.EquipmentDto): com.example.foodsearch.domain.models.OtherModels.Equipment {
        return com.example.foodsearch.domain.models.OtherModels.Equipment(
            id = dto.id ?: 0,
            name = dto.name ?: "",
            localizedName = dto.localizedName ?: "",
            image = dto.image ?: ""
        )
    }
    
    // Метод для массового сохранения рецептов в кеш
    suspend fun saveRecipesToCache(recipes: List<RecipeSummary>) {
        try {
            recipes.forEach { recipe ->
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
                insertRecipeDetails(recipeDetails)
            }
        } catch (e: Exception) {
            Log.e("SearchRepositoryImpl", "Error saving recipes to cache", e)
        }
    }


}

