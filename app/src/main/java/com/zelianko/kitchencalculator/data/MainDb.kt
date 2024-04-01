package com.zelianko.kitchencalculator.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Миграция, внести дополнения в БД локально, изменить версию на единицу поле version,
 * добавить в массив autoMigrations AutoMigration(from = 2, to = 3) с какой версии на какую переходим.
 * В файле App.kt
 * добавить код миграции
 */
@Database(
    entities = [
        Recipe::class, ProductEn::class, CookingTime::class
    ],
//    autoMigrations = [AutoMigration(from = 1, to = 2),
//        AutoMigration(from = 2, to = 3),
//        AutoMigration(from = 3, to = 4),
//        AutoMigration(from = 4, to = 5),
//        AutoMigration(from = 5, to = 6),
//        AutoMigration(from = 6, to = 7),
//        AutoMigration(from = 7, to = 8),
//        AutoMigration(from = 8, to = 9),
//                     ],
    version = 1,
    exportSchema = true
)
abstract class MainDb : RoomDatabase() {
    abstract val recipeDao: RecipeDao
    abstract val productEnDao: ProductEnDao
    abstract val cookingTimeDao: CookingTimeDao
}