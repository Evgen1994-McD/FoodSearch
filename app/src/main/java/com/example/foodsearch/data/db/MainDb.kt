package com.example.foodsearch.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodsearch.data.db.converters.ExtendConverters
import com.example.foodsearch.data.db.dao.RecipeDetailsDao
import com.example.foodsearch.data.db.dao.RecipeSummaryDao
import com.example.foodsearch.data.db.entity.RecipeDetailsEntity
import com.example.foodsearch.data.db.entity.RecipeSummaryEntity
import com.example.foodsearch.data.db.entity.extendEntity.AnalyzedInstructionEntity
import com.example.foodsearch.data.db.entity.extendEntity.EquipmentEntity
import com.example.foodsearch.data.db.entity.extendEntity.IngredientEntity
import com.example.foodsearch.data.db.entity.extendEntity.MetricMeasuresEntity
import com.example.foodsearch.data.db.entity.extendEntity.StepEntity
import com.example.foodsearch.data.db.entity.extendEntity.UsMeasuresEntity

@Database(entities = [RecipeSummaryEntity::class,RecipeDetailsEntity::class, IngredientEntity::class, UsMeasuresEntity::class, MetricMeasuresEntity::class, AnalyzedInstructionEntity::class, StepEntity::class, EquipmentEntity::class], version = 1)
@TypeConverters(ExtendConverters::class)
abstract class MainDb : RoomDatabase() {
    abstract fun recipeDetailsDao(): RecipeDetailsDao
    abstract fun recipeSummaryDao(): RecipeSummaryDao

}

