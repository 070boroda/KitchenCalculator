package com.zelianko.kitchencalculator.data

import kotlinx.coroutines.flow.Flow

class RecipeRepositoryImpl(
    private val dao: RecipeDao
) : RecipeRepository {
    override suspend fun insertRecipe(item: Recipe) {
        dao.insertRecipe(item)
    }

    override suspend fun deleteRecipe(item: Recipe) {
        dao.deleteRecipe(item)
    }

    override fun getAllItems(): Flow<List<Recipe>> {
        return dao.getAllItems()
    }

    override fun getRecipeById(id: Long): Recipe {
        return dao.getRecipeById(id)
    }

    override fun getAllItemsByName(name: String): Flow<List<Recipe>> {
        return dao.getAllItemsByName(name)
    }
}