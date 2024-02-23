package com.zelianko.kitchencalculator.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cookingtime")
data class CookingTime (

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    /**
     * Группы продуктов
     */
    var groupProduct: Int,
    var productName: String,
    /**
     * Время варки
     */
    var boilingTime : Int,
    /**
     * Время жарки
     */
    var fryTime : Int,
    /**
     * Время тушения
     */
    var braiseTime : Int
)