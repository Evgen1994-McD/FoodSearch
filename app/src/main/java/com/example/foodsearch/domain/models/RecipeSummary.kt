package com.example.foodsearch.domain.models

data class RecipeSummary(
    val id: Int?, // Уникальный идентификатор рецепта
    val image: String?, // URL изображения рецепта
    val title: String?, // Название рецепта
    val readyInMinutes: Int?, // Время приготовления в минутах
    val servings: Int?, // Количество порций
    val summary: String?,
    val isLike:Boolean) // Краткое описание рецепта)

