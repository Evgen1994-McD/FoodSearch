package com.example.foodsearch.presentation.search.adapter

import com.example.foodsearch.domain.models.RecipeSummary

interface OnRecipeClickListener {
    fun onRecipeClicker(recipeSummary: RecipeSummary)
}