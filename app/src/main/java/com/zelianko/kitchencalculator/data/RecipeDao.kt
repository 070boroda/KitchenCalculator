package com.zelianko.kitchencalculator.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(item: Recipe): Long

    @Delete
    suspend fun deleteRecipe(item: Recipe)

    @Query("SELECT * FROM recipe")
    fun getAllItems(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipe WHERE recipe.id =:id")
    suspend fun getRecipeById(id: Long): Recipe

    @Query("SELECT * FROM recipe WHERE recipe.name like :name")
    fun getAllItemsByName(name: String): Flow<List<Recipe>>

    @Query("SELECT count(*) FROM recipe")
    fun getCount(): Long
}