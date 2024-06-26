package com.zelianko.kitchencalculator.dialog

import androidx.compose.runtime.MutableState

interface DialogController {

    val fromFormCircle: MutableState<Boolean>
    val fromFormRectangle: MutableState<Boolean>


    val toFormRectangle: MutableState<Boolean>
    val toFormCircle: MutableState<Boolean>

    val fromDiametr: MutableState<String>
    val fromWidth: MutableState<String>
    val fromHeight: MutableState<String>

    val toDiametr: MutableState<String>
    val toWidth: MutableState<String>
    val toHeight: MutableState<String>

    val openDialog: MutableState<Boolean>

    fun onDialogEvent(event: DialogEvent)

}