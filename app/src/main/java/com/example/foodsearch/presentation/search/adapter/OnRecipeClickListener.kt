package com.example.foodsearch.presentation.search.adapter

import com.example.foodsearch.domain.models.Recipe

interface OnRecipeClickListener {
    fun onRecipeClicker(recipe: Recipe)
}