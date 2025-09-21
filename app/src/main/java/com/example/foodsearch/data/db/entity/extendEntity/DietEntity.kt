package com.example.foodsearch.data.db.entity.extendEntity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diet_table")
data class DietEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
)