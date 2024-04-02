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
        1 to R.string.baranina_big,
        2 to R.string.baranina_small,
        3 to R.string.govyadina_big,
        4 to R.string.govyadina_small,
        5 to R.string.gus_celyi,
        6 to R.string.indeika_celyi,
        7 to R.string.kotleti,
        8 to R.string.krolik,
        9 to R.string.kuricca,
        10 to R.string.lungs,
        11 to R.string.liver,
        12 to R.string.pork,
        13 to R.string.heart,
        14 to R.string.duck,
        15 to R.string.jellied,
        17 to R.string.tongue,
        18 to R.string.egg_in_bag,
        19 to R.string.hard_boiled_egg,
        20 to R.string.soft_boiled_egg,
        21 to R.string.squid,
        22 to R.string.shrimp_pink,
        23 to R.string.shrimp_grey,
        24 to R.string.crab,
        25 to R.string.mussels,
        26 to R.string.scallop,
        27 to R.string.octopus,
        28 to R.string.lobster,
        29 to R.string.crayfish,
        30 to R.string.pieces_of_fish,
        31 to R.string.big_pieces_of_fish,
        32 to R.string.pieces_of_sturgeon,
        33 to R.string.salmon,
        34 to R.string.codfish,
        35 to R.string.zanderfish,
        36 to R.string.fish_cakes,
        37 to R.string.artichoke,
        38 to R.string.eggplant,
        39 to R.string.zucchini,
        40 to R.string.cabbage,
        41 to R.string.broccoli,
        42 to R.string.potatoes_sliced,
        43 to R.string.potatoes,
        44 to R.string.leek,
        45 to R.string.onions,
        46 to R.string.carrot,
        47 to R.string.tomato,
        48 to R.string.beet,
        49 to R.string.pumpkin,
        50 to R.string.string_beans,
        51 to R.string.fresh_lentil,
        52 to R.string.penne,
        53 to R.string.cockleshells,
        54 to R.string.elbow_pasta_big,
        55 to R.string.elbow_pasta_small,
        56 to R.string.spaghetti,
        57 to R.string.spiral_pasta,
        58 to R.string.farfalle,
        59 to R.string.fettuccine,
        60 to R.string.pea,
        61 to R.string.semolina_for_time,
        62 to R.string.porridge_for_time,
        63 to R.string.pearl_barley,
        64 to R.string.millet,
        65 to R.string.rice,
        66 to R.string.kidney_beans,
        67 to R.string.lentils_dried,
        68 to R.string.white_mushrooms,
        69 to R.string.boletus,
        70 to R.string.boletus_two,
        71 to R.string.chanterelles,
        72 to R.string.champignons,
        73 to R.string.oyster_mushrooms,
        74 to R.string.mushrooms_dried,
        75 to R.string.mushrooms_frozen,
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
