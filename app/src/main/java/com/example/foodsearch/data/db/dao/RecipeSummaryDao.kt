package com.example.foodsearch.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodsearch.data.db.entity.RecipeSummaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeSummaryDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipeSummaryEntity: RecipeSummaryEntity)

    @Query("DELETE FROM recipe_summary_table WHERE id =:id")
    suspend fun deleteRecipeById(id: Int)


@Query("SELECT * FROM recipe_summary_table ORDER BY id ASC LIMIT :limit OFFSET :offset")
suspend fun getAllRecipes(offset: Int, limit: Int): List<RecipeSummaryEntity>

    @Query("SELECT * FROM recipe_summary_table WHERE title LIKE '%' || :title || '%' ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getRecipesByName(title: String, offset: Int, limit: Int): List<RecipeSummaryEntity>


   @Query("SELECT * FROM recipe_summary_table WHERE id=:id")
    suspend fun getRecipeById(id: Int): List<RecipeSummaryEntity>

}

