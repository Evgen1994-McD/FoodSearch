package com.example.foodsearch.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodsearch.data.db.entity.RecipeDetailsEntity

@Dao
interface RecipeDetailsDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeDetailsEntity)

    @Query("DELETE FROM recipe_details_table WHERE id =:id")
    suspend fun deleteRecipeById(id: Int)

    @Query("SELECT * FROM recipe_details_table")
    suspend fun getAllRecipes(): List<RecipeDetailsEntity>


    @Query("SELECT * FROM recipe_details_table WHERE id=:id")
    suspend fun getRecipeById(id: Int): List<RecipeDetailsEntity>

}