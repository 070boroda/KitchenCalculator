package com.zelianko.kitchencalculator.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDescriptionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: RecipeDescription)

    @Query("SELECT * FROM recipe_description WHERE recipeId = :recipeId LIMIT 1")
    suspend fun getByRecipeId(recipeId: Long): RecipeDescription?

    @Query("DELETE FROM recipe_description WHERE recipeId = :recipeId")
    suspend fun deleteByRecipeId(recipeId: Long)
}
