package com.example.foodsearch.data.db.entity.extendEntity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dish_type_table")
data class DishTypeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
)