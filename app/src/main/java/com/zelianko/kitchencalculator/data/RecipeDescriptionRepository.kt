package com.zelianko.kitchencalculator.data

interface RecipeDescriptionRepository {
    suspend fun upsert(item: RecipeDescription)
    suspend fun getByRecipeId(recipeId: Long): RecipeDescription?
    suspend fun deleteByRecipeId(recipeId: Long)
}

class RecipeDescriptionRepositoryImpl(
    private val dao: RecipeDescriptionDao,
) : RecipeDescriptionRepository {
    override suspend fun upsert(item: RecipeDescription) = dao.upsert(item)
    override suspend fun getByRecipeId(recipeId: Long): RecipeDescription? = dao.getByRecipeId(recipeId)
    override suspend fun deleteByRecipeId(recipeId: Long) = dao.deleteByRecipeId(recipeId)
}
