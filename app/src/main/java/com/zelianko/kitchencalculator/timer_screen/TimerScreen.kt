package com.zelianko.kitchencalculator.timer_screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.data.CookingTime
import com.zelianko.kitchencalculator.subactivity.Timer
import com.zelianko.kitchencalculator.util.GroupProduct
import com.zelianko.kitchencalculator.util.TypeCooking

@Composable
fun TimerScreen(
    paddingValues: PaddingValues,
    viewModel: TimerScreenViewModel = hiltViewModel(),
) {

    val listGroupProduct = listOf(
        TwoField(stringResource(id = R.string.pasta), GroupProduct.PASTA),
        TwoField(stringResource(id = R.string.fish), GroupProduct.FISH_AND_SEAFOOD),
        TwoField(stringResource(id = R.string.meat), GroupProduct.CHICKEN_AND_MEAT),
        TwoField(stringResource(id = R.string.porridge), GroupProduct.PORRIDGE),
        TwoField(stringResource(id = R.string.mushrooms), GroupProduct.MUSHROOM)
    )

    val listTypeCooking = listOf(
        TwoField(stringResource(id = R.string.boiling), TypeCooking.BOILING),
        TwoField(stringResource(id = R.string.fry), TypeCooking.FRY),
        TwoField(stringResource(id = R.string.braise), TypeCooking.BRAISE)
    )

    /**
     * начение выбранного времени приготовления
     */
    val numberTypeCooking = remember {
        mutableStateOf(20)
    }

    /**
     * Продукты с временем
     */
    val productList = viewModel.cookingTimeList.collectAsState()

    /**
     * Выбранный продукт
     */
    val cookingProduct = viewModel.cookingProduct.collectAsState()

    /**
     * Мапа для перевода и получения значений из словаря
     */
    val translateMap = viewModel.mapTranslateProduct

    val cookingTimeTimer = viewModel.cookingTimeForTimer.collectAsState()

    Surface(
        color = colorResource(id = R.color.grey_light),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            //писок групп продуктов
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CustomScrollGroupProductsTabs(
                    listGroupProduct,
                    2
                ) { event ->
                    viewModel.onEvent(event)
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            //Список тип приготовления
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CustomCookingTypeTabs(listTypeCooking, 1, numberTypeCooking)
                { event ->
                    viewModel.onEvent(event)
                }
            }
            Spacer(modifier = Modifier.height(5.dp))

            if (productList.value.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    CustomScrollProductTabs(
                        productList.value,
                        0,
                    ) { event ->
                        viewModel.onEvent(event)
                    }
                }
            }
            Spacer(modifier = Modifier.height(5.dp))

            if (cookingProduct.value.id != null) {
                ResultCardProduct(
                    cookingProduct.value,
                    when (numberTypeCooking.value) {
                        TypeCooking.FRY -> cookingProduct.value.fryTime.toLong() * 1000L
                        TypeCooking.BOILING -> cookingProduct.value.boilingTime.toLong() * 1000L
                        TypeCooking.BRAISE -> cookingProduct.value.braiseTime.toLong() * 1000L
                        else -> {
                            0
                        }
                    }
                )

                Spacer(modifier = Modifier.height(50.dp))

                Box(
                    contentAlignment = Alignment.BottomCenter
                ) {
                    key(cookingTimeTimer.value) {
                        val context = LocalContext.current
                        Timer(
                            totalTime = cookingTimeTimer.value,
                            handleColor = colorResource(id = R.color.orange_primary),
                            inactiveBarColor = Color.DarkGray,
                            activeBarColor = colorResource(id = R.color.orange_primary),
                            modifier = Modifier.size(250.dp),
                            context = context
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomScrollProductTabs(
    list: List<CookingTime>,
    startPosition: Int,
    onEvent: (TimerScreenEvent) -> Unit
) {
    var selectedIndex by remember { mutableStateOf(startPosition) }
    ScrollableTabRow(
        edgePadding = 1.dp,
        selectedTabIndex = selectedIndex,
        containerColor = colorResource(id = R.color.grey_light),
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(25))
            .padding(1.dp),
        indicator = {},
        divider = {}
    ) {
        list.forEachIndexed { index, groupProduct ->
            val selected = selectedIndex == index
            Tab(
                modifier = if (selected) Modifier
                    .padding(2.dp)
                    .clip(RoundedCornerShape(25))
                    .background(
                        colorResource(id = R.color.orange_primary)
                    )
                else Modifier
                    .padding(2.dp)
                    .clip(RoundedCornerShape(25))
                    .background(
                        Color.White
                    ),
                selected = selected,
                onClick = {
                    selectedIndex = index
                    onEvent(TimerScreenEvent.ChooseProduct(groupProduct.id))
                },
                text = { Text(text = groupProduct.productName, color = Color.Black) }
            )
        }
    }
}

@Composable
fun CustomScrollGroupProductsTabs(
    list: List<TwoField>,
    startPosition: Int,
    onEvent: (TimerScreenEvent) -> Unit
) {
    var selectedIndex by remember { mutableStateOf(startPosition) }
    ScrollableTabRow(
        edgePadding = 1.dp,
        selectedTabIndex = selectedIndex,
        containerColor = colorResource(id = R.color.grey_light),
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(25))
            .padding(1.dp),
        indicator = {},
        divider = {}
    ) {
        list.forEachIndexed { index, groupProduct ->
            val selected = selectedIndex == index
            Tab(
                modifier = if (selected) Modifier
                    .padding(2.dp)
                    .clip(RoundedCornerShape(25))
                    .background(
                        colorResource(id = R.color.orange_primary)
                    )
                else Modifier
                    .padding(2.dp)
                    .clip(RoundedCornerShape(25))
                    .background(
                        Color.White
                    ),
                selected = selected,
                onClick = {
                    selectedIndex = index
                    onEvent(TimerScreenEvent.ChooseGroup(groupProduct.key))
                },
                text = { Text(text = groupProduct.name, color = Color.Black) }
            )
        }
    }
}

@Composable
fun CustomCookingTypeTabs(
    list: List<TwoField>,
    startPosition: Int,
    numberTypeCooking: MutableState<Int>,
    onEvent: (TimerScreenEvent) -> Unit
) {
    var selectedIndex by remember { mutableStateOf(startPosition) }
    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = colorResource(id = R.color.grey_light),
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(25))
            .padding(1.dp),
        indicator = {},
        divider = {}
    ) {
        list.forEachIndexed { index, cookingType ->
            val selected = selectedIndex == index
            Tab(
                modifier = if (selected) Modifier
                    .padding(2.dp)
                    .clip(RoundedCornerShape(25))
                    .background(
                        colorResource(id = R.color.orange_primary)
                    )
                else Modifier
                    .padding(2.dp)
                    .clip(RoundedCornerShape(25))
                    .background(
                        Color.White
                    ),
                selected = selected,
                onClick = {
                    selectedIndex = index
                    numberTypeCooking.value = cookingType.key
                    onEvent(TimerScreenEvent.ChooseCookingType(cookingType.key))
                },
                text = { Text(text = cookingType.name, color = Color.Black) }
            )
        }
    }
}


/**
 * Карточка с данными по выбрнаному продукту
 */
@Composable
fun ResultCardProduct(
    product: CookingTime,
    cookingTime: Long,
) {
    val hour = 3600L * 1000L
    val minut = 60L * 1000L

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(horizontal = 30.dp)
            .clip(shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.orange_primary),
            contentColor = Color.Black
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.cooking_time),
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 30.sp
                )
            )
            Text(
                text = String.format(
                    "%s %s", product.productName, String.format(
                        "%02d:%02d:%02d",
                        (cookingTime / hour),
                        (cookingTime % hour) / minut,
                        (cookingTime % minut) / 1000L
                    )
                ),
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )
        }
    }
}

