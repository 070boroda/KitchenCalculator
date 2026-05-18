package com.zelianko.kitchencalculator.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * v1 -> v2: добавлена таблица [RecipeDescription] для хранения заметок к рецептам.
 * Колонки совпадают с сигнатурой, которую Room ожидает по аннотациям entity:
 *   - recipeId INTEGER NOT NULL PRIMARY KEY
 *   - description TEXT NOT NULL
 *
 * Если изменишь поля entity — перегенерируй ожидаемый SQL через
 * Build → Make Project, посмотри schemas/MainDb/2.json и сверь.
 */
val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `recipe_description` (
                `recipeId` INTEGER NOT NULL,
                `description` TEXT NOT NULL,
                PRIMARY KEY(`recipeId`)
            )
            """.trimIndent()
        )
    }
}
