package com.example.foodsearch.data.db.entity.extendEntity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "occasion_table")
data class OccasionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
)