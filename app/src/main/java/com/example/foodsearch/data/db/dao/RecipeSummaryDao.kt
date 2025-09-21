package com.example.foodsearch.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodsearch.data.db.entity.RecipeSummaryEntity

@Dao
interface RecipeSummaryDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipeSummaryEntity: RecipeSummaryEntity)

    @Query("DELETE FROM recipe_summary_table WHERE id =:id")
    suspend fun deleteRecipeById(id: Int)

    @Query("SELECT * FROM recipe_summary_table")
    suspend fun getAllRecipes(): List<RecipeSummaryEntity>


   @Query("SELECT * FROM recipe_summary_table WHERE id=:id")
    suspend fun getRecipeById(id: Int): List<RecipeSummaryEntity>

}