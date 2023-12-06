package com.zelianko.kitchencalculator.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_en")
data class ProductEn(
    @PrimaryKey(autoGenerate = true)
    val id:Long? = null,
    val name: String,
    val mass: Double,
    val recipeId:Long
)
