package com.example.foodsearch.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_summary_table")
data class RecipeSummaryEntity(
    @PrimaryKey
    val id: Int?, // Уникальный идентификатор рецепта
    val image: String?, // URL изображения рецепта
    val title: String?, // Название рецепта
    val readyInMinutes: Int?, // Время приготовления в минутах
    val servings: Int?, // Количество порций
    val summary: String?,
    val isLike:Boolean=false) // Краткое описание рецепта)