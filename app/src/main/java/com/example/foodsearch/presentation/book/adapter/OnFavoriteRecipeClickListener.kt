package com.example.foodsearch.presentation.book.adapter

import com.example.foodsearch.domain.models.RecipeDetails
import com.example.foodsearch.domain.models.RecipeSummary

interface OnFavoriteRecipeClickListener {
    fun onRecipeClicker(recipeDetails: RecipeDetails)
}