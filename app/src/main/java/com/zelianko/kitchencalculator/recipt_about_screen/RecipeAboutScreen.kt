package com.zelianko.kitchencalculator.recipt_about_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.util.Routes
import com.zelianko.kitchencalculator.util.UiEvent


@Composable
fun RecipeAboutScreen(
    viewModel: RecipeAboutViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {

    val counter = viewModel.counter.collectAsState(1.0)
    val recipeName = viewModel.recipeName.collectAsState("")
    val uriImage = viewModel.uriImage.collectAsState("")
    val listProducts = viewModel.productList.collectAsState(initial = emptyList())


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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.grey_light))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Box(
            modifier = Modifier
                .size(400.dp, 230.dp)
                .background(Color.Transparent),
        ) {
            AsyncImage(
                model = uriImage.value,
                contentDescription = "file",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .fillMaxSize()
            )
            Row(
                modifier = Modifier
                    .size(336.dp, 52.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier
                        .size(50.dp),
                    onClick = {
                        viewModel.onEvent(RecipeAboutEvent.OnItemClickBackRoute(Routes.RECIPE_LIST_SCREEN))
                    })
                {
                    Icon(
                        ImageVector.vectorResource(R.drawable.ic_arrow_left_white),
                        contentDescription = "arrow left",
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(45.dp))
                Text(
                    text = recipeName.value,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(95.dp))
            }
        }
        Spacer(modifier = Modifier.size(10.dp))
        Column(
            modifier = Modifier
                .height(405.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            listProducts.value.forEachIndexed { _, ingredient ->
                Spacer(modifier = Modifier.height(12.dp))
                IngredientRow(ingredient)
            }
        }
        Spacer(modifier = Modifier.size(2.dp))
        UsebleCard(
            counter = counter.value
        ) { event ->
            viewModel.onEvent(event)
        }
    }
}

@Composable
fun IngredientRow(
    ingredient: RecipeAboutViewModel.ProductDto
) {
    Row(
        modifier = Modifier
            .height(37.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(20.dp))
        Image(
            modifier = Modifier.size(35.dp),
            imageVector = ImageVector.vectorResource(R.drawable.icon_ingr_point),
            contentDescription = "icon_ingr_point"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = ingredient.name,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
        Spacer(modifier = Modifier.weight(1F))
        Text(
            text = ingredient.mass.toString(),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = ingredient.measureWeight,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
        Spacer(modifier = Modifier.width(25.dp))
        Text(
            text = ingredient.countMass.toString(),
            style = MaterialTheme.typography.bodyLarge,
            color = colorResource(id = R.color.grey)
        )
        Spacer(modifier = Modifier.width(20.dp))
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun UsebleCard(
    counter: Double,
    onEvent: (RecipeAboutEvent) -> Unit
) {
    Card(
        modifier = Modifier
            .height(166.dp)
            .width(350.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "На порцию",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(55.dp))
                IconButton(
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.orange_primary),
                            shape = CircleShape
                        ),
                    onClick = {
                        //Пересчет Ингредиентов
                        onEvent(RecipeAboutEvent.CounterUp)
                    }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_keyboard_arrow_up_24),
                        contentDescription = "plus"
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "X $counter",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(5.dp))
                IconButton(
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.orange_primary),
                            shape = CircleShape
                        ),
                    onClick = {
                        //Пересчет Ингредиентов
                        onEvent(RecipeAboutEvent.CounterDown)
                    }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_keyboard_arrow_down_24),
                        contentDescription = "plus"
                    )
                }


            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier
                        .height(50.dp)
                        .width(140.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.orange_primary),
                        contentColor = Color.Black
                    ),
                    onClick = {
                        // viewModel.onEvent(RecipeAddEvent.OnItemSave)
                    }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_rectangle),
                        contentDescription = "ic_rectangle",
                        modifier = Modifier.size(25.dp),
                        tint = Color.Blue
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = stringResource(id = R.string.form),
                        style = MaterialTheme.typography.titleLarge
                    )
                }

            }
        }
    }
}