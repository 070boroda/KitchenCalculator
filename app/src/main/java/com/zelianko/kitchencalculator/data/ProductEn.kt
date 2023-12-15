package com.zelianko.kitchencalculator.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_en")
data class ProductEn(
    @PrimaryKey(autoGenerate = true)
    val id:Long? = null,
    var name: String,
    var mass: Double,
    var measureWeight: String,
    val recipeId:Long,
)
