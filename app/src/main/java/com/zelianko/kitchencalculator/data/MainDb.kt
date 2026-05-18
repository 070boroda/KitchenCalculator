package com.zelianko.kitchencalculator.data

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * История схем:
 *  v1 — стартовая (preseeded из assets/main.db): recipe, product_en, cooking_time.
 *  v2 — добавлена таблица recipe_description (мягкая связь по recipeId).
 *
 * При добавлении новой версии:
 *  1. Поднимай `version` ниже.
 *  2. Опиши SQL миграции в [Migrations.kt].
 *  3. Передай миграцию в `Room.databaseBuilder(...).addMigrations(...)` в [di/App.kt].
 *  4. Запусти сборку — Room сравнит фактическую схему с ожиданием и упадёт,
 *     если не совпало (см. schemas/MainDb/<version>.json).
 */
@Database(
    entities = [
        Recipe::class,
        ProductEn::class,
        CookingTime::class,
        RecipeDescription::class,
    ],
    version = 2,
    exportSchema = true,
)
abstract class MainDb : RoomDatabase() {
    abstract val recipeDao: RecipeDao
    abstract val productEnDao: ProductEnDao
    abstract val cookingTimeDao: CookingTimeDao
    abstract val recipeDescriptionDao: RecipeDescriptionDao
}
