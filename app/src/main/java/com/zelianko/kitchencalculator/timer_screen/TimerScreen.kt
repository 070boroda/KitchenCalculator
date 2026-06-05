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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.data.CookingTime
import com.zelianko.kitchencalculator.subactivity.Timer
import com.zelianko.kitchencalculator.ui.theme.Dimens
import com.zelianko.kitchencalculator.util.GroupProduct
import com.zelianko.kitchencalculator.util.TypeCooking
import com.zelianko.kitchencalculator.yandex_ads.BannerId
import com.zelianko.kitchencalculator.yandex_ads.BannerSticky

@Composable
fun TimerScreen(
    paddingValues: PaddingValues,
//    billingViewModel: BillingViewModel,
    viewModel: TimerScreenViewModel = hiltViewModel(),
) {

    val isActiveSub = false
//    val isActiveSub = billingViewModel.isActiveSub.observeAsState()

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

    // Текущий выбор пользователя (поднят в VM, переживает навигацию).
    val selectedGroupKey by viewModel.selectedGroupKey.collectAsState()
    val selectedTypeKey by viewModel.selectedTypeKey.collectAsState()
    val selectedProductId by viewModel.selectedProductId.collectAsState()

    // Состояние фонового таймера — единый источник правды, переживает уход с экрана.
    val timerState by TimerController.state.collectAsState()
    // Таймер активен (идёт или на паузе, но не завершён) — выбор заблокирован.
    val locked = timerState.totalMillis > 0L && !timerState.isFinished

    // При возврате на экран с работающим таймером — восстанавливаем те же chips.
    LaunchedEffect(locked, timerState.productId) {
        if (locked) {
            viewModel.restoreSelectionFromTimer(
                groupKey = timerState.groupKey,
                cookingTypeKey = timerState.cookingTypeKey,
                productId = timerState.productId,
            )
        }
    }

    // Индексы для подсветки chips по выбранным ключам.
    val groupIndex = listGroupProduct.indexOfFirst { it.key == selectedGroupKey }
        .let { if (it < 0) 0 else it }
    val typeIndex = listTypeCooking.indexOfFirst { it.key == selectedTypeKey }
        .let { if (it < 0) 0 else it }
    val productIndex = productList.value.indexOfFirst { it.id == selectedProductId }
        .let { if (it < 0) 0 else it }

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

            if (isActiveSub == false) {
                BannerSticky(id = BannerId.SEVEN_BANNER.bannerId)
//                GoogleBannerAd(textId = StringConstants.BannerTimerScreenId)
            }
            //список групп продуктов
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CustomScrollGroupProductsTabs(
                    list = listGroupProduct,
                    selectedIndex = groupIndex,
                    enabled = !locked,
                    ringtone = ringtone,
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
                CustomCookingTypeTabs(
                    list = listTypeCooking,
                    selectedIndex = typeIndex,
                    enabled = !locked,
                    ringtone = ringtone,
                ) { event ->
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
                        list = productList.value,
                        selectedIndex = productIndex,
                        enabled = !locked,
                        translateMap = translateMap,
                        ringtone = ringtone,
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
                    when (selectedTypeKey) {
                        TypeCooking.FRY -> cookingProduct.value.fryTime.toLong() * 1000L
                        TypeCooking.BOILING -> cookingProduct.value.boilingTime.toLong() * 1000L
                        TypeCooking.BRAISE -> cookingProduct.value.braiseTime.toLong() * 1000L
                        else -> {
                            0
                        }
                    },
                    translateMap
                )

                Spacer(modifier = Modifier.height(40.dp))

                if (cookingTimeTimer.value != 0L) {
                    Box(
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        key(cookingTimeTimer.value) {
                            Timer(
                                totalTime = cookingTimeTimer.value,
                                handleColor = colorResource(id = R.color.orange_primary),
                                inactiveBarColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                activeBarColor = colorResource(id = R.color.orange_primary),
                                modifier = Modifier.size(260.dp),
                                ringtone = ringtone,
                                context = context,
                                groupKey = selectedGroupKey,
                                cookingTypeKey = selectedTypeKey,
                                productId = cookingProduct.value.id,
                            )
                        }
                    }
                } else {
                    InfoMessageCard(
                        text = stringResource(id = R.string.not_find_time_for_cooking),
                        containerColor = colorResource(id = R.color.header_product_result),
                    )
                }
            } else {
                InfoMessageCard(
                    text = stringResource(id = R.string.please_tap_to_product),
                    containerColor = colorResource(id = R.color.tea_spoon_card),
                )
            }
        }
    }
}

@Composable
fun CustomScrollProductTabs(
    list: List<CookingTime>,
    selectedIndex: Int,
    enabled: Boolean,
    translateMap: HashMap<Int, Int>,
    ringtone: Ringtone,
    onEvent: (TimerScreenEvent) -> Unit
) {
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

            TimerChipTab(
                text = nameProduct!!,
                selected = selected,
                enabled = enabled,
                onClick = {
                    onEvent(TimerScreenEvent.ChooseProduct(groupProduct.id))
                    ringtone.stop()
                },
            )
        }
    }
}

@Composable
fun CustomScrollGroupProductsTabs(
    list: List<TwoField>,
    selectedIndex: Int,
    enabled: Boolean,
    ringtone: Ringtone,
    onEvent: (TimerScreenEvent) -> Unit
) {
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
            TimerChipTab(
                text = groupProduct.name,
                selected = selected,
                enabled = enabled,
                onClick = {
                    onEvent(TimerScreenEvent.ChooseGroup(groupProduct.key))
                    ringtone.stop()
                },
            )
        }
    }
}

@Composable
fun CustomCookingTypeTabs(
    list: List<TwoField>,
    selectedIndex: Int,
    enabled: Boolean,
    ringtone: Ringtone,
    onEvent: (TimerScreenEvent) -> Unit
) {
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
            TimerChipTab(
                text = cookingType.name,
                selected = selected,
                enabled = enabled,
                onClick = {
                    onEvent(TimerScreenEvent.ChooseCookingType(cookingType.key))
                    ringtone.stop()
                },
            )
        }
    }
}

/**
 * Единый chip-таб таймер-экрана. Когда [enabled] = false (таймер крутится),
 * невыбранные chips затемняются и не реагируют на клик.
 */
@Composable
private fun TimerChipTab(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val containerColor = if (selected) {
        colorResource(id = R.color.orange_primary)
    } else {
        colorResource(id = R.color.card_surface)
    }
    // Заблокированные и невыбранные chips приглушаем, выбранный остаётся ярким.
    val tabAlpha = if (!enabled && !selected) 0.4f else 1f
    Tab(
        modifier = Modifier
            .padding(2.dp)
            .clip(RoundedCornerShape(25))
            .alpha(tabAlpha)
            .background(containerColor),
        selected = selected,
        enabled = enabled || selected,
        onClick = { if (enabled) onClick() },
        text = {
            Text(
                text = text,
                color = if (selected) Color.Black else MaterialTheme.colorScheme.onSurface
            )
        }
    )
}


/**
 * Карточка-сообщение (пустые состояния таймера): крупный текст по центру
 * на тонированном фоне, единое скругление и тень.
 */
@Composable
private fun InfoMessageCard(
    text: String,
    containerColor: Color,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = Dimens.SpacingXl),
        shape = RoundedCornerShape(Dimens.RadiusL),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = Color.Black,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.SpacingXl, horizontal = Dimens.SpacingL),
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * Карточка с данными по выбрнаному продукту — акцентный брендовый блок.
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
            .padding(horizontal = Dimens.SpacingXl),
        shape = RoundedCornerShape(Dimens.RadiusL),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        val idStringResource = translateMap.get(product.id!!.toInt())
        var nameProduct = idStringResource?.let { stringResource(id = it) }

        if (nameProduct!!.contains("(")) {
            nameProduct = nameProduct.substring(0, nameProduct.indexOf("("))
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.SpacingXl, horizontal = Dimens.SpacingL),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingXs, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.cooking_time),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
            )
            Text(
                text = String.format("%s", nameProduct),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
            )
        }
    }
}
