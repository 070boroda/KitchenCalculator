package com.zelianko.kitchencalculator.activity

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.modelview.ProductViewModel
import com.zelianko.kitchencalculator.subactivity.CardFirstScreenTop
import com.zelianko.kitchencalculator.subactivity.CustomTextInput
import com.zelianko.kitchencalculator.subactivity.DropListConditionFrom
import com.zelianko.kitchencalculator.subactivity.DropListConditionTo
import com.zelianko.kitchencalculator.subactivity.DropListProduct
import com.zelianko.kitchencalculator.subactivity.ResultCardProduct
import com.zelianko.kitchencalculator.ui.theme.Dimens
import com.zelianko.kitchencalculator.yandex_ads.BannerId
import com.zelianko.kitchencalculator.yandex_ads.BannerSticky

/**
 * Пересчет массы продуктов.
 */
@RequiresApi(Build.VERSION_CODES.Q)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FirstScreen(
    productViewModel: ProductViewModel,
    paddingValues: PaddingValues,
) {
    val selectedProduct by productViewModel.currentProduct.observeAsState()
    val selectedItemFrom by productViewModel.currentProductFrom.observeAsState("")
    val selectedItemTo by productViewModel.currentProductTo.observeAsState("")
    val resultCount = productViewModel.resultCount.observeAsState()
    val inputText = remember { mutableStateOf("") }

    // Пересчёт — side-effect, нельзя дёргать ViewModel прямо из composition.
    LaunchedEffect(selectedProduct, inputText.value, selectedItemFrom, selectedItemTo) {
        val product = selectedProduct ?: return@LaunchedEffect
        if (inputText.value.isBlank()) return@LaunchedEffect
        if (selectedItemFrom.isBlank() || selectedItemTo.isBlank()) return@LaunchedEffect
        productViewModel.countProductResult(
            product, inputText.value, selectedItemFrom, selectedItemTo
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(colorResource(id = R.color.all_background))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BannerSticky(id = BannerId.ONE_BANNER.bannerId)

        Spacer(modifier = Modifier.height(Dimens.SpacingM))

        // Selector продукта в карточке
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.SpacingL),
            shape = RoundedCornerShape(Dimens.RadiusM),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.input_background),
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                DropListProduct(productViewModel = productViewModel)
            }
        }

        Spacer(modifier = Modifier.height(Dimens.SpacingL))

        // Имя выбранного продукта
        val nameRes = selectedProduct?.keyName
        Text(
            text = if (nameRes != null) stringResource(nameRes)
            else stringResource(id = R.string.choose_product),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(Dimens.SpacingM))

        // Карточки мер — 2x2 сетка, на всю ширину с равными колонками
        val gramSuffix = stringResource(id = R.string.g)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.SpacingL),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingM),
        ) {
            CardFirstScreenTop(
                title = R.string.tea_glass,
                titleGramm = R.string.tea_glass_250,
                value = massString(selectedProduct?.teaGlass, gramSuffix),
                colorCard = colorResource(id = R.color.tea_glass_card),
                modifier = Modifier.weight(1f),
            )
            CardFirstScreenTop(
                title = R.string.table_spoon,
                titleGramm = 0,
                value = massString(selectedProduct?.tableSpoon, gramSuffix),
                colorCard = colorResource(id = R.color.table_spoon_card),
                modifier = Modifier.weight(1f),
            )
        }
        Spacer(modifier = Modifier.height(Dimens.SpacingM))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.SpacingL),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingM),
        ) {
            CardFirstScreenTop(
                title = R.string.faceted_glass,
                titleGramm = R.string.faceted_glass_200,
                value = massString(selectedProduct?.facetedGlass, gramSuffix),
                colorCard = colorResource(id = R.color.faceted_glass_card),
                modifier = Modifier.weight(1f),
            )
            CardFirstScreenTop(
                title = R.string.tea_spoon,
                titleGramm = 0,
                value = massString(selectedProduct?.teaSpoon, gramSuffix),
                colorCard = colorResource(id = R.color.tea_spoon_card),
                modifier = Modifier.weight(1f),
            )
        }

        if (selectedProduct != null) {
            Spacer(modifier = Modifier.height(Dimens.SpacingL))

            // Блок пересчёта: ввод количества + единицы "из"/"в" в одной карточке
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.SpacingL),
                shape = RoundedCornerShape(Dimens.RadiusL),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.card_surface),
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = Dimens.SpacingM,
                        vertical = Dimens.SpacingM,
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingM),
                        verticalAlignment = Alignment.Top,
                    ) {
                        CustomTextInput(
                            inputText = inputText,
                            modifier = Modifier.weight(1f),
                        )
                        DropListConditionFrom(
                            productViewModel = productViewModel,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimens.SpacingM))
                    DropListConditionTo(
                        productViewModel = productViewModel,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingL))

            if (inputText.value.isNotEmpty() && selectedItemFrom.isNotEmpty() && selectedItemTo.isNotEmpty()) {
                ResultCardProduct(resultCount, selectedItemTo)
                Spacer(modifier = Modifier.height(Dimens.SpacingL))
            }
        }
    }
}

private fun massString(value: Int?, suffix: String): String =
    if (value == null) "0" else "$value$suffix"
