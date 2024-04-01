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
        11 to R.string.baranina_big,
        12 to R.string.baranina_small,
        13 to R.string.govyadina_big,
        14 to R.string.govyadina_small,
        15 to R.string.gus_celyi,
        16 to R.string.indeika_celyi,
        17 to R.string.kotleti,
        18 to R.string.krolik,
        19 to R.string.kuricca,
        20 to R.string.lungs,
        21 to R.string.liver,
        22 to R.string.pork,
        23 to R.string.heart,
        24 to R.string.duck,
        26 to R.string.jellied,
        27 to R.string.tongue,
        28 to R.string.egg_in_bag,
        29 to R.string.hard_boiled_egg,
        30 to R.string.soft_boiled_egg,
        54 to R.string.squid,
        55 to R.string.shrimp_pink,
        56 to R.string.shrimp_grey,
        57 to R.string.crab,
        58 to R.string.mussels,
        59 to R.string.scallop,
        60 to R.string.octopus,
        61 to R.string.lobster,
        62 to R.string.crayfish,
        63 to R.string.pieces_of_fish,
        64 to R.string.big_pieces_of_fish,
        65 to R.string.pieces_of_sturgeon,
        66 to R.string.salmon,
        67 to R.string.codfish,
        68 to R.string.zanderfish,
        69 to R.string.fish_cakes,
        70 to R.string.artichoke,
        71 to R.string.eggplant,
        72 to R.string.zucchini,
        73 to R.string.cabbage,
        74 to R.string.broccoli,
        75 to R.string.potatoes_sliced,
        76 to R.string.potatoes,
        77 to R.string.leek,
        78 to R.string.onions,
        79 to R.string.carrot,
        80 to R.string.tomato,
        81 to R.string.beet,
        82 to R.string.pumpkin,
        83 to R.string.string_beans,
        84 to R.string.fresh_lentil,
        85 to R.string.penne,
        86 to R.string.cockleshells,
        87 to R.string.elbow_pasta_big,
        88 to R.string.elbow_pasta_small,
        89 to R.string.spaghetti,
        90 to R.string.spiral_pasta,
        91 to R.string.farfalle,
        92 to R.string.fettuccine,
        93 to R.string.pea,
        94 to R.string.semolina_for_time,
        95 to R.string.porridge_for_time,
        96 to R.string.pearl_barley,
        97 to R.string.millet,
        98 to R.string.rice,
        99 to R.string.kidney_beans,
        100 to R.string.lentils_dried,
        101 to R.string.white_mushrooms,
        102 to R.string.boletus,
        103 to R.string.boletus_two,
        104 to R.string.chanterelles,
        105 to R.string.champignons,
        106 to R.string.oyster_mushrooms,
        107 to R.string.mushrooms_dried,
        108 to R.string.mushrooms_frozen,
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
