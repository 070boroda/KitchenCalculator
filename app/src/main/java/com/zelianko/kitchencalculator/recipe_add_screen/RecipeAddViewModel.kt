package com.zelianko.kitchencalculator.recipe_add_screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject

@HiltViewModel
class RecipeAddViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val productRepository: ProductEnRepository
) : ViewModel() {

    @SuppressLint("MutableCollectionMutableState")
    val listProduct = mutableStateListOf(TwoField("", ""))

    var nameRecipeText = mutableStateOf("")
        private set
    var imageUri = mutableStateOf("")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: RecipeAddEvent) {
        when (event) {
            is RecipeAddEvent.RecipeNameTextEnter -> {
                nameRecipeText.value = event.text
                Log.d("MyLog", nameRecipeText.value)
            }

            is RecipeAddEvent.RecipeImageEnter -> {
                imageUri.value = event.uri
                Log.d("MyLog", imageUri.value)
            }

            is RecipeAddEvent.AddRowProduct -> {
                listProduct.add(TwoField("", ""))
                Log.d("MyLog", "Add product")
            }

            is RecipeAddEvent.RecipeProductEnter -> {
                if (StringUtils.isBlank(event.value.value) or StringUtils.isBlank(event.value.key)) return
                listProduct[event.index] = event.value
            }

            is RecipeAddEvent.DismissItem -> {
                if (listProduct.size == 1) return
                listProduct.removeAt(index = event.index)
            }

            is RecipeAddEvent.IngredientName -> {
                if (event.text.isBlank()) return
                listProduct[event.index].key = event.text
            }

            is RecipeAddEvent.IngredientWeight -> {
                Log.d("MyLog", "${event.weight}")
                if (event.weight.isBlank()) return
                listProduct[event.index].value = event.weight
            }

            //Сохраняем рецепт выходим из экрана
            is RecipeAddEvent.OnItemSave -> {
                if (nameRecipeText.value.isBlank()) return
                Log.d("MyLog", nameRecipeText.value)
                if (listProduct.isEmpty()) return
                Log.d("MyLog", listProduct.size.toString())
                Log.d("MyLog", "${listProduct[0].key} ${listProduct[0].value}")
                if (listProduct.size == 1 && (listProduct[0].key.isBlank() || listProduct[0].value.isBlank())) return
                Log.d("MyLog", "${listProduct[0].key} ${listProduct[0].value}")

                viewModelScope.launch(Dispatchers.IO) {
                    val idRecipe = recipeRepository.insertRecipe(
                        Recipe(
                            null,
                            nameRecipeText.value,
                            imageUri.value
                        )
                    )
                    Log.d("MyLog", idRecipe.toString())
                    listProduct
                        .forEach { ingredient ->
                            productRepository.insertProductEn(
                                ProductEn(
                                    null,
                                    ingredient.key,
                                    ingredient.value.toDouble(),
                                    idRecipe
                                )
                            )

                        }
                }
                sendUiEvent(UiEvent.Navigate(Routes.RECIPE_LIST_SCREEN))
            }
            is RecipeAddEvent.OnItemClick -> {
                sendUiEvent(UiEvent.Navigate(event.route))
            }
            else -> {}
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}

data class TwoField<T>(var key: T, var value: T) {

}