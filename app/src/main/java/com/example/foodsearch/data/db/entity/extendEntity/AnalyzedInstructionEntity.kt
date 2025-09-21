package com.example.foodsearch.data.db.entity.extendEntity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "analyzed_instruction_table")
data class AnalyzedInstructionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val steps: String // Хранение списка в виде JSON
)