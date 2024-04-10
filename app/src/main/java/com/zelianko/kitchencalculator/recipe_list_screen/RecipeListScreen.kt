package com.zelianko.kitchencalculator.recipe_list_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.zelianko.kitchencalculator.constants.StringConstants
import com.zelianko.kitchencalculator.constants.StringConstants.Companion.MONTHLY
import com.zelianko.kitchencalculator.data.Recipe
import com.zelianko.kitchencalculator.google_ads.GoogleBannerAd
import com.zelianko.kitchencalculator.util.Routes
import com.zelianko.kitchencalculator.util.UiEvent

@Composable
fun RecipeListScreen(
    viewModel: RecipeViewModel = hiltViewModel(),
    currentSubscriptionList: List<String>,
    onNavigate: (String) -> Unit
) {

    val recipeList = viewModel.listRecipe.collectAsState(initial = emptyList())
    val textSearch = remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.Navigate -> {
                    onNavigate(uiEvent.route)
                }

                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 63.dp)
            .background(colorResource(id = R.color.all_background)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        if (!currentSubscriptionList.contains(MONTHLY)) {
            GoogleBannerAd(textId = StringConstants.BannerListRecipeId)
        }

        //Передаем евент, что будем делать
        CustomTextInputField(
            state = textSearch
        )
//TODO нужно попробовать передать ивент во вьюмодел и от туда обрезать список
//        if (!currentSubscriptionList.contains(MONTHLY)) {
//            recipeList.value.subList(0, 2);
//        }

        //Если нет подписки и список рецептов больше трех выводим только три рецепта
        if (!currentSubscriptionList.contains(MONTHLY) && recipeList.value.size >= 5) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val tempList = recipeList.value.subList(0, 4);
                items(tempList.filter {
                    it.name.contains(textSearch.value.text, ignoreCase = true)
                }
                ) { recipe ->
                    Spacer(modifier = Modifier.height(12.dp))
                    RowRecipe(recipe) { event ->
                        viewModel.onEvent(event)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(recipeList.value.filter {
                    it.name.contains(textSearch.value.text, ignoreCase = true)
                }
                ) { recipe ->
                    Spacer(modifier = Modifier.height(12.dp))
                    RowRecipe(recipe) { event ->
                        viewModel.onEvent(event)
                    }
                }
            }
        }
    }
}


@Composable
fun CustomTextInputField(
    state: MutableState<TextFieldValue>,
) {
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
            unfocusedBorderColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        maxLines = 1,
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_find),
                contentDescription = "find",
                tint = colorResource(id = R.color.grey)
            )
        },
        label = {
            Text(
                text = stringResource(id = R.string.search_recipes),
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(id = R.color.grey)
            )
        },
        onValueChange = { value ->
            state.value = value
        }
    )
}


@Composable
fun RowRecipe(
    recipe: Recipe,
    onEvent: (RecipeListEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .height(50.dp)
            .width(332.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = recipe.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .clickable {
                    onEvent(RecipeListEvent.OnItemClick(Routes.RECIPE_ABOUT_SCREEN + "/${recipe.id}"))
                },
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(18.dp))
        Column {
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(
            Modifier
                .weight(1f)
                .fillMaxHeight())
        IconButton(onClick = {
            Routes.RECIPE_UPDATE_SCREEN + "/${recipe.id}"
            onEvent(RecipeListEvent.OnItemClick(Routes.RECIPE_UPDATE_SCREEN + "/${recipe.id}"))
        }) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_edit_24),
                contentDescription = "update",
                tint = Color.Green,
            )
        }

        IconButton(onClick = {
            onEvent(RecipeListEvent.DeleteRecipe(recipe))
        }) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_trach),
                contentDescription = "trash",
                tint = Color.Red,
            )
        }
    }
}