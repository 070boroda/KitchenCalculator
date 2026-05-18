package com.zelianko.kitchencalculator.recipe_update_screen

sealed class RecipeUpdateEvent {
    data class RecipeNameTextEnter(val text: String) : RecipeUpdateEvent()
    data class RecipeImageEnter(val uri: String) : RecipeUpdateEvent()
    data class RecipeDescriptionEnter(val text: String) : RecipeUpdateEvent()
    data class IngredientName(val text: String, val index: Int) : RecipeUpdateEvent()
    data class IngredientWeight(val weight: String, val index: Int) : RecipeUpdateEvent()
    data class MeasureWeight(val value: String, val index: Int) : RecipeUpdateEvent()
    data class DismissItem(val index: Int) : RecipeUpdateEvent()
    object AddRowProduct : RecipeUpdateEvent()
    object OnItemSave : RecipeUpdateEvent()
    data class OnItemClick(val route: String) : RecipeUpdateEvent()
}
