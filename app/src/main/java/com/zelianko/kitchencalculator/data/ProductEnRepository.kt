package com.zelianko.kitchencalculator.data

import kotlinx.coroutines.flow.Flow

interface ProductEnRepository {

    suspend fun insertProductEn(item: ProductEn)

    suspend fun deleteProductEn(item: ProductEn)

    fun getAllItemsByRecipeId(recipeId: Long): Flow<List<ProductEn>>

    fun getProductEnById(id: Long): ProductEn
}