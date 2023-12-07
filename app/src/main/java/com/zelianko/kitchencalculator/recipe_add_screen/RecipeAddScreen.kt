package com.zelianko.kitchencalculator.recipe_add_screen

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.util.Routes
import com.zelianko.kitchencalculator.util.UiEvent


@Composable
fun RecipeAddScreen(
    viewModel: RecipeAddViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    val listProducts = viewModel.listProduct

    //Переход на другой экран
    LaunchedEffect(key1 = true){
        viewModel.uiEvent.collect{ uiEven ->
            when(uiEven){
                is UiEvent.Navigate -> {
                    onNavigate(uiEven.route)
                }
                else -> {}
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.grey_light)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier
                    .size(48.dp),
                onClick = {
                    viewModel.onEvent(RecipeAddEvent.OnItemClick(Routes.RECIPE_LIST_SCREEN))
                })
            {
                Icon(
                    ImageVector.vectorResource(R.drawable.ic_arrow_left),
                    contentDescription = "arrow left"
                )
            }
            Spacer(modifier = Modifier.width(26.dp))
            Text(
                text = stringResource(id = R.string.create_recipe),
                style = MaterialTheme.typography.titleLarge
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        RecipeNameTextInputField() { event ->
            viewModel.onEvent(event)
        }
        CardLoadImage() { event ->
            viewModel.onEvent(event)
        }
        Spacer(modifier = Modifier.width(40.dp))
        LazyColumn(
            modifier = Modifier
                .height(400.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            itemsIndexed(listProducts) { index, ingredient ->
                Spacer(modifier = Modifier.height(12.dp))
                IngredientsRow(
                    index = index
                ) { event ->
                    viewModel.onEvent(event)
                }
            }
        }
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.orange_primary),
                contentColor = Color.Black
            ),
            onClick = {
                viewModel.onEvent(RecipeAddEvent.OnItemSave)
            }
        ) {
            Text(
                text = stringResource(id = R.string.save_recipe),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun RecipeNameTextInputField(
    onEvent: (RecipeAddEvent) -> Unit
) {
    var value by remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        modifier = Modifier
            .height(55.dp)
            .width(335.dp),
        value = value,
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = colorResource(id = R.color.grey_light),
            focusedContainerColor = colorResource(id = R.color.white),
            unfocusedContainerColor = colorResource(id = R.color.grey_light),
            focusedBorderColor = colorResource(id = R.color.grey_light),
            unfocusedBorderColor = colorResource(id = R.color.grey_light)
        ),
        shape = RoundedCornerShape(12.dp),
        maxLines = 1,
        label = {
            Text(
                text = stringResource(id = R.string.recipe_name),
                style = MaterialTheme.typography.titleSmall,
                color = colorResource(id = R.color.grey)
            )
        },
        onValueChange = { newText ->
            value = newText
            onEvent(RecipeAddEvent.RecipeNameTextEnter(value))
        }
    )
}

@Composable
fun CardLoadImage(
    onEvent: (RecipeAddEvent) -> Unit
) {

    var imageUri: Uri? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
            it?.let { uri ->
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                imageUri = uri
            }
        }

    Card(
        modifier = Modifier
            .height(199.dp)
            .width(335.dp)
            .padding(20.dp),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = colorResource(id = R.color.grey)
    ) {

        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    modifier = Modifier
                        .height(101.dp)
                        .width(120.dp),
                    onClick = {
                        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    })
                {
                    Icon(
                        ImageVector.vectorResource(R.drawable.ic_camera_plus),
                        contentDescription = "arrow left",
                        tint = Color.White
                    )
                }
                Text(
                    text = stringResource(id = R.string.add_photo),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                onEvent(RecipeAddEvent.RecipeImageEnter(imageUri.toString()))
            }
        }
    }
}

@Composable
fun IngredientsRow(
    index: Int,
    onEvent: (RecipeAddEvent) -> Unit,
) {
    var ingredientName by remember {
        mutableStateOf("")
    }
    var ingredientWeight by remember {
        mutableStateOf("")
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier
                .height(55.dp)
                .width(235.dp),
            value = ingredientName,
            colors = TextFieldDefaults.colors(
                cursorColor = colorResource(id = R.color.grey_light),
                focusedContainerColor = colorResource(id = R.color.white),
                unfocusedContainerColor = colorResource(id = R.color.grey_light),
                focusedIndicatorColor = colorResource(id = R.color.orange_primary),
                unfocusedIndicatorColor = colorResource(id = R.color.orange_primary),
            ),
            shape = RoundedCornerShape(12.dp),
            maxLines = 1,
            label = {
                Text(
                    text = stringResource(id = R.string.add_ingredient),
                    style = MaterialTheme.typography.titleSmall,
                    color = colorResource(id = R.color.grey)
                )
            },

            onValueChange = { newText ->
                ingredientName = newText
                onEvent(RecipeAddEvent.IngredientName(ingredientName, index))
            }
        )
        Spacer(modifier = Modifier.width(5.dp))
        OutlinedTextField(
            modifier = Modifier
                .height(54.dp)
                .width(67.dp),
            value = ingredientWeight,
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = colorResource(id = R.color.grey_light),
                focusedContainerColor = colorResource(id = R.color.white),
                unfocusedContainerColor = colorResource(id = R.color.grey_light),
                focusedBorderColor = colorResource(id = R.color.grey),
                unfocusedBorderColor = colorResource(id = R.color.grey)
            ),
            shape = RoundedCornerShape(12.dp),
            maxLines = 1,
            label = {
                Text(
                    text = "   " + stringResource(id = R.string.g),
                    style = MaterialTheme.typography.titleSmall,
                    color = colorResource(id = R.color.grey)
                )
            },
            onValueChange = { newText ->
                ingredientWeight = newText
                onEvent(RecipeAddEvent.IngredientWeight(ingredientWeight, index))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.width(5.dp))
        IconButton(
            modifier = Modifier
                .height(40.dp)
                .width(40.dp),
            onClick = {
                onEvent(RecipeAddEvent.AddRowProduct)
            })
        {
            Icon(
                ImageVector.vectorResource(R.drawable.ic_plus),
                contentDescription = "plus",
                tint = colorResource(id = R.color.grey)
            )
        }
        IconButton(
            modifier = Modifier
                .height(40.dp)
                .width(40.dp),
            onClick = {
                onEvent(RecipeAddEvent.DismissItem(index))
            })
        {
            Icon(
                ImageVector.vectorResource(R.drawable.ic_trach),
                contentDescription = "plus",
                tint = Color.Red
            )
        }
    }
}

