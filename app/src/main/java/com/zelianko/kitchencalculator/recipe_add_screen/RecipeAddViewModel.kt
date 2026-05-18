package com.zelianko.kitchencalculator.recipe_add_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zelianko.kitchencalculator.data.ProductEnRepository
import com.zelianko.kitchencalculator.data.Recipe
import com.zelianko.kitchencalculator.data.RecipeDescription
import com.zelianko.kitchencalculator.data.RecipeDescriptionRepository
import com.zelianko.kitchencalculator.data.RecipeRepository
import com.zelianko.kitchencalculator.util.Routes
import com.zelianko.kitchencalculator.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class RecipeAddUiState(
    val name: String = "",
    val imageUri: String = "",
    val description: String = "",
    val products: List<ProductDraft> = listOf(ProductDraft.empty()),
    val isSaving: Boolean = false,
)

@HiltViewModel
class RecipeAddViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val productRepository: ProductEnRepository,
    private val descriptionRepository: RecipeDescriptionRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(RecipeAddUiState())
    val state: StateFlow<RecipeAddUiState> = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: RecipeAddEvent) {
        when (event) {
            is RecipeAddEvent.RecipeNameTextEnter ->
                _state.update { it.copy(name = event.text) }

            is RecipeAddEvent.RecipeImageEnter ->
                _state.update { it.copy(imageUri = event.uri) }

            is RecipeAddEvent.RecipeDescriptionEnter ->
                _state.update { it.copy(description = event.text) }

            is RecipeAddEvent.IngredientName ->
                _state.update { it.copy(products = it.products.updateAt(event.index) { p -> p.copy(name = event.text) }) }

            is RecipeAddEvent.IngredientWeight ->
                _state.update { it.copy(products = it.products.updateAt(event.index) { p -> p.copy(amount = event.weight) }) }

            is RecipeAddEvent.MeasureWeight ->
                _state.update { it.copy(products = it.products.updateAt(event.index) { p -> p.copy(unit = event.value) }) }

            is RecipeAddEvent.AddRowProduct ->
                _state.update { it.copy(products = it.products + ProductDraft.empty()) }

            is RecipeAddEvent.DismissItem -> {
                _state.update { current ->
                    val newList = current.products.toMutableList().apply {
                        if (event.index in indices) removeAt(event.index)
                        if (isEmpty()) add(ProductDraft.empty())
                    }
                    current.copy(products = newList)
                }
            }

            is RecipeAddEvent.OnItemSave -> saveRecipe()

            is RecipeAddEvent.OnItemClick ->
                sendUiEvent(UiEvent.Navigate(event.route))
        }
    }

    private fun saveRecipe() {
        val current = _state.value
        if (current.name.isBlank()) {
            sendUiEvent(UiEvent.ShowSnackBarIfNameRecipeIsEmpty)
            return
        }
        val cleaned = current.products.filter { it.name.isNotBlank() }
        if (cleaned.isEmpty()) {
            sendUiEvent(UiEvent.ShowSnackBarIfNameProductIsEmpty)
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            withContext(Dispatchers.IO) {
                val recipeId = recipeRepository.insertRecipe(
                    Recipe(id = null, name = current.name, imageUrl = current.imageUri)
                )
                cleaned.forEach { draft ->
                    productRepository.insertProductEn(draft.toEntity(recipeId))
                }
                if (current.description.isNotBlank()) {
                    descriptionRepository.upsert(
                        RecipeDescription(recipeId = recipeId, description = current.description)
                    )
                }
            }
            _state.update { it.copy(isSaving = false) }
            sendUiEvent(UiEvent.RecipeSaved)
            sendUiEvent(UiEvent.Navigate(Routes.RECIPE_LIST_SCREEN))
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private inline fun List<ProductDraft>.updateAt(
        index: Int,
        transform: (ProductDraft) -> ProductDraft,
    ): List<ProductDraft> {
        if (index !in indices) return this
        return toMutableList().also { it[index] = transform(it[index]) }
    }
}
