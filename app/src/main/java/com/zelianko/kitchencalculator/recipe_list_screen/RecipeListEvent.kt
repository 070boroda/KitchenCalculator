package com.zelianko.kitchencalculator.recipe_list_screen

sealed class RecipeListEvent {

    data class SearchRecipe(val text: String) : RecipeListEvent()
    data class OnItemClick(val route: String) : RecipeListEvent()

    //    data class OnShowDeleteDialog(val item: Recipe) : RecipeListEvent()
//    data class OnShowEditDialog(val item: Recipe) : RecipeListEvent()

//    object OnItemSave : RecipeListEvent()

}
