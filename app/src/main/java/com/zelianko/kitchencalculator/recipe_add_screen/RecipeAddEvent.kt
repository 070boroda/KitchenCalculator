package com.zelianko.kitchencalculator.recipe_add_screen

sealed class RecipeAddEvent {
    data class RecipeNameTextEnter(val text: String) : RecipeAddEvent()
    data class RecipeImageEnter(val uri: String) : RecipeAddEvent()
    data class RecipeDescriptionEnter(val text: String) : RecipeAddEvent()
    data class IngredientName(val text: String, val index: Int) : RecipeAddEvent()
    data class IngredientWeight(val weight: String, val index: Int) : RecipeAddEvent()
    data class MeasureWeight(val value: String, val index: Int) : RecipeAddEvent()
    data class DismissItem(val index: Int) : RecipeAddEvent()
    object AddRowProduct : RecipeAddEvent()
    object OnItemSave : RecipeAddEvent()
    data class OnItemClick(val route: String) : RecipeAddEvent()
}
