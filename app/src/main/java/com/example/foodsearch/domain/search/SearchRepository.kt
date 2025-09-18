package com.example.foodsearch.domain.search

import com.example.foodsearch.domain.models.Recipe
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

     fun searchRecipe(expression: String):Flow<List<Recipe>?>
}