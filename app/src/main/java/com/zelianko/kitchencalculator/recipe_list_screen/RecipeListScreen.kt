package com.zelianko.kitchencalculator.recipe_list_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.data.Recipe

@Composable
fun RecipeListScreen(
    viewModel: RecipeViewModel = hiltViewModel(),
) {

    val recipeList = viewModel.listRecipe.collectAsState(initial = emptyList())

//    LaunchedEffect(key1 = true) {
//        viewModel.uiEvent.collect { uiEvent ->
//            when (uiEvent) {
//                is UiEvent.Navigate -> {
//                    onNavigate(uiEvent.route)
//                }
//
//                else -> {}
//            }
//        }
//    }

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

        //Передаем евент, что будем делать
        CustomTextInputField() { event ->
            viewModel.onEvent(event)
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(recipeList.value) { recipe ->
                Spacer(modifier = Modifier.height(12.dp))
                RowRecipe(recipe)
            }

        }
    }
}


@Composable
fun CustomTextInputField(
    onEvent: (RecipeListEvent) -> Unit
) {
    var value by remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        modifier = Modifier
            .height(60.dp)
            .width(335.dp),
        value = value,
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
        onValueChange = { newText ->
            value = newText
            onEvent(RecipeListEvent.SearchRecipe(value))
        }
    )
}


@Composable
fun RowRecipe(
    recipe: Recipe
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
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(18.dp))
        Column {
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.titleMedium
            )
//            Text(
//                text = "Ингредиентов на порцию * 2",
//                style = MaterialTheme.typography.bodySmall
//            )
        }
    }
}