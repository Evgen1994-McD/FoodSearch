package com.example.foodsearch.data.db.entity.extendEntity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "step_table")
data class StepEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val number: Int,
    val step: String,
    val ingredients: String, // Хранение списка в виде JSON
    val equipment: String // Хранение списка в виде JSON
)