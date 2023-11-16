package com.zelianko.kitchencalculator.subactivity

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanyidev.searchable_dropdown.SearchableExpandedDropDownMenu
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.modelview.ProductViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DropListCondition(
) {
    val products = listOf<String>("Грамм", "Столовая ложка", "Чайная ложка")
    val keyboardController = LocalSoftwareKeyboardController.current

    var selectedItem by rememberSaveable {
        mutableStateOf("")
    }
    SearchableExpandedDropDownMenu(
        listOfItems = products,
        modifier = Modifier
            .width(190.dp)
            .padding(start = 15.dp, top = 14.dp),
        placeholder = {
            Text(
                text = "Choose",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 16.sp
            )
        },
        onDropDownItemSelected = { item ->
//            if (Locale.current.language == "ru") {
//                selectedItem = productViewModel.dictionary(item)
//            } else {
//                selectedItem = item
//            }
//            productViewModel.mapProduct.value?.get(selectedItem)
//                ?.let { productViewModel.setCurrentProduct(it) }
            Log.e("DEFAULT_ITEM", selectedItem)
          //  Log.e("DEFAULT_ITEM", productViewModel.currentProduct.value.toString())
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

@Composable
fun DropDownCondition(test: String) {
    Row(
        modifier = Modifier
            .padding(4.dp)
           // .wrapContentSize(),
    ) {
        Text(
            test,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            fontSize = 18.sp
        )
    }
}
