package com.zelianko.kitchencalculator.timer_screen

sealed class TimerScreenEvent {
    data class ChooseGroup(val groupNumber: Int) : TimerScreenEvent()
    data class ChooseCookingType(val cookingTypeNumber: Int) : TimerScreenEvent()
    data class ChooseProduct(val idProduct: Long?) : TimerScreenEvent()
}