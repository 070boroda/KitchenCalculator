package com.zelianko.kitchencalculator.recipe_update_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zelianko.kitchencalculator.data.ProductEnRepository
import com.zelianko.kitchencalculator.data.Recipe
import com.zelianko.kitchencalculator.data.RecipeDescription
import com.zelianko.kitchencalculator.data.RecipeDescriptionRepository
import com.zelianko.kitchencalculator.data.RecipeRepository
import com.zelianko.kitchencalculator.recipe_add_screen.ProductDraft
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

@HiltViewModel
class RecipeUpdateViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val productRepository: ProductEnRepository,
    private val descriptionRepository: RecipeDescriptionRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val recipeId: Long = requireNotNull(savedStateHandle.get<String>("recipeId")) {
        "RecipeUpdateScreen открыт без параметра recipeId"
    }.toLong()

    private val _state = MutableStateFlow(RecipeUpdateUiState(recipeId = recipeId))
    val state: StateFlow<RecipeUpdateUiState> = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadRecipe()
    }

    private fun loadRecipe() {
        viewModelScope.launch {
            val recipe = withContext(Dispatchers.IO) { recipeRepository.getRecipeById(recipeId) }
            val products = withContext(Dispatchers.IO) {
                productRepository.getAllItemsByRecipeId(recipeId)
            }
            val description = withContext(Dispatchers.IO) {
                descriptionRepository.getByRecipeId(recipeId)?.description.orEmpty()
            }
            _state.update {
                it.copy(
                    name = recipe.name,
                    imageUri = recipe.imageUrl,
                    description = description,
                    products = products.map(ProductDraft::fromEntity)
                        .ifEmpty { listOf(ProductDraft.empty()) },
                    isLoaded = true,
                )
            }
        }
    }

    fun onEvent(event: RecipeUpdateEvent) {
        when (event) {
            is RecipeUpdateEvent.RecipeNameTextEnter ->
                _state.update { it.copy(name = event.text) }

            is RecipeUpdateEvent.RecipeImageEnter ->
                _state.update { it.copy(imageUri = event.uri) }

            is RecipeUpdateEvent.RecipeDescriptionEnter ->
                _state.update { it.copy(description = event.text) }

            is RecipeUpdateEvent.IngredientName ->
                _state.update { it.copy(products = it.products.updateAt(event.index) { p -> p.copy(name = event.text) }) }

            is RecipeUpdateEvent.IngredientWeight ->
                _state.update { it.copy(products = it.products.updateAt(event.index) { p -> p.copy(amount = event.weight) }) }

            is RecipeUpdateEvent.MeasureWeight ->
                _state.update { it.copy(products = it.products.updateAt(event.index) { p -> p.copy(unit = event.value) }) }

            is RecipeUpdateEvent.AddRowProduct ->
                _state.update { it.copy(products = it.products + ProductDraft.empty()) }

            is RecipeUpdateEvent.DismissItem -> {
                _state.update { current ->
                    val newList = current.products.toMutableList().apply {
                        if (event.index in indices) removeAt(event.index)
                        if (isEmpty()) add(ProductDraft.empty())
                    }
                    current.copy(products = newList)
                }
            }

            is RecipeUpdateEvent.OnItemSave -> saveRecipe()

            is RecipeUpdateEvent.OnItemClick ->
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
                recipeRepository.insertRecipe(
                    Recipe(id = recipeId, name = current.name, imageUrl = current.imageUri)
                )
                // Простейший вариант: переписываем все продукты рецепта.
                // ProductEn не используется другими таблицами, поэтому пересоздать безопасно.
                productRepository.deleteProductEnByRecipeId(recipeId)
                cleaned.forEach { draft ->
                    productRepository.insertProductEn(draft.copy(dbId = null).toEntity(recipeId))
                }
                if (current.description.isBlank()) {
                    descriptionRepository.deleteByRecipeId(recipeId)
                } else {
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
