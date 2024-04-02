package com.zelianko.kitchencalculator.data

interface ProductEnRepository {

    suspend fun insertProductEn(item: ProductEn)

    suspend fun deleteProductEn(item: ProductEn)

    suspend fun deleteProductEnByRecipeId(recipeId: Long)

    suspend fun getAllItemsByRecipeId(recipeId: Long): MutableList<ProductEn>

    suspend fun getProductEnById(id: Long): ProductEn
}