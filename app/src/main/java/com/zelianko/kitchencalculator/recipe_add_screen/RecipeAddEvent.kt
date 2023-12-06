package com.zelianko.kitchencalculator.recipe_add_screen

sealed class RecipeAddEvent {

    data class RecipeNameTextEnter(val text: String) : RecipeAddEvent()
    data class RecipeImageEnter(val uri: String) : RecipeAddEvent()
    data class RecipeProductNameEnter(val name: String) : RecipeAddEvent()
    //Выход из экрана
    data class OnItemClick(val route: String) : RecipeAddEvent()
    object OnItemSave : RecipeAddEvent()


}
