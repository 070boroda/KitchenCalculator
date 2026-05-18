package com.zelianko.kitchencalculator.recipe_list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zelianko.kitchencalculator.data.ProductEn
import com.zelianko.kitchencalculator.data.ProductEnRepository
import com.zelianko.kitchencalculator.data.Recipe
import com.zelianko.kitchencalculator.data.RecipeDescription
import com.zelianko.kitchencalculator.data.RecipeDescriptionRepository
import com.zelianko.kitchencalculator.data.RecipeRepository
import com.zelianko.kitchencalculator.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val productRepository: ProductEnRepository,
    private val descriptionRepository: RecipeDescriptionRepository,
) : ViewModel() {

    val listRecipe = recipeRepository.getAllItems()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    /** Последний удалённый рецепт (со всеми продуктами и описанием) — для Undo. */
    private var lastDeleted: DeletedRecipe? = null

    fun onEvent(event: RecipeListEvent) {
        when (event) {
            is RecipeListEvent.SearchRecipe -> Unit
            is RecipeListEvent.OnItemClick -> sendUiEvent(UiEvent.Navigate(event.route))
            is RecipeListEvent.DeleteRecipe -> deleteRecipe(event.recipe)
            is RecipeListEvent.UndoDelete -> undoDelete()
        }
    }

    private fun deleteRecipe(recipe: Recipe) {
        val recipeId = recipe.id ?: return
        viewModelScope.launch {
            val products = withContext(Dispatchers.IO) {
                productRepository.getAllItemsByRecipeId(recipeId)
            }
            val description = withContext(Dispatchers.IO) {
                descriptionRepository.getByRecipeId(recipeId)
            }
            lastDeleted = DeletedRecipe(recipe, products.toList(), description)
            withContext(Dispatchers.IO) {
                productRepository.deleteProductEnByRecipeId(recipeId)
                descriptionRepository.deleteByRecipeId(recipeId)
                recipeRepository.deleteRecipe(recipe)
            }
        }
    }

    private fun undoDelete() {
        val snapshot = lastDeleted ?: return
        lastDeleted = null
        viewModelScope.launch(Dispatchers.IO) {
            val restoredId = recipeRepository.insertRecipe(snapshot.recipe)
            snapshot.products.forEach { product ->
                productRepository.insertProductEn(
                    product.copy(id = null, recipeId = restoredId)
                )
            }
            snapshot.description?.let {
                descriptionRepository.upsert(it.copy(recipeId = restoredId))
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private data class DeletedRecipe(
        val recipe: Recipe,
        val products: List<ProductEn>,
        val description: RecipeDescription?,
    )
}
