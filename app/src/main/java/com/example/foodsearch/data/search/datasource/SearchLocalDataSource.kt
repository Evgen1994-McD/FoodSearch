package com.example.foodsearch.data.search.datasource

import androidx.paging.PagingData
import com.example.foodsearch.data.db.MainDb
import com.example.foodsearch.data.db.converters.RecipeDetailsDbConvertor
import com.example.foodsearch.data.db.converters.RecipeSummaryDbConvertor
import com.example.foodsearch.data.db.entity.RecipeDetailsEntity
import com.example.foodsearch.data.db.entity.RecipeSummaryEntity
import com.example.foodsearch.domain.models.RecipeDetails
import com.example.foodsearch.domain.models.RecipeSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Data Source для локальных операций с рецептами
 */
class SearchLocalDataSource @Inject constructor(
    val mainDb: MainDb,
    val recipeSummaryDbConvertor: RecipeSummaryDbConvertor,
    private val recipeDetailsDbConvertor: RecipeDetailsDbConvertor,
) {
    
    suspend fun insertRecipeDetails(recipe: RecipeDetails) {
        val recipeToSave = recipeDetailsDbConvertor.map(recipe)
        mainDb.recipeDetailsDao().insertRecipe(recipeToSave)
    }
    
    suspend fun insertRecipeSummary(recipe: RecipeSummary) {
        val recipeToSave = recipeSummaryDbConvertor.map(recipe)
        mainDb.recipeSummaryDao().insertRecipe(recipeToSave)
    }
    
    suspend fun getRecipeSummaryFromMemory(query: String?): Flow<PagingData<RecipeSummary>> {
        // Логика получения рецептов из БД будет реализована в DbPagingSource
        throw NotImplementedError("Will be implemented in DbPagingSource")
    }
    
    suspend fun getRecipeDetailsFromMemoryById(id: Int): RecipeDetails? {
        val recipe = mainDb.recipeDetailsDao().getRecipeById(id)
        return if (recipe.isNotEmpty()) {
            recipeDetailsDbConvertor.map(recipe[0])
        } else {
            null
        }
    }
    
    suspend fun getFavoriteRecipes(): List<RecipeDetails> {
        return mainDb.recipeDetailsDao().getFavoriteRecipes(true).map {
            recipeDetailsDbConvertor.map(it)
        }
    }
    
    suspend fun getAllRecipes(): List<RecipeDetails> {
        return mainDb.recipeDetailsDao().getAllRecipes().map {
            recipeDetailsDbConvertor.map(it)
        }
    }
    
    suspend fun saveRecipesToCache(recipes: List<RecipeSummary>) {
        recipes.forEach { recipe ->
            insertRecipeSummary(recipe)
        }
    }
    
    suspend fun clearCache() {
        mainDb.recipeDetailsDao().deleteAllRecipes()
    }
    
    suspend fun deleteRecipeById(id: Int) {
        mainDb.recipeDetailsDao().deleteRecipeById(id)
    }
    
    suspend fun getRecipeCount(): Int {
        return mainDb.recipeSummaryDao().getRecipeCount()
    }
}
