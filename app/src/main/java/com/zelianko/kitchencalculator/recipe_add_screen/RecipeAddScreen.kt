package com.zelianko.kitchencalculator.recipe_add_screen

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.subscriptions.BillingViewModel
import com.zelianko.kitchencalculator.util.Routes
import com.zelianko.kitchencalculator.util.UiEvent
import com.zelianko.kitchencalculator.yandex_ads.BannerId
import com.zelianko.kitchencalculator.yandex_ads.BannerSticky


@Composable
fun RecipeAddScreen(
    viewModel: RecipeAddViewModel = hiltViewModel(),
    billingViewModel: BillingViewModel,
    onNavigate: (String) -> Unit
) {
    val listProducts = viewModel.listProduct

    val snackState = remember { SnackbarHostState() }

    val context = LocalContext.current
    val nameReciptIsEmpty = stringResource(id = R.string.name_recipt_is_empty)
    val productIsEmpty = stringResource(id = R.string.product_is_empty)
    val isActiveSub = billingViewModel.isActiveSub.observeAsState()


    SnackbarHost(hostState = snackState, Modifier)
    //Переход на другой экран
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { uiEven ->
            when (uiEven) {
                is UiEvent.Navigate -> {
                    onNavigate(uiEven.route)
                }

                is UiEvent.ShowSnackBarIfNameRecipeIsEmpty -> {
                    Toast.makeText(context, nameReciptIsEmpty, Toast.LENGTH_SHORT).show()
                }

                is UiEvent.ShowSnackBarIfNameProductIsEmpty -> {
                    Toast.makeText(context, productIsEmpty, Toast.LENGTH_SHORT).show()
                }

                is UiEvent.ShowSnackBarIfProductRowIsEmpty -> {
                    Toast.makeText(context, productIsEmpty, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                backgroundColor = colorResource(id = R.color.grey_light),
                cutoutShape = CircleShape,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.Center
                ) {
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
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .background(colorResource(id = R.color.grey_light))
                    .verticalScroll(rememberScrollState()),
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
                    Spacer(modifier = Modifier.weight(1f))

                    //Если нет подписки и рецептов базе больше или равно пяти не даем создавать
                    //больше
//                    if (isActiveSub.value == false && listProducts.size >= 5) {
//
//                    } else {
                    IconButton(
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .background(
                                colorResource(id = R.color.orange_primary),
                                shape = CircleShape
                            ),
                        onClick = {
                            viewModel.onEvent(RecipeAddEvent.AddRowProduct)

                        }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_plus),
                            contentDescription = "plus"
                        )
                    }
//                    }
                }
                Spacer(modifier = Modifier.width(20.dp))
                if (isActiveSub.value == false) {
                    BannerSticky(id = BannerId.THREE_BANNER.bannerId)
//                    GoogleBannerAd(textId = StringConstants.BannerAddRecipeId)
                }
                RecipeNameTextInputField() { event ->
                    viewModel.onEvent(event)
                }
                CardLoadImage() { event ->
                    viewModel.onEvent(event)
                }
                Spacer(modifier = Modifier.width(40.dp))
                Column(
                    modifier = Modifier
                        .height(380.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    listProducts.forEachIndexed { index, ingredient ->
                        Spacer(modifier = Modifier.height(12.dp))
                        IngredientsRow(
                            index = index,
                            listProducts = listProducts
                        ) { event ->
                            viewModel.onEvent(event)
                        }
                    }
                }

            }

        }
    )
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
    listProducts: List<ThreeField<String>>,
    onEvent: (RecipeAddEvent) -> Unit,
) {
    var ingredientName by remember {
        mutableStateOf("")
    }
    var ingredientWeight by remember {
        mutableStateOf("")
    }

    val measureWeight = remember {
        mutableStateOf("")
    }

    var showSheet by remember { mutableStateOf(false) }

    val pattern = remember { Regex("[\\d]*[.]?[\\d]*") }

    if (showSheet) {
        BottomSheet(
            onDismiss = { showSheet = false },
            measureWeight = measureWeight,
            index = index
        ) { event ->
            onEvent(event)
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
                .width(200.dp),
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
            onValueChange = { newText ->
                if (newText.isEmpty() || newText.matches(pattern)) {
                    ingredientWeight = newText
                    onEvent(RecipeAddEvent.IngredientWeight(ingredientWeight, index))
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.width(5.dp))
        IconButton(
            modifier = Modifier
                .height(40.dp)
                .width(60.dp),
            onClick = {
                showSheet = true
            })
        {
            if (measureWeight.value.isBlank()) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "contentDescription",
                    modifier = Modifier
                        .size(23.dp)
                        .background(colorResource(id = R.color.orange_primary), CircleShape)
                        .padding(2.dp),
                    tint = colorResource(id = R.color.orange_primary)
                )
            } else {
                Text(text = measureWeight.value)
            }
        }
        Spacer(modifier = Modifier.width(5.dp))
        if (index == (listProducts.size - 1) && index != 0) {
            IconButton(
                modifier = Modifier
                    .height(40.dp)
                    .width(40.dp),
                onClick = {
                    onEvent(RecipeAddEvent.DismissItem)
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
    index: Int,
    onEvent: (RecipeAddEvent) -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        WeightList(
            measureWeight = measureWeight,
            index = index
        ) { event ->
            onEvent(event)
        }
    }
}


@Composable
fun WeightList(
    measureWeight: MutableState<String>,
    index: Int,
    onEvent: (RecipeAddEvent) -> Unit
) {
    val countries = listOf(
        stringResource(id = R.string.g),
        stringResource(id = R.string.l),
        stringResource(id = R.string.ml),
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
                        onEvent(RecipeAddEvent.MeasureWeight(it, index))
                        measureWeight.value = it
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (measureWeight.value == it) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "contentDescription",
                        modifier = Modifier
                            .size(14.dp)
                            .background(colorResource(id = R.color.orange_primary), CircleShape)
                            .padding(2.dp),
                        tint = colorResource(id = R.color.orange_primary)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                }
                Text(
                    text = it,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 20.sp
                    )
                )
            }
        }
    }
}
