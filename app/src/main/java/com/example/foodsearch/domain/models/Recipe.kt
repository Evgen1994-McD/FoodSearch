package com.example.foodsearch.domain.models

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val servings:Int,
    val readyInMinutes:Int,
    val summary: String?



    )
