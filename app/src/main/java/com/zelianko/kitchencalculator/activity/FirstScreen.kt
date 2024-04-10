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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.modelview.ProductViewModel
import com.zelianko.kitchencalculator.subactivity.CardFirstScreenTop
import com.zelianko.kitchencalculator.subactivity.CustomTextInput
import com.zelianko.kitchencalculator.subactivity.DropListConditionFrom
import com.zelianko.kitchencalculator.subactivity.DropListConditionTo
import com.zelianko.kitchencalculator.subactivity.DropListProduct
import com.zelianko.kitchencalculator.subactivity.ResultCardProduct

/**
 * Пересчет массы продуктов
 */
@RequiresApi(Build.VERSION_CODES.Q)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FirstScreen(
    productViewModel: ProductViewModel,
    paddingValues: PaddingValues
) {
    val selectedItemString = productViewModel.currentProduct.observeAsState()
    val selectedItemFrom = productViewModel.currentProductFrom.observeAsState("")
    val selectedItemTo = productViewModel.currentProductTo.observeAsState("")
    val inputText = remember { mutableStateOf("") }

    val resultCount = productViewModel.resultCount.observeAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        containerColor = Color(0xFFF8F8F8)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .background(Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 5.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {
                DropListProduct(
                    productViewModel = productViewModel
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val selectedString = selectedItemString.value?.keyName?.let { it1 ->
                    stringResource(
                        it1
                    )
                }
                Text(
                    text = if (selectedString != null) {
                        selectedString
                    } else {
                        stringResource(id = R.string.choose_product)
                    },
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
               horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally)
            ) {

                CardFirstScreenTop(
                    title = R.string.tea_glass,
                    titleGramm = R.string.tea_glass_250,
                    value = if (selectedItemString.value?.teaGlass.toString()
                                    .equals("null")
                           ) "0" else selectedItemString.value?.teaGlass.toString() + stringResource(
                                id = R.string.g
                            ),
                    colorCard = colorResource(
                        id = R.color.tea_glass_card
                    )
                )

                CardFirstScreenTop(
                    title = R.string.table_spoon,
                    titleGramm = 0,
                    value = if (selectedItemString.value?.tableSpoon.toString()
                            .equals("null")
                    ) "0" else selectedItemString.value?.tableSpoon.toString() + stringResource(
                        id = R.string.g
                    ),
                    colorCard = colorResource(
                        id = R.color.table_spoon_card
                    )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally)
            ) {
                CardFirstScreenTop(
                    title = R.string.faceted_glass,
                    titleGramm = R.string.faceted_glass_200,
                    value = if (selectedItemString.value?.facetedGlass.toString()
                            .equals("null")
                    ) "0" else selectedItemString.value?.facetedGlass.toString() + stringResource(
                        id = R.string.g
                    ),
                    colorCard = colorResource(
                        id = R.color.faceted_glass_card
                    )
                )
                CardFirstScreenTop(
                    title = R.string.tea_spoon,
                    titleGramm = 0,
                    value = if (selectedItemString.value?.teaSpoon.toString()
                            .equals("null")
                    ) "0" else selectedItemString.value?.teaSpoon.toString() + stringResource(
                        id = R.string.g
                    ),
                    colorCard = colorResource(
                        id = R.color.tea_spoon_card
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            if (selectedItemString.value != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    CustomTextInput(inputText)
                    Spacer(modifier = Modifier.width(40.dp))
                    DropListConditionFrom(productViewModel)
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                ) {
                    DropListConditionTo(productViewModel)
                }
                Spacer(modifier = Modifier.height(10.dp))
                if (selectedItemString.value != null && inputText.value.isNotEmpty() && selectedItemFrom.value.isNotEmpty() &&
                    selectedItemTo.value.isNotEmpty()
                ) {
                    productViewModel.countProductResult(
                        selectedItemString.value!!, inputText.value,
                        selectedItemFrom.value, selectedItemTo.value
                    )
                    ResultCardProduct(resultCount, selectedItemTo.value)

                }
            }
        }
    }
}
