package com.zelianko.kitchencalculator.recipe_update_screen

import com.zelianko.kitchencalculator.recipe_add_screen.ProductDraft

/**
 * UI state экрана редактирования рецепта.
 *
 * `isLoaded` нужен, чтобы UI знал, дочитал ли init корутины из БД — иначе
 * экран мигает пустыми полями до завершения загрузки.
 */
data class RecipeUpdateUiState(
    val recipeId: Long? = null,
    val name: String = "",
    val imageUri: String = "",
    val description: String = "",
    val products: List<ProductDraft> = emptyList(),
    val isLoaded: Boolean = false,
    val isSaving: Boolean = false,
)
