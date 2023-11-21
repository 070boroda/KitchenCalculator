package com.zelianko.kitchencalculator.subactivity

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanyidev.searchable_dropdown.SearchableExpandedDropDownMenu
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.modelview.ProductViewModel

/**
 * Выподающий список конвертировать в
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DropListConditionTo(productViewModel: ProductViewModel) {
    val products = listOf(
        stringResource(id = R.string.gramm),
        stringResource(id = R.string.table_spoon),
        stringResource(id = R.string.tea_spoon),
        stringResource(id = R.string.tea_glass),
        stringResource(id = R.string.faceted_glass)
    )
    val keyboardController = LocalSoftwareKeyboardController.current
    val blue = Color(0xFF146AFC)

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .height(50.dp)
            .padding(start = 44.dp, end = 20.dp)
        // .background(Color.Black)
    ) {
        Text(
            text = stringResource(id = R.string.convert_to),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        SearchableExpandedDropDownMenu(
            listOfItems = products,
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.convert_to),
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 19.sp
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
            onSearchTextFieldClicked = {
                keyboardController?.show()
            }
        )
    }
}
