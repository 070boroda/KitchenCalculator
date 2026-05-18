package com.zelianko.kitchencalculator.util

sealed class UiEvent {
    object PopBackStack : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    object ShowSnackBarIfNameRecipeIsEmpty : UiEvent()
    object ShowSnackBarIfNameProductIsEmpty : UiEvent()
    object ShowSnackBarIfProductRowIsEmpty : UiEvent()
    object ShowSnackBarIfDiametrOrRowIsEmpty : UiEvent()

    /**
     * Универсальный snackbar c опциональным действием (например, Undo).
     * messageRes — resource id, чтобы UI сам резолвил локализацию.
     */
    data class ShowSnackbar(
        val messageRes: Int,
        val actionLabelRes: Int? = null,
        val onAction: (() -> Unit)? = null,
    ) : UiEvent()

    object RecipeSaved : UiEvent()
}
