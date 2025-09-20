package com.example.foodsearch.data.search.dto.details

import com.example.foodsearch.data.search.dto.Response

class RecipeDetailsResponse(
    val number: Int,
    val result: RecipeDetailsDto
) : Response()