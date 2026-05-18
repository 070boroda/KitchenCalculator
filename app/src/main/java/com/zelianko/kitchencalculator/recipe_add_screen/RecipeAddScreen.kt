package com.zelianko.kitchencalculator.recipe_add_screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.subactivity.IngredientCard
import com.zelianko.kitchencalculator.subactivity.RecipeImagePicker
import com.zelianko.kitchencalculator.ui.theme.Dimens
import com.zelianko.kitchencalculator.util.Routes
import com.zelianko.kitchencalculator.util.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeAddScreen(
    viewModel: RecipeAddViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val nameRecipeIsEmpty = stringResource(id = R.string.name_recipt_is_empty)
    val productIsEmpty = stringResource(id = R.string.product_is_empty)
    val savedMessage = stringResource(id = R.string.recipe_saved)

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event.route)
                UiEvent.ShowSnackBarIfNameRecipeIsEmpty ->
                    Toast.makeText(context, nameRecipeIsEmpty, Toast.LENGTH_SHORT).show()
                UiEvent.ShowSnackBarIfNameProductIsEmpty,
                UiEvent.ShowSnackBarIfProductRowIsEmpty ->
                    Toast.makeText(context, productIsEmpty, Toast.LENGTH_SHORT).show()
                UiEvent.RecipeSaved ->
                    Toast.makeText(context, savedMessage, Toast.LENGTH_SHORT).show()
                else -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.create_recipe),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.onEvent(RecipeAddEvent.OnItemClick(Routes.RECIPE_LIST_SCREEN))
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.cd_back),
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.grey_light),
                ),
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.grey_light))
                    .navigationBarsPadding()
                    .padding(horizontal = Dimens.SpacingL, vertical = Dimens.SpacingS),
                horizontalArrangement = Arrangement.Center,
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isSaving,
                    onClick = { viewModel.onEvent(RecipeAddEvent.OnItemSave) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.orange_primary),
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    shape = RoundedCornerShape(Dimens.RadiusM),
                ) {
                    Text(
                        text = stringResource(id = R.string.save_recipe),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = colorResource(id = R.color.grey_light),
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Spacer(modifier = Modifier.height(Dimens.SpacingS))
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { viewModel.onEvent(RecipeAddEvent.RecipeNameTextEnter(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.SpacingL, vertical = Dimens.SpacingS),
                    label = {
                        Text(
                            text = stringResource(id = R.string.recipe_name),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(Dimens.RadiusM),
                )
            }
            item {
                RecipeImagePicker(
                    initialUri = state.imageUri,
                    onImagePicked = { viewModel.onEvent(RecipeAddEvent.RecipeImageEnter(it)) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            item {
                OutlinedTextField(
                    value = state.description,
                    onValueChange = {
                        viewModel.onEvent(RecipeAddEvent.RecipeDescriptionEnter(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.SpacingL, vertical = Dimens.SpacingS),
                    label = {
                        Text(
                            text = stringResource(id = R.string.description_hint),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    },
                    minLines = 3,
                    maxLines = 6,
                    shape = RoundedCornerShape(Dimens.RadiusM),
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.SpacingL, vertical = Dimens.SpacingS),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(id = R.string.add_ingredient),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = Dimens.SpacingXs),
                    )
                }
            }

            items(
                items = state.products,
                key = { it.uiId },
            ) { draft ->
                val index = state.products.indexOf(draft)
                IngredientCard(
                    draft = draft,
                    onNameChange = { viewModel.onEvent(RecipeAddEvent.IngredientName(it, index)) },
                    onAmountChange = { viewModel.onEvent(RecipeAddEvent.IngredientWeight(it, index)) },
                    onUnitChange = { viewModel.onEvent(RecipeAddEvent.MeasureWeight(it, index)) },
                    onDelete = { viewModel.onEvent(RecipeAddEvent.DismissItem(index)) },
                )
            }

            item {
                OutlinedButton(
                    onClick = { viewModel.onEvent(RecipeAddEvent.AddRowProduct) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.SpacingL, vertical = Dimens.SpacingS),
                    shape = RoundedCornerShape(Dimens.RadiusM),
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.size(Dimens.SpacingXs))
                    Text(stringResource(id = R.string.add_product_action))
                }
                Spacer(modifier = Modifier.height(Dimens.SpacingL))
            }
        }
    }
}
