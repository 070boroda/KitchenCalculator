package com.zelianko.kitchencalculator.recipe_list_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.data.Recipe
import com.zelianko.kitchencalculator.ui.theme.Dimens
import com.zelianko.kitchencalculator.util.Routes
import com.zelianko.kitchencalculator.util.UiEvent
import com.zelianko.kitchencalculator.yandex_ads.BannerId
import com.zelianko.kitchencalculator.yandex_ads.BannerSticky
import com.zelianko.kitchencalculator.yandex_ads.NativeAdRecipeItem
import com.zelianko.kitchencalculator.yandex_ads.NativeAdUnitId
import kotlinx.coroutines.launch

/** Через сколько рецептов показывать нативный блок в списке. */
private const val NATIVE_AD_INTERVAL = 5

@Composable
fun RecipeListScreen(
    viewModel: RecipeViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit,
) {
    val recipeList = viewModel.listRecipe.collectAsState(initial = emptyList())
    val textSearch = remember { mutableStateOf(TextFieldValue("")) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var pendingDelete by remember { mutableStateOf<Recipe?>(null) }

    val deletedMessage = stringResource(id = R.string.recipe_deleted)
    val undoLabel = stringResource(id = R.string.undo)

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is UiEvent.Navigate) onNavigate(event.route)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = colorResource(id = R.color.all_background),
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(modifier = Modifier.height(Dimens.SpacingS))
            BannerSticky(id = BannerId.FOUR_BANNER.bannerId)

            if (recipeList.value.isEmpty()) {
                Spacer(modifier = Modifier.height(120.dp))
                Text(
                    text = stringResource(id = R.string.add_recipe),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            } else {
                Spacer(modifier = Modifier.height(Dimens.SpacingS))
                CustomTextInputField(state = textSearch)

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(Dimens.SpacingS),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val filtered = recipeList.value.filter {
                        it.name.contains(textSearch.value.text, ignoreCase = true)
                    }
                    if (filtered.isEmpty()) {
                        item {
                            Text(stringResource(R.string.no_recipes_available_upon_request))
                        }
                    }
                    itemsIndexed(filtered, key = { _, r -> r.id ?: r.name }) { index, recipe ->
                        Spacer(modifier = Modifier.height(Dimens.SpacingS))
                        RowRecipe(
                            recipe = recipe,
                            onEvent = { event ->
                                if (event is RecipeListEvent.DeleteRecipe) {
                                    pendingDelete = event.recipe
                                } else {
                                    viewModel.onEvent(event)
                                }
                            },
                        )
                        // Native ad после каждого пятого рецепта.
                        // adKey зависит от слота, не от номера рецепта — Yandex SDK
                        // переиспользует объявление при ре-композиции одного и того же узла.
                        if ((index + 1) % NATIVE_AD_INTERVAL == 0) {
                            Spacer(modifier = Modifier.height(Dimens.SpacingS))
                            NativeAdRecipeItem(
                                adUnitId = NativeAdUnitId.RECIPE_LIST,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                }
            }
        }
    }

    val toDelete = pendingDelete
    if (toDelete != null) {
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            title = { Text(stringResource(id = R.string.delete_recipe_title)) },
            text = { Text(toDelete.name) },
            confirmButton = {
                TextButton(onClick = {
                    pendingDelete = null
                    viewModel.onEvent(RecipeListEvent.DeleteRecipe(toDelete))
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = deletedMessage,
                            actionLabel = undoLabel,
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onEvent(RecipeListEvent.UndoDelete)
                        }
                    }
                }) {
                    Text(stringResource(id = R.string.delete), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDelete = null }) {
                    Text(stringResource(id = R.string.cancel))
                }
            },
        )
    }
}

@Composable
private fun CustomTextInputField(state: MutableState<TextFieldValue>) {
    OutlinedTextField(
        modifier = Modifier
            .height(60.dp)
            .width(335.dp),
        value = state.value,
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
        ),
        shape = RoundedCornerShape(Dimens.RadiusM),
        maxLines = 1,
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_find),
                contentDescription = stringResource(id = R.string.search_recipes),
                tint = colorResource(id = R.color.grey),
            )
        },
        label = {
            Text(
                text = stringResource(id = R.string.search_recipes),
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(id = R.color.grey),
            )
        },
        onValueChange = { value -> state.value = value },
    )
}

/**
 * Кружок-аватар рецепта: если у рецепта есть фото — показываем его кропом,
 * иначе — заглушку (иконка my_recipe на тонированном фоне), чтобы строка
 * списка не выглядела пустой.
 */
@Composable
private fun RecipeAvatar(recipe: Recipe) {
    val size = 48.dp
    if (recipe.imageUrl.isBlank()) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_my_recipe),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(24.dp),
            )
        }
    } else {
        AsyncImage(
            model = recipe.imageUrl,
            contentDescription = recipe.name,
            modifier = Modifier
                .size(size)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun RowRecipe(
    recipe: Recipe,
    onEvent: (RecipeListEvent) -> Unit,
) {
    Row(
        modifier = Modifier
            .height(56.dp)
            .width(332.dp)
            .clickable {
                onEvent(RecipeListEvent.OnItemClick(Routes.RECIPE_ABOUT_SCREEN + "/${recipe.id}"))
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RecipeAvatar(recipe = recipe)
        Spacer(modifier = Modifier.width(Dimens.SpacingL))
        Box(modifier = Modifier.weight(1f)) {
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        IconButton(onClick = {
            onEvent(RecipeListEvent.OnItemClick(Routes.RECIPE_UPDATE_SCREEN + "/${recipe.id}"))
        }) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_edit_24),
                contentDescription = stringResource(id = R.string.cd_edit),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        IconButton(onClick = { onEvent(RecipeListEvent.DeleteRecipe(recipe)) }) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_trach),
                contentDescription = stringResource(id = R.string.cd_delete),
                tint = MaterialTheme.colorScheme.error,
            )
        }
    }
}
