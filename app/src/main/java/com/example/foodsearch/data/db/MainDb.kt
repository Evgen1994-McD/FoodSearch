package com.example.foodsearch.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foodsearch.data.db.dao.RecipeDetailsDao
import com.example.foodsearch.data.db.dao.RecipeSummaryDao
import com.example.foodsearch.data.db.entity.RecipeDetailsEntity
import com.example.foodsearch.data.db.entity.RecipeSummaryEntity

@Database(version = 1, entities = [RecipeSummaryEntity::class, RecipeDetailsEntity::class])
abstract class MainDb : RoomDatabase() {
    abstract fun recipeSummaryDao(): RecipeSummaryDao
    abstract fun recipeDetailsDao(): RecipeDetailsDao
}