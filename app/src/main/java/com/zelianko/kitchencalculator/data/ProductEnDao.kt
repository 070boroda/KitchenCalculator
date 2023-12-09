package com.zelianko.kitchencalculator.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductEnDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductEn(item: ProductEn)

    @Delete
    suspend fun deleteProductEn(item: ProductEn)

    @Query("DELETE FROM product_en WHERE recipeId = :recipeId")
    suspend fun deleteProductEnByRecipeId(recipeId: Long)

    @Query("SELECT * FROM product_en WHERE recipeId = :recipeId")
    fun getAllItemsByRecipeId(recipeId: Long): Flow<List<ProductEn>>

    @Query("SELECT * FROM product_en WHERE product_en.id = :id")
    fun getProductEnById(id: Long): ProductEn
}