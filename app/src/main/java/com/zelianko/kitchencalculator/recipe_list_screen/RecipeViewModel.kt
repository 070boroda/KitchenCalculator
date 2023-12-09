package com.zelianko.kitchencalculator.recipe_list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zelianko.kitchencalculator.data.ProductEnRepository
import com.zelianko.kitchencalculator.data.ProductEnRepositoryImp
import com.zelianko.kitchencalculator.data.RecipeRepository
import com.zelianko.kitchencalculator.data.RecipeRepositoryImpl
import com.zelianko.kitchencalculator.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val productRepository: ProductEnRepository
) : ViewModel() {

    var listRecipe = recipeRepository.getAllItems()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: RecipeListEvent) {
        when (event) {
            is RecipeListEvent.SearchRecipe -> {
//                if (event.text.isBlank()) {
//                    listRecipe = repository.getAllItems()
//                } else {
//                    listRecipe = repository.getAllItemsByName("${event.text}%")
//                }
            }
            is RecipeListEvent.OnItemClick -> {
                sendUiEvent(UiEvent.Navigate(event.route))
            }
            is RecipeListEvent.DeleteRecipe -> {
                viewModelScope.launch {
                    event.recipe.id?.let { productRepository.deleteProductEnByRecipeId(it) }
                    event.recipe.let { recipeRepository.deleteRecipe(it) }
                }
            }

        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}