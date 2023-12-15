package com.zelianko.kitchencalculator.recipe_update_screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zelianko.kitchencalculator.data.ProductEn
import com.zelianko.kitchencalculator.data.ProductEnRepository
import com.zelianko.kitchencalculator.data.Recipe
import com.zelianko.kitchencalculator.data.RecipeRepository
import com.zelianko.kitchencalculator.util.Routes
import com.zelianko.kitchencalculator.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeUpdateViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val productRepository: ProductEnRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    var recipeId: Long? = null
    var recipe: Recipe? = null
    private var productList: Flow<List<ProductEn>>? = null
    private val recipeDto = RecipeUpdateState.RecipeDto(Recipe(null, "", ""), emptyList())
    private val _screenState = MutableStateFlow<RecipeUpdateState>(RecipeUpdateState.Initial)
    val screenState: StateFlow<RecipeUpdateState> = _screenState.asStateFlow()

    init {
        viewModelScope.launch {
            recipeId = savedStateHandle.get<String>("recipeId")?.toLong()!!
            Log.d("MyLog", "recipeId" + recipeId.toString())
            recipe = recipeRepository.getRecipeById(recipeId!!)
            Log.d("MyLog", recipe!!.name)
            recipeDto.recipe = recipe
        }
        productList = productRepository.getAllItemsByRecipeId(recipeId!!)
        viewModelScope.launch {
            productList!!.collect {
                recipeDto.products = it
            }
        }
        _screenState.value = recipeDto
    }

    @SuppressLint("SuspiciousIndentation")
    fun onEvent(event: RecipeUpdateEvent) {
        when (event) {
            is RecipeUpdateEvent.RecipeNameTextEnter -> {
                recipeDto.recipe?.name = event.text
            }
            is RecipeUpdateEvent.RecipeImageEnter -> {
                    recipeDto.recipe?.imageUrl = event.uri
            }

            is RecipeUpdateEvent.AddRowProduct -> {
                val currentState = _screenState.value
                if (currentState is RecipeUpdateState.RecipeDto) {
                    viewModelScope.launch {
                        _screenState.emit(RecipeUpdateState.Initial)
                        productRepository.insertProductEn(recipeId?.let {
                            ProductEn(
                                null, "", 0.00, "",
                                it
                            )
                        }!!)
                        productList = productRepository.getAllItemsByRecipeId(recipeId!!)
                        productList!!.collect {
                            _screenState.emit(
                                currentState.copy(
                                    products = it
                                )
                            )
                        }
                    }
                }
            }

            is RecipeUpdateEvent.RecipeProductEnter -> {
//                if (StringUtils.isBlank(event.value.value) or StringUtils.isBlank(event.value.key)) return
//                listProduct[event.index] = event.value
            }

            is RecipeUpdateEvent.DismissItem -> {
                viewModelScope.launch {
                    _screenState.emit(RecipeUpdateState.Initial)
                    productRepository.deleteProductEn(event.productEn)
                    productList = productRepository.getAllItemsByRecipeId(recipeId!!)
                    productList!!.collect {
                        _screenState.emit(
                            recipeDto.copy(
                                products = it
                            )
                        )
                    }
                }
            }

            is RecipeUpdateEvent.IngredientName -> {
                if (event.text.isBlank()) return
                recipeDto.products.find {it.id == event.productId}?.name = event.text
            }

            is RecipeUpdateEvent.IngredientWeight -> {
                if (event.weight.isBlank()) return
                recipeDto.products.find {it.id == event.productId}?.mass = event.weight.toDouble()
            }
            //
            is RecipeUpdateEvent.MeasureWeight -> {
                if (event.value.isBlank()) return
                recipeDto.products.find {it.id == event.productId}?.measureWeight = event.value
            }

            //Сохраняем рецепт выходим из экрана
            is RecipeUpdateEvent.OnItemSave -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val idRecipe = recipeDto.recipe?.name?.let {
                        recipeDto.recipe?.imageUrl?.let { it1 ->
                            Recipe(
                                recipeDto.recipe?.id,
                                it,
                                it1
                            )
                        }
                    }?.let {
                        recipeRepository.insertRecipe(
                            it
                        )
                    }

                    recipeDto.products
                        .forEach { ingredient ->
                            productRepository.insertProductEn(
                                ProductEn(
                                    ingredient.id,
                                    ingredient.name,
                                    ingredient.mass.toDouble(),
                                    ingredient.measureWeight,
                                    ingredient.recipeId
                                )
                            )

                        }
                    _screenState.emit(
                        recipeDto.copy( recipe = Recipe(null, "", ""),
                            products = emptyList())
                    )
                }
                sendUiEvent(UiEvent.Navigate(Routes.RECIPE_LIST_SCREEN))
            }

            is RecipeUpdateEvent.OnItemClick -> {
                sendUiEvent(UiEvent.Navigate(event.route))
            }
        }
    }


    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}