package com.example.foodsearch.data.search.dto.summary

import com.example.foodsearch.data.search.dto.Response

class RecipeSummryResponse(
    val number: Int,
    val results: List<RecipeSummaryDto>
) : Response()