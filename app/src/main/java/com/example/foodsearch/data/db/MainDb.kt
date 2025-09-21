package com.example.foodsearch.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodsearch.data.db.converters.ExtendConverters
import com.example.foodsearch.data.db.dao.RecipeDetailsDao
import com.example.foodsearch.data.db.dao.RecipeSummaryDao
import com.example.foodsearch.data.db.entity.RecipeDetailsEntity
import com.example.foodsearch.data.db.entity.RecipeSummaryEntity


@Database(
    entities = [RecipeSummaryEntity::class, RecipeDetailsEntity::class],
    version = 1
)
@TypeConverters(ExtendConverters::class)
abstract class MainDb : RoomDatabase() {
    abstract fun recipeDetailsDao(): RecipeDetailsDao
    abstract fun recipeSummaryDao(): RecipeSummaryDao

}

