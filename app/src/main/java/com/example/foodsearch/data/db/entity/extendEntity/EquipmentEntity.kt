package com.example.foodsearch.data.db.entity.extendEntity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "equipment_table")
data class EquipmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val localizedName: String,
    val image: String
)