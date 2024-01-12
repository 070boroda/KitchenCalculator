package com.zelianko.kitchencalculator.data

import kotlinx.coroutines.flow.Flow

class ProductEnRepositoryImp(
    private val dao: ProductEnDao
) : ProductEnRepository {
    override suspend fun insertProductEn(item: ProductEn) {
        dao.insertProductEn(item)
    }

    override suspend fun deleteProductEn(item: ProductEn) {
        dao.deleteProductEn(item)
    }

    override suspend fun getAllItemsByRecipeId(recipeId: Long): MutableList<ProductEn> {
        return dao.getAllItemsByRecipeId(recipeId)
    }

    override fun getProductEnById(id: Long): ProductEn {
        return dao.getProductEnById(id)
    }

    override suspend fun deleteProductEnByRecipeId(recipeId: Long) {
        dao.deleteProductEnByRecipeId(recipeId)
    }
}