package com.example.foodsearch.domain.search.impl

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

    override fun searchRecipe(expression: String): Flow<Pair<List<RecipeSummary>?, String?>> {


        return searchRepository.searchRecipe(expression).map { results ->
            if (results != null) {
                Pair(results, null)
            } else {
                Pair(null, exceptionStateString)
            }
        }
    }

    override suspend fun searchRecipeCard(id:Int):String?{
        return searchRepository.searchRecipeCard(id)
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
