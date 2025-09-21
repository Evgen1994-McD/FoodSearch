package com.example.foodsearch.data.db.entity.extendEntity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "us_measures_table")
data class UsMeasuresEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val amount: Double,
    val unitShort: String,
    val unitLong: String
)