package com.zelianko.kitchencalculator.recipt_about_screen

sealed class RecipeAboutEvent {

    data class OnItemClickBackRoute(val route: String) : RecipeAboutEvent()
    object CounterUp : RecipeAboutEvent()
    object CounterDown : RecipeAboutEvent()
}