package com.zelianko.kitchencalculator.recipt_about_screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zelianko.kitchencalculator.data.ProductEnRepository
import com.zelianko.kitchencalculator.data.RecipeRepository
import com.zelianko.kitchencalculator.dialog.DialogController
import com.zelianko.kitchencalculator.dialog.DialogEvent
import com.zelianko.kitchencalculator.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeAboutViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val productRepository: ProductEnRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), DialogController {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    /**
     * Счетчик для умножения кол-ва продуктов
     */
    private val _counter = MutableStateFlow(1.0)
    val counter: StateFlow<Double> = _counter.asStateFlow()

    private var recipeId: Long = savedStateHandle.get<String>("recipeId")?.toLong()!!

    private var _recipeName = MutableStateFlow("")
    val recipeName: StateFlow<String> = _recipeName.asStateFlow()

    private var _uriImage = MutableStateFlow("")
    val uriImage: StateFlow<String> = _uriImage.asStateFlow()

    private var _productList = MutableStateFlow<List<ProductDto>>(emptyList())
    val productList: StateFlow<List<ProductDto>> = _productList.asStateFlow()

    private val _coff = MutableStateFlow(1.0)
    var coff: StateFlow<Double> = _coff.asStateFlow()


    //Dialog
    override var fromFormCircle = mutableStateOf(true)
        private set
    override var fromFormRectangle = mutableStateOf(false)
        private set
    override var toFormCircle = mutableStateOf(true)
        private set
    override var toFormRectangle = mutableStateOf(false)
        private set
    override var fromRadius = mutableStateOf("")
        private set
    override var fromWidth = mutableStateOf("")
        private set
    override var fromHeight = mutableStateOf("")
        private set
    override var toRadius = mutableStateOf("")
        private set
    override var toWidth = mutableStateOf("")
        private set
    override var toHeight = mutableStateOf("")
        private set
    override var openDialog = mutableStateOf(false)
        private set


    init {
        viewModelScope.launch(Dispatchers.IO) {
            _recipeName.emit(recipeRepository.getRecipeById(recipeId).name)
            _uriImage.emit(recipeRepository.getRecipeById(recipeId).imageUrl)
            _productList.value = productRepository.getAllItemsByRecipeId(recipeId)
                .map {
                    ProductDto(
                        name = it.name,
                        mass = it.mass,
                        countMass = (it.mass * _counter.value),
                        measureWeight = it.measureWeight
                    )
                }
        }
    }

    fun onEvent(event: RecipeAboutEvent) {
        when (event) {
            is RecipeAboutEvent.CounterUp -> {
                _counter.value = _counter.value + 0.5
                _productList.value.forEach(
                    this::countWeight
                )
            }

            is RecipeAboutEvent.CounterDown -> {
                if (_counter.value != 0.0) {
                    _counter.value = _counter.value - 0.5
                    _productList.value.forEach(
                        this::countWeight
                    )
                }
            }

            is RecipeAboutEvent.OnItemClickBackRoute -> {
                sendUiEvent(UiEvent.Navigate(event.route))
            }
        }
    }


    //Work with dialog
    override fun onDialogEvent(event: DialogEvent) {
        when (event) {
            is DialogEvent.OnChangeFromForm -> {
                if (fromFormCircle.value) {
                    fromFormCircle.value = false
                    fromFormRectangle.value = true
                } else {
                    fromFormCircle.value = true
                    fromFormRectangle.value = false
                }
            }

            is DialogEvent.OnChangeFromTo -> {
                if (toFormCircle.value) {
                    toFormCircle.value = false
                    toFormRectangle.value = true
                } else {
                    toFormCircle.value = true
                    toFormRectangle.value = false
                }
            }
            //SIZE FROM
            is DialogEvent.RadiusSetFrom -> {
                fromRadius.value = event.text
            }

            is DialogEvent.HeightSetFrom -> {
                fromHeight.value = event.text
            }

            is DialogEvent.WidthSetFrom -> {
                fromWidth.value = event.text
            }

            //SIZE TO
            is DialogEvent.RadiusSetTo -> {
                toRadius.value = event.text
            }

            is DialogEvent.HeightSetTo -> {
                toHeight.value = event.text
            }

            is DialogEvent.WidthSetTo -> {
                toWidth.value = event.text
            }

            is DialogEvent.OnCancel -> {
                openDialog.value = !openDialog.value
            }

            is DialogEvent.OnConfirm -> {
                //Пересчет из круглой формы в круглую
                if (fromFormCircle.value && toFormCircle.value) {
                    if (fromRadius.value.toInt() == 0 || toRadius.value.toInt() == 0) return

                    val fromTempRadius = fromRadius.value.toDouble() * fromRadius.value.toDouble()
                    val toTempRadius = toRadius.value.toDouble() * toRadius.value.toDouble()
                    if (fromTempRadius < toTempRadius) {
                        _coff.value = Math.round((toTempRadius / fromTempRadius) * 100.00) / 100.00
                    } else {
                        _coff.value = Math.round((toTempRadius / fromTempRadius) * 100.00) / 100.00
                    }

                } else if (toFormRectangle.value && toFormRectangle.value) {
                    //Пересчет из прямоугольной формы в прямоугольную


                    Log.d("My log", coff.toString())
                }
                openDialog.value = !openDialog.value
                Log.d("My log", coff.toString())
                _productList.value.forEach(
                    this::countByCoff
                )
            }
        }
    }


    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun countByCoff(product: ProductDto) {
        product.countMass = Math.round((product.mass * _coff.value) * 100.00) / 100.00
    }

    private fun countWeight(product: ProductDto) {
        product.countMass = Math.round((product.mass * _counter.value) * 100.00) / 100.00
    }

    inner class ProductDto(
        var name: String,
        var mass: Double,
        var countMass: Double,
        var measureWeight: String
    )
}

