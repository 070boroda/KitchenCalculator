package com.zelianko.kitchencalculator.recipt_about_screen

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.dialog.DialogEvent
import com.zelianko.kitchencalculator.dialog.MainDialog
import com.zelianko.kitchencalculator.ui.theme.Dimens
import com.zelianko.kitchencalculator.util.Routes
import com.zelianko.kitchencalculator.util.UiEvent
import com.zelianko.kitchencalculator.yandex_ads.BannerId
import com.zelianko.kitchencalculator.yandex_ads.BannerSticky

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeAboutScreen(
    viewModel: RecipeAboutViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit,
) {
    val counter by viewModel.counter.collectAsState(1.0)
    val recipeName by viewModel.recipeName.collectAsState("")
    val uriImage by viewModel.uriImage.collectAsState("")
    val description by viewModel.description.collectAsState("")
    val listProducts by viewModel.productList.collectAsState(initial = emptyList())
    val coff by viewModel.coff.collectAsState()

    val context = LocalContext.current
    val fillRadiusText = stringResource(id = R.string.аill_radius_width_or_height)

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event.route)
                UiEvent.ShowSnackBarIfDiametrOrRowIsEmpty ->
                    Toast.makeText(context, fillRadiusText, Toast.LENGTH_SHORT).show()
                else -> Unit
            }
        }
    }

    MainDialog(dialogController = viewModel)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = recipeName,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.onEvent(
                            RecipeAboutEvent.OnItemClickBackRoute(Routes.RECIPE_LIST_SCREEN)
                        )
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.cd_back),
                        )
                    }
                },
                actions = {
                    IconButton(
                        enabled = listProducts.isNotEmpty(),
                        onClick = {
                            shareRecipe(context, recipeName, description, listProducts)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(id = R.string.cd_share),
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.grey_light),
                ),
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.grey_light))
                    .navigationBarsPadding()
                    .padding(Dimens.SpacingS),
                contentAlignment = Alignment.Center,
            ) {
                PortionCard(
                    counter = counter,
                    coff = coff,
                    onEvent = viewModel::onEvent,
                    onEventDialog = viewModel::onDialogEvent,
                )
            }
        },
        containerColor = colorResource(id = R.color.grey_light),
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Картинка показывается ТОЛЬКО если есть; пустого слота больше нет.
            if (uriImage.isNotBlank()) {
                item {
                    AsyncImage(
                        model = uriImage,
                        contentDescription = stringResource(id = R.string.cd_recipe_photo),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(Dimens.SpacingS))
                BannerSticky(id = BannerId.SIX_BANNER.bannerId)
                Spacer(modifier = Modifier.height(Dimens.SpacingS))
            }
            if (description.isNotBlank()) {
                item {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.SpacingL, vertical = Dimens.SpacingXs),
                    )
                    Spacer(modifier = Modifier.height(Dimens.SpacingS))
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.SpacingL),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(id = R.string.ingredients),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black,
                    )
                }
                Spacer(modifier = Modifier.height(Dimens.SpacingXs))
            }
            items(listProducts, key = { it.name }) { ingredient ->
                IngredientRow(ingredient)
                Spacer(modifier = Modifier.height(Dimens.SpacingXs))
            }
            item {
                // Запас под bottomBar, чтобы последний ингредиент не прятался.
                Spacer(modifier = Modifier.height(180.dp))
            }
        }
    }
}

@Composable
private fun IngredientRow(ingredient: RecipeAboutViewModel.ProductDto) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpacingL),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier.size(28.dp),
            imageVector = ImageVector.vectorResource(R.drawable.icon_ingr_point),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(Dimens.SpacingS))
        Text(
            text = ingredient.name,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = "${formatNumber(ingredient.mass)} ${ingredient.measureWeight}",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black,
        )
        Spacer(modifier = Modifier.width(Dimens.SpacingS))
        Text(
            text = "→ ${formatNumber(ingredient.countMass)}",
            style = MaterialTheme.typography.bodyLarge,
            color = colorResource(id = R.color.grey),
        )
    }
}

private fun formatNumber(value: Double): String =
    if (value % 1.0 == 0.0) value.toInt().toString()
    else value.toBigDecimal().stripTrailingZeros().toPlainString()

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun PortionCard(
    counter: Double,
    coff: Double,
    onEvent: (RecipeAboutEvent) -> Unit,
    onEventDialog: (DialogEvent) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(Dimens.RadiusM),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.SpacingM),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.for_portion),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        modifier = Modifier
                            .background(colorResource(id = R.color.orange_primary), CircleShape),
                        onClick = { onEvent(RecipeAboutEvent.CounterDown) },
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.baseline_keyboard_arrow_down_24),
                            contentDescription = stringResource(id = R.string.cd_decrease_portion),
                        )
                    }
                    Spacer(modifier = Modifier.width(Dimens.SpacingS))
                    Text(
                        text = "X ${formatNumber(counter)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black,
                    )
                    Spacer(modifier = Modifier.width(Dimens.SpacingS))
                    IconButton(
                        modifier = Modifier
                            .background(colorResource(id = R.color.orange_primary), CircleShape),
                        onClick = { onEvent(RecipeAboutEvent.CounterUp) },
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.baseline_keyboard_arrow_up_24),
                            contentDescription = stringResource(id = R.string.cd_increase_portion),
                        )
                    }
                }
            }
            Button(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.orange_primary),
                    contentColor = Color.Black,
                ),
                onClick = { onEventDialog(DialogEvent.OnCancel) },
                shape = RoundedCornerShape(Dimens.RadiusM),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_rectangle),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Blue,
                )
                Spacer(modifier = Modifier.size(Dimens.SpacingS))
                val str = buildAnnotatedString {
                    append(stringResource(id = R.string.form))
                    append(" X ")
                    append(formatNumber(coff))
                }
                Text(text = str, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
