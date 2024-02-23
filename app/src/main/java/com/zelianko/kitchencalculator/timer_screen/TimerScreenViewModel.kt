package com.zelianko.kitchencalculator.timer_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.data.CookingTime
import com.zelianko.kitchencalculator.data.CookingTimeRepository
import com.zelianko.kitchencalculator.util.GroupProduct
import com.zelianko.kitchencalculator.util.TypeCooking
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerScreenViewModel @Inject constructor(
    private val cookingTimeRepository: CookingTimeRepository,
) : ViewModel() {
    val mapTranslateProduct = hashMapOf(
        "Баранина" to R.string.baranina,
        "Говядина" to R.string.baranina,
        "Кальмар" to R.string.baranina,
        "Креветка" to R.string.baranina,
        "Котлеты" to R.string.baranina,
        "Сердце" to R.string.baranina,
        "Лосось" to R.string.baranina,
        "Свиннина" to R.string.baranina,
        "Курица" to R.string.baranina
    )

    /**
     * Список продуктов
     */
    private var _cookingTimeList = MutableStateFlow<List<CookingTime>>(emptyList())
    val cookingTimeList: StateFlow<List<CookingTime>> = _cookingTimeList.asStateFlow()

    /**
     * Тип приготовления
     */
    private val _numberCookingType = MutableStateFlow(TypeCooking.FRY)
    val numberCookingType: StateFlow<Int> = _numberCookingType.asStateFlow()

    /**
     * ыбранный продукт для приготовления
     */
    private val _cookingProduct = MutableStateFlow(CookingTime(null, 0, "", 0, 0, 0))
    val cookingProduct: StateFlow<CookingTime> = _cookingProduct.asStateFlow()

    /**
     * Значение времени приготовления для таймера
     */
    private val _cookingTimeForTimer = MutableStateFlow(25L)
    val cookingTimeForTimer: StateFlow<Long> = _cookingTimeForTimer.asStateFlow()

    init {
        viewModelScope.launch {
            cookingTimeRepository.getAllCookingTimeByGroup(GroupProduct.CHICKEN_AND_MEAT).collect {
                _cookingTimeList.value = it
            }
        }
    }

    fun onEvent(event: TimerScreenEvent) {
        when (event) {
            is TimerScreenEvent.ChooseGroup -> {
                viewModelScope.launch {
                    cookingTimeRepository.getAllCookingTimeByGroup(event.groupNumber).collect {
                        _cookingTimeList.value = it
                    }
                    _cookingTimeForTimer.emit(getCookingTimer())
                }
            }

            is TimerScreenEvent.ChooseCookingType -> {
                _numberCookingType.value = event.cookingTypeNumber
                viewModelScope.launch {
                    _cookingTimeForTimer.emit(getCookingTimer())
                }
            }

            is TimerScreenEvent.ChooseProduct -> {
                viewModelScope.launch {
                    _cookingProduct.update {
                        event.idProduct?.let { it1 ->
                            cookingTimeRepository.getCookingTimeById(
                                it1
                            )
                        }!!
                    }
                    _cookingTimeForTimer.emit(getCookingTimer())
                }
            }
        }
    }

    private fun getCookingTimer(): Long {
        return when (_numberCookingType.value) {
            TypeCooking.FRY ->  cookingProduct.value.fryTime.toLong() * 1000L
            TypeCooking.BOILING -> cookingProduct.value.boilingTime.toLong() * 1000L
            TypeCooking.BRAISE -> cookingProduct.value.braiseTime.toLong() * 1000L
            else -> {
                0
            }
        }
    }

}

data class TwoField(val name: String, val key: Int)
