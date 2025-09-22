package com.example.foodsearch.data.search.dto.card

import com.example.foodsearch.data.search.dto.Response

class RecipeCardResponse(
    val url: String,
    val status: String,
    val time: String
) : Response()