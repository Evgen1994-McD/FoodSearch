package com.example.foodsearch.data.db.entity.extendEntity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredient_table")
data class IngredientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val aisle: String,
    val image: String,
    val consistency: String,
    val name: String,
    val nameClean: String,
    val original: String,
    val originalName: String,
    val amount: Double,
    val unit: String,
    val meta: String, // Хранение списка в виде JSON
    val measures: String // Хранение объекта в виде JSON
)