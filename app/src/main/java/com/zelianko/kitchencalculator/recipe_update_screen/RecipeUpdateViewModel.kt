package com.zelianko.kitchencalculator.recipe_update_screen

import android.annotation.SuppressLint
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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
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

    private var recipeId: Long = savedStateHandle.get<String>("recipeId")?.toLong()!!
    var recipe: Recipe? = null


    private var productList: MutableList<ProductEn>? = null
    private val recipeDto = RecipeUpdateState.RecipeDto(Recipe(null, "", ""), mutableListOf())
    private val _screenState = MutableStateFlow<RecipeUpdateState>(RecipeUpdateState.Initial)
    val screenState: StateFlow<RecipeUpdateState> = _screenState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            productList = productRepository.getAllItemsByRecipeId(recipeId)
            recipe = recipeRepository.getRecipeById(recipeId)
            recipeDto.recipe = recipe
            recipeDto.products = productList!!.toMutableList()
        }
        _screenState.value = recipeDto
    }

    @SuppressLint("SuspiciousIndentation")
    fun onEvent(event: RecipeUpdateEvent) {
        when (event) {
            is RecipeUpdateEvent.RecipeNameTextEnter -> {
                if (event.text.isBlank()) return
                viewModelScope.launch(Dispatchers.IO) {
                    recipe!!.name = event.text
                    recipeDto.recipe = recipe
                    recipeRepository.insertRecipe(recipe!!)
                }
            }

            is RecipeUpdateEvent.RecipeImageEnter -> {
                viewModelScope.launch(Dispatchers.IO) {
                    recipe!!.imageUrl = event.uri
                    recipeDto.recipe = recipe
                    recipeRepository.insertRecipe(recipe!!)
                }
//                recipeDto.recipe?.imageUrl = event.uri
            }

            is RecipeUpdateEvent.AddRowProduct -> {
//                    val lastProduct = productList!!.get(productList!!.size - 1)
//                    if (lastProduct.name.isEmpty() || lastProduct.measureWeight == null) {
//                        sendUiEvent(UiEvent.ShowSnackBarIfProductRowIsEmpty)
//                        return
//                    }

                viewModelScope.launch(Dispatchers.IO) {
                    _screenState.emit(RecipeUpdateState.Initial)
                    val pr = ProductEn(null, "", 0.00, "", recipeId)
                    productRepository.insertProductEn(pr)
                    productList = productRepository.getAllItemsByRecipeId(recipeId)
                    _screenState.emit(
                        recipeDto.copy(
                            products = productList!!.toMutableList()
                        )
                    )
                }
            }

            is RecipeUpdateEvent.RecipeProductEnter -> {
//                if (StringUtils.isBlank(event.value.value) or StringUtils.isBlank(event.value.key)) return
//                listProduct[event.index] = event.value
            }

            //Удаление ингредиентов работает
            is RecipeUpdateEvent.DismissItem -> {
                viewModelScope.launch(Dispatchers.IO){
                    _screenState.emit(RecipeUpdateState.Initial)
                    productRepository.deleteProductEn(event.productEn)
                    productList = productRepository.getAllItemsByRecipeId(recipeId)
                    _screenState.emit(
                        recipeDto.copy(
                            products = productList!!.toMutableList()
                        )
                    )
                }
            }
            //TODO Переделать на один запрос
            is RecipeUpdateEvent.IngredientName -> {
                if (event.text.isBlank()) return
                viewModelScope.launch(Dispatchers.IO) {
                    val product = productRepository.getProductEnById(event.productId)
                    product.name = event.text
                    productRepository.insertProductEn(product)
                }
            }

            is RecipeUpdateEvent.IngredientWeight -> {
                if (event.weight.isBlank()) return
                viewModelScope.launch(Dispatchers.IO) {
                    val product = productRepository.getProductEnById(event.productId)
                    product.mass = event.weight.toDouble()
                    productRepository.insertProductEn(product)
                }
            }
            //
            is RecipeUpdateEvent.MeasureWeight -> {
                if (event.value.isBlank()) return
                viewModelScope.launch(Dispatchers.IO) {
                    val product = productRepository.getProductEnById(event.productId)
                    product.measureWeight = event.value
                    productRepository.insertProductEn(product)
                }
               // recipeDto.products.find { it.id == event.productId }?.measureWeight = event.value
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
                                    ingredient.mass,
                                    ingredient.measureWeight,
                                    ingredient.recipeId
                                )
                            )

                        }
                    _screenState.emit(
                        recipeDto.copy(
                            recipe = Recipe(null, "", ""),
                            products = mutableListOf()
                        )
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