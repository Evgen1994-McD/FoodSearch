package com.example.foodsearch.data.db.entity.extendEntity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cuisine_table")
data class CuisineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
)