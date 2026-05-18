package com.zelianko.kitchencalculator.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Описание/заметки рецепта. Вынесено в отдельную таблицу, чтобы не трогать
 * preseeded `main.db` ассет и сохранить совместимость со схемой v1.
 *
 * Связь с [Recipe] — мягкая (без FK): repository чистит запись по recipeId
 * при удалении рецепта.
 */
@Entity(tableName = "recipe_description")
data class RecipeDescription(
    @PrimaryKey
    val recipeId: Long,
    val description: String,
)
