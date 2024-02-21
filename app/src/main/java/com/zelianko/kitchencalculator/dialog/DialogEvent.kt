package com.zelianko.kitchencalculator.dialog

sealed class DialogEvent {

    object OnChangeFromForm : DialogEvent()
    object OnChangeFromTo : DialogEvent()

    data class RadiusSetFrom(val text: String) : DialogEvent()
    data class WidthSetFrom(val text: String) : DialogEvent()
    data class HeightSetFrom(val text: String) : DialogEvent()

    data class RadiusSetTo(val text: String) : DialogEvent()
    data class WidthSetTo(val text: String) : DialogEvent()
    data class HeightSetTo(val text: String) : DialogEvent()


    object OnCancel : DialogEvent()
    object OnConfirm : DialogEvent()
}
