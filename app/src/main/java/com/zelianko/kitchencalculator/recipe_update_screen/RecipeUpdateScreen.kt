package com.zelianko.kitchencalculator.recipe_update_screen

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.data.ProductEn
import com.zelianko.kitchencalculator.data.Recipe
import com.zelianko.kitchencalculator.util.Routes
import com.zelianko.kitchencalculator.util.UiEvent


@Composable
fun RecipeUpdateScreen(
    viewModel: RecipeUpdateViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    val screenState = viewModel.screenState.collectAsState(RecipeUpdateState.Initial)

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { uiEven ->
            when (uiEven) {
                is UiEvent.Navigate -> {
                    onNavigate(uiEven.route)
                }

                else -> {}
            }
        }
    }

    when (val currentState = screenState.value) {
        is RecipeUpdateState.RecipeDto -> {
            RecipeUpdateCurrentScreen(viewModel, currentState)
        }

        RecipeUpdateState.Initial -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center),
                    color = Color.DarkGray
                )
            }
        }

        RecipeUpdateState.Loading -> {

        }
    }
}


@Composable
fun RecipeUpdateCurrentScreen(
    viewModel: RecipeUpdateViewModel,
    currentState: RecipeUpdateState.RecipeDto,
) {
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                modifier = Modifier
                    .size(48.dp),
                onClick = {
                    viewModel.onEvent(RecipeUpdateEvent.OnItemClick(Routes.RECIPE_LIST_SCREEN))
                })
            {
                Icon(
                    ImageVector.vectorResource(R.drawable.ic_arrow_left),
                    contentDescription = "arrow left"
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(id = R.string.edit_recipe),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .background(
                        colorResource(id = R.color.orange_primary),
                        shape = CircleShape
                    ),
                onClick = {
                    viewModel.onEvent(RecipeUpdateEvent.AddRowProduct)

                }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_plus),
                    contentDescription = "plus"
                )
            }
        }
        Spacer(modifier = Modifier.width(20.dp))

        currentState.recipe?.let {
            RecipeNameTextInputField(
                recipe = it
            ) { event ->
                viewModel.onEvent(event)
            }
        }
        currentState.recipe?.let {
            CardLoadImage(
                recipe = it
            ) { event ->
                viewModel.onEvent(event)
            }
        }
        Spacer(modifier = Modifier.width(40.dp))
        Column(
            modifier = Modifier
                .height(420.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            currentState.products.forEachIndexed { index, ingredient ->
                Spacer(modifier = Modifier.height(12.dp))
                IngredientsRow(
                    index = index,
                    ingredient,
                    currentState.products
                ) { event ->
                    viewModel.onEvent(event)
                }
            }
        }
//        Button(
//            colors = ButtonDefaults.buttonColors(
//                containerColor = colorResource(id = R.color.orange_primary),
//                contentColor = Color.Black
//            ),
//            onClick = {
//                viewModel.onEvent(RecipeUpdateEvent.OnItemSave)
//            }
//        ) {
//            Text(
//                text = stringResource(id = R.string.save_recipe),
//                style = MaterialTheme.typography.titleLarge
//            )
//        }
    }
}

@Composable
fun RecipeNameTextInputField(
    recipe: Recipe,
    onEvent: (RecipeUpdateEvent) -> Unit
) {
    var value by remember {
        mutableStateOf(recipe.name)
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
            onEvent(RecipeUpdateEvent.RecipeNameTextEnter(value))
        }
    )
}


@Composable
fun CardLoadImage(
    recipe: Recipe,
    onEvent: (RecipeUpdateEvent) -> Unit
) {

    var imageUri: Uri? by remember { mutableStateOf(recipe.imageUrl.toUri()) }
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
                onEvent(RecipeUpdateEvent.RecipeImageEnter(imageUri.toString()))
            }
        }
    }
}

@Composable
fun IngredientsRow(
    index: Int,
    productEn: ProductEn,
    listProducts: MutableList<ProductEn>,
    onEvent: (RecipeUpdateEvent) -> Unit,
) {
    var ingredientName by remember {
        mutableStateOf(productEn.name)
    }
    var ingredientWeight by remember {
        mutableStateOf(productEn.mass.toString())
    }

    val measureWeight = remember {
        mutableStateOf(productEn.measureWeight)
    }

    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        productEn.id?.let {
            BottomSheet(
                onDismiss = { showSheet = false },
                measureWeight = measureWeight,
                productId = it
            ) { event ->
                onEvent(event)
            }
        }
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
                .width(225.dp),
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
                productEn.id?.let { RecipeUpdateEvent.IngredientName(ingredientName, it) }
                    ?.let { onEvent(it) }
            }
        )
        Spacer(modifier = Modifier.width(5.dp))
        OutlinedTextField(
            modifier = Modifier
                .height(50.dp)
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
//            label = {
//                Text(
//                    text = "",
//                    style = MaterialTheme.typography.titleSmall,
//                    color = colorResource(id = R.color.grey)
//                )
//            },
            onValueChange = { newText ->
                ingredientWeight = newText
                productEn.id?.let {
                    RecipeUpdateEvent.IngredientWeight(
                        ingredientWeight,
                        it
                    )
                }?.let { onEvent(it) }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.width(5.dp))
        IconButton(
            modifier = Modifier
                .height(40.dp)
                .width(40.dp),
            onClick = {
                showSheet = true
                // onEvent(RecipeAddEvent.DismissItem(index))
            })
        {
            if (measureWeight.value.isBlank()) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "contentDescription",
                    modifier = Modifier
                        .size(18.dp)
                        .background(colorResource(id = R.color.orange_primary), CircleShape)
                        .padding(2.dp),
                    tint = colorResource(id = R.color.orange_primary)
                )
            } else {
                Text(text = measureWeight.value)
            }
        }
        Spacer(modifier = Modifier.width(5.dp))
        if (index == (listProducts.size-1) && index != 0) {
            IconButton(
                modifier = Modifier
                    .height(40.dp)
                    .width(40.dp),
                onClick = {
                    onEvent(RecipeUpdateEvent.DismissItem(productEn))
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    measureWeight: MutableState<String>,
    productId: Long,
    onEvent: (RecipeUpdateEvent) -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        WeightList(
            measureWeight = measureWeight,
            productId = productId
        ) { event ->
            onEvent(event)
        }
    }
}

@Composable
fun WeightList(
    measureWeight: MutableState<String>,
    productId: Long,
    onEvent: (RecipeUpdateEvent) -> Unit
) {
    val countries = listOf(
        stringResource(id = R.string.g),
        stringResource(id = R.string.l),
        stringResource(id = R.string.kg),
        stringResource(id = R.string.sht),
        stringResource(id = R.string.tea_spoon_short),
        stringResource(id = R.string.table_spoon_short),
        stringResource(id = R.string.glass_short),
        stringResource(id = R.string.oz),
        stringResource(id = R.string.packing_short),
        stringResource(id = R.string.pinch),
        stringResource(id = R.string.lb),
        stringResource(id = R.string.drop),
        stringResource(id = R.string.pinta),
        stringResource(id = R.string.gallon),
    )

    LazyColumn {
        items(countries) { it ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 20.dp)
                    .clickable {
                        onEvent(RecipeUpdateEvent.MeasureWeight(it, productId))
                        measureWeight.value = it
                    }
            ) {
                Text(text = it)
            }
        }
    }
}

