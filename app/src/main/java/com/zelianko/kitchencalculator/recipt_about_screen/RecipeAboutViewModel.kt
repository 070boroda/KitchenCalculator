package com.zelianko.kitchencalculator.recipt_about_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zelianko.kitchencalculator.data.ProductEnRepository
import com.zelianko.kitchencalculator.data.RecipeRepository
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
    savedStateHandle: SavedStateHandle
) : ViewModel() {

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

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun countWeight(product: ProductDto) {
        product.countMass = product.mass * _counter.value
    }

    inner class ProductDto(
        var name: String,
        var mass: Double,
        var countMass: Double,
        var measureWeight: String
    )
}

