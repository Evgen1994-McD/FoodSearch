package com.example.foodsearch.domain.search.impl

import android.util.Log
import androidx.paging.PagingData
import com.example.foodsearch.domain.models.RecipeDetails
import com.example.foodsearch.domain.models.RecipeSummary
import com.example.foodsearch.domain.search.SearchInteractor
import com.example.foodsearch.domain.search.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchInteractorImpl @Inject constructor(
    private val searchRepository: SearchRepository
): SearchInteractor {
    companion object{
        private const val exceptionStateString = "Exception"
    }

    override suspend fun getRecipeDetailsById(id: Int):RecipeDetails?{
        return searchRepository.getRecipeDetailsFromMemoryById(id)
    }

    override suspend fun getRecipeFromMemory(query:String?) : Flow<PagingData<RecipeSummary>> {
       return searchRepository.getRecipeSummaryFromMemory(query)
    }

    override suspend fun insertRecipeDetails(recipeDetails: RecipeDetails){
        searchRepository.insertRecipeDetails(recipeDetails)
    }



    override fun searchRecipe(expression: String): Flow<PagingData<RecipeSummary>>{


        return searchRepository.searchRecipe(expression)
    }

   override fun getRandomRecipes(): Flow<PagingData<RecipeSummary>> {


        return searchRepository.getRandomRecipes()
    }






    override suspend fun searchRecipeDetailsInfo(id: Int): Pair<RecipeDetails?, String?> {


        val result = searchRepository.searchRecipeDetailsInfo(id)

         if (result != null) {
             return Pair(result, null)
        } else {
             return   Pair(null, exceptionStateString)
        }


    }

    }
