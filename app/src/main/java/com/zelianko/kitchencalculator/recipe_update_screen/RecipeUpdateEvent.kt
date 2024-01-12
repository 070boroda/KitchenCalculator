package com.zelianko.kitchencalculator.recipe_update_screen

import com.zelianko.kitchencalculator.data.ProductEn
import com.zelianko.kitchencalculator.recipe_add_screen.ThreeField

sealed class RecipeUpdateEvent {

    //Название рецепта
    data class RecipeNameTextEnter(val text: String) : RecipeUpdateEvent()
    //Картинка
    data class RecipeImageEnter(val uri: String) : RecipeUpdateEvent()
    //Добавление строки продукт
    data class RecipeProductEnter(val value: ThreeField<String>, val index: Int) : RecipeUpdateEvent()
    //Удаление продуктов
    data class DismissItem(val productEn: ProductEn) : RecipeUpdateEvent()
    //Имя продукта
    data class IngredientName(val text: String, val productId: Long) : RecipeUpdateEvent()
    //Вес продукта
    data class IngredientWeight(val weight: String, val productId: Long) : RecipeUpdateEvent()
    data class MeasureWeight(val value: String, val productId: Long) : RecipeUpdateEvent()
    //Выход из экрана
    data class OnItemClick(val route: String) : RecipeUpdateEvent()
    object AddRowProduct : RecipeUpdateEvent()
    //Сохранение рецепта
    object OnItemSave : RecipeUpdateEvent()


}
