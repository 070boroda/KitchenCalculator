package com.zelianko.kitchencalculator.recipe_update_screen

import com.zelianko.kitchencalculator.data.ProductEn
import com.zelianko.kitchencalculator.data.Recipe

sealed class RecipeUpdateState {

    object Initial : RecipeUpdateState()
    object Loading: RecipeUpdateState()

    data class RecipeDto(var recipe: Recipe?, var products: List<ProductEn>) : RecipeUpdateState()
}
