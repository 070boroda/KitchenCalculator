package com.zelianko.kitchencalculator.data

import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun insertRecipe(item: Recipe): Long

    suspend fun deleteRecipe(item: Recipe)

    fun getAllItems(): Flow<List<Recipe>>

    suspend fun getRecipeById(id: Long): Recipe

    fun getAllItemsByName(name: String): Flow<List<Recipe>>
}