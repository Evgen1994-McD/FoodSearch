package com.example.foodsearch.data.search.dto.random

import com.example.foodsearch.data.search.dto.Response
import com.example.foodsearch.data.search.dto.summary.RecipeSummaryDto

class RecipeRandomResponse(
    val number: Int,
    val recipes: List<RecipeSummaryDto>
) : Response()