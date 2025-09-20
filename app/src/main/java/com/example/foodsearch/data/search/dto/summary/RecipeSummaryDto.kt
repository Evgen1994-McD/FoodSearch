package com.example.foodsearch.data.search.dto.summary

data class RecipeSummaryDto(

    val id: Int, // Уникальный идентификатор рецепта
    val image: String, // URL изображения рецепта
    val title: String, // Название рецепта
    val readyInMinutes: Int, // Время приготовления в минутах
    val servings: Int, // Количество порций
    val summary: String, // Краткое описание рецепта
) {
}