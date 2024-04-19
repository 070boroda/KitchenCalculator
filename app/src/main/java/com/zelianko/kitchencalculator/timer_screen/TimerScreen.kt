package com.zelianko.kitchencalculator.timer_screen

import android.media.Ringtone
import android.media.RingtoneManager
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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.constants.StringConstants
import com.zelianko.kitchencalculator.data.CookingTime
import com.zelianko.kitchencalculator.google_ads.GoogleBannerAd
import com.zelianko.kitchencalculator.subactivity.Timer
import com.zelianko.kitchencalculator.subscriptions.BillingViewModel
import com.zelianko.kitchencalculator.util.GroupProduct
import com.zelianko.kitchencalculator.util.TypeCooking

@Composable
fun TimerScreen(
    paddingValues: PaddingValues,
    billingViewModel: BillingViewModel,
    viewModel: TimerScreenViewModel = hiltViewModel(),
) {

    val isActiveSub = billingViewModel.isActiveSub.observeAsState()

    val listGroupProduct = listOf(
        TwoField(stringResource(id = R.string.pasta), GroupProduct.PASTA),
        TwoField(stringResource(id = R.string.fish), GroupProduct.FISH_AND_SEAFOOD),
        TwoField(stringResource(id = R.string.meat), GroupProduct.CHICKEN_AND_MEAT),
        TwoField(stringResource(id = R.string.porridge), GroupProduct.PORRIDGE),
        TwoField(stringResource(id = R.string.mushrooms), GroupProduct.MUSHROOM),
        TwoField(stringResource(id = R.string.vegetables), GroupProduct.VEGETABLES)
    )

    val listTypeCooking = listOf(
        TwoField(stringResource(id = R.string.boiling), TypeCooking.BOILING),
        TwoField(stringResource(id = R.string.fry), TypeCooking.FRY),
        TwoField(stringResource(id = R.string.braise), TypeCooking.BRAISE)
    )
    val context = LocalContext.current
    val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    val ringtone = RingtoneManager.getRingtone(context, notification)

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

            if (isActiveSub.value == false) {
                GoogleBannerAd(textId = StringConstants.BannerTimerScreenId)
            }
            //список групп продуктов
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CustomScrollGroupProductsTabs(
                    listGroupProduct,
                    2,
                    ringtone
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
                CustomCookingTypeTabs(listTypeCooking, 1, numberTypeCooking, ringtone)
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
                        translateMap,
                        ringtone
                    ) { event ->
                        viewModel.onEvent(event)
                    }
                }
            }
            Spacer(modifier = Modifier.height(5.dp))

            //Если есть продукт и есть или нет время выводим этот блок
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
                    },
                    translateMap
                )

                Spacer(modifier = Modifier.height(50.dp))

                if (cookingTimeTimer.value != 0L) {
                    Box(
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        key(cookingTimeTimer.value) {
                            Timer(
                                totalTime = cookingTimeTimer.value,
                                handleColor = colorResource(id = R.color.orange_primary),
                                inactiveBarColor = Color.DarkGray,
                                activeBarColor = colorResource(id = R.color.orange_primary),
                                modifier = Modifier.size(250.dp),
                                ringtone = ringtone,
                                context = context
                            )
                        }
                    }
                } else{
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 30.dp)
                            .clip(shape = RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = colorResource(id = R.color.header_product_result),
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.not_find_time_for_cooking),
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 30.dp)
                        .clip(shape = RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.tea_spoon_card),
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.please_tap_to_product),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        textAlign = TextAlign.Center
                    )
                }

            }
        }
    }
}

@Composable
fun CustomScrollProductTabs(
    list: List<CookingTime>,
    startPosition: Int,
    translateMap: HashMap<Int, Int>,
    ringtone: Ringtone,
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

            val idStringResource = translateMap.get(groupProduct.id!!.toInt())
            val nameProduct = idStringResource?.let { stringResource(id = it) }


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
                    ringtone.stop()
                },
                text = { Text(text = nameProduct!!, color = Color.Black) }
            )
        }
    }
}

@Composable
fun CustomScrollGroupProductsTabs(
    list: List<TwoField>,
    startPosition: Int,
    ringtone: Ringtone,
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
                    ringtone.stop()
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
    ringtone: Ringtone,
    onEvent: (TimerScreenEvent) -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(startPosition) }
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
                    ringtone.stop()
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
    translateMap: HashMap<Int, Int>,
) {

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
        val idStringResource = translateMap.get(product.id!!.toInt())
        var nameProduct = idStringResource?.let { stringResource(id = it) }

        if (nameProduct!!.contains("(")) {
            nameProduct = nameProduct.substring(0, nameProduct.indexOf("("))
        }
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
                    "%s", nameProduct
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

