package com.zelianko.kitchencalculator.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Recipe::class, ProductEn::class
    ],
    version = 1
)
abstract class MainDb : RoomDatabase() {
    abstract val recipeDao:RecipeDao
    abstract val productEnDao:ProductEnDao
}