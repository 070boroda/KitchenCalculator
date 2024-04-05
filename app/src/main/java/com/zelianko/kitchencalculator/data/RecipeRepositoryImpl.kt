package com.zelianko.kitchencalculator.data

import kotlinx.coroutines.flow.Flow

class RecipeRepositoryImpl(
    private val dao: RecipeDao
) : RecipeRepository {
    override suspend fun insertRecipe(item: Recipe): Long {
        return dao.insertRecipe(item)
    }

    override suspend fun deleteRecipe(item: Recipe) {
        dao.deleteRecipe(item)
    }

    override fun getAllItems(): Flow<List<Recipe>> {
        return dao.getAllItems()
    }

    override suspend fun getRecipeById(id: Long): Recipe {
        return dao.getRecipeById(id)
    }

    override fun getAllItemsByName(name: String): Flow<List<Recipe>> {
        return dao.getAllItemsByName(name)
    }

    override fun getCount(): Long {
        return dao.getCount()
    }
}