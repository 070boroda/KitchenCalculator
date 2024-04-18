package com.zelianko.kitchencalculator.util

sealed class UiEvent {
    object PopBackStack: UiEvent()
    data class Navigate(val route: String): UiEvent()
    object ShowSnackBarIfNameRecipeIsEmpty: UiEvent()
    object ShowSnackBarIfNameProductIsEmpty: UiEvent()
    object ShowSnackBarIfProductRowIsEmpty: UiEvent()
    object ShowSnackBarIfDiametrOrRowIsEmpty: UiEvent()
}
