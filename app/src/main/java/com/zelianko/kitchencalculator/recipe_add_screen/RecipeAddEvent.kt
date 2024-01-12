package com.zelianko.kitchencalculator.recipe_add_screen

import com.zelianko.kitchencalculator.data.ProductEn

sealed class RecipeAddEvent {

    //Название рецепта
    data class RecipeNameTextEnter(val text: String) : RecipeAddEvent()
    //Картинка
    data class RecipeImageEnter(val uri: String) : RecipeAddEvent()
    //Добавление строки продукт
    data class RecipeProductEnter(val value: ThreeField<String>, val index: Int) : RecipeAddEvent()
    //Удаление продуктов
    object DismissItem : RecipeAddEvent()
    //Имя продукта
    data class IngredientName(val text: String, val index: Int) : RecipeAddEvent()
    //Вес продукта
    data class IngredientWeight(val weight: String, val index: Int) : RecipeAddEvent()
    data class MeasureWeight(val value: String, val index: Int) : RecipeAddEvent()
    //Выход из экрана
    data class OnItemClick(val route: String) : RecipeAddEvent()
    object AddRowProduct : RecipeAddEvent()
    //Сохранение рецепта
    object OnItemSave : RecipeAddEvent()


}
