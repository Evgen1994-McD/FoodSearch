package com.example.foodsearch.data.search.dto

class RecipeResponse(
    val number: Int,
val results: List<RecipeDto>
) : Response()
