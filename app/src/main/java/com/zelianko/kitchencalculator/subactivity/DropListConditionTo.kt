package com.zelianko.kitchencalculator.subactivity

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import com.kanyidev.searchable_dropdown.SearchableExpandedDropDownMenuMaterial3
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.modelview.ProductViewModel
import com.zelianko.kitchencalculator.ui.theme.Dimens

/**
 * Выподающий список конвертировать в
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DropListConditionTo(
    productViewModel: ProductViewModel,
    modifier: Modifier = Modifier,
) {
    val products = listOf(
        stringResource(id = R.string.gramm),
        stringResource(id = R.string.table_spoon),
        stringResource(id = R.string.tea_spoon),
        stringResource(id = R.string.tea_glass),
        stringResource(id = R.string.faceted_glass),
        stringResource(id = R.string.oz)
    )
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.convert_to),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimens.SpacingXs),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
        SearchableExpandedDropDownMenuMaterial3(
            listOfItems = products,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.convert_to),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            onDropDownItemSelected = { item ->
                productViewModel.setCurrentProductTo(item)
                Log.e("DEFAULT_ITEM", Locale.current.language)
            },
            dropdownItem = { drItem ->
                DropDownItem(drItem)
            },
            defaultItem = {},
        )
    }
}
