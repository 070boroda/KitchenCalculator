package com.zelianko.kitchencalculator.dialog

sealed class DialogSizeTypes {

    object FromRadius : DialogSizeTypes()
    object FromHeight : DialogSizeTypes()
    object FromWidth : DialogSizeTypes()


    object ToRadius : DialogSizeTypes()
    object ToHeight : DialogSizeTypes()
    object ToWidth : DialogSizeTypes()
}