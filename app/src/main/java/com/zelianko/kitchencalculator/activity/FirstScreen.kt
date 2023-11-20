package com.zelianko.kitchencalculator.activity

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.modelview.ProductViewModel
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
        modifier = Modifier.padding(paddingValues)
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_one),
            contentDescription = "background",
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.2f),
            contentScale = ContentScale.FillHeight
        )
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 5.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(colorResource(id = R.color.backgroud_row))
            ) {
                DropListProduct(
                    productViewModel = productViewModel
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(270.dp)
                    .padding(horizontal = 20.dp)
                    .clip(shape = RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = Color(R.color.backgroud_row),
                    contentColor = Color.White
                )

            ) {
                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .height(35.dp),
                    horizontalArrangement = Arrangement.Center

                ) {
                    val selectedString = selectedItemString.value?.keyName?.let { it1 ->
                        stringResource(
                            it1
                        )
                    }
                    if (selectedString != null) {
                        Text(
                            text = selectedString,
                            style = TextStyle(
                                color = colorResource(id = R.color.header_name_product),
                                fontStyle = FontStyle.Italic,
                                fontSize = 30.sp
                            )
                        )
                    } else {
                        Text(
                            text = stringResource(id = R.string.choose_product),
                            style = TextStyle(
                                color = colorResource(id = R.color.header_name_product),
                                fontStyle = FontStyle.Italic,
                                fontSize = 24.sp
                            )
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxHeight()
                            .fillMaxWidth(0.5f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (selectedItemString.value?.teaGlass.toString()
                                    .equals("null")
                            ) "0" else selectedItemString.value?.teaGlass.toString() + stringResource(
                                id = R.string.g
                            ),
                            style = TextStyle(
                                color = Color.Green,
                                fontStyle = FontStyle.Italic,
                                fontSize = 40.sp
                            )
                        )
                        Text(
                            text = stringResource(id = R.string.tea_glass),
                            style = TextStyle(
                                color = Color.Green,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = if (selectedItemString.value?.facetedGlass.toString()
                                    .equals("null")
                            ) "0" else selectedItemString.value?.facetedGlass.toString() + stringResource(
                                id = R.string.g
                            ),
                            style = TextStyle(
                                color = colorResource(id = R.color.faceted_glass),
                                fontStyle = FontStyle.Italic,
                                fontSize = 40.sp
                            )
                        )
                        Text(
                            text = stringResource(id = R.string.faceted_glass),
                            style = TextStyle(
                                color = colorResource(id = R.color.faceted_glass),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (selectedItemString.value?.tableSpoon.toString()
                                    .equals("null")
                            ) "0" else selectedItemString.value?.tableSpoon.toString() + stringResource(
                                id = R.string.g
                            ),
                            style = TextStyle(
                                color = colorResource(id = R.color.table_spoon),
                                fontStyle = FontStyle.Italic,
                                fontSize = 40.sp
                            )
                        )
                        Text(
                            text = stringResource(id = R.string.table_spoon),
                            style = TextStyle(
                                color = colorResource(id = R.color.table_spoon),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = if (selectedItemString.value?.teaSpoon.toString()
                                    .equals("null")
                            ) "0" else selectedItemString.value?.teaSpoon.toString() + stringResource(
                                id = R.string.g
                            ),
                            style = TextStyle(
                                color = colorResource(id = R.color.tea_spoon),
                                fontStyle = FontStyle.Italic,
                                fontSize = 40.sp
                            )
                        )
                        Text(
                            text = stringResource(id = R.string.tea_spoon),
                            style = TextStyle(
                                color = colorResource(id = R.color.tea_spoon),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        )
                    }
                }

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
                    productViewModel.countProductResult(selectedItemString.value!!, inputText.value,
                        selectedItemFrom.value, selectedItemTo.value
                    )
                    ResultCardProduct(resultCount, selectedItemTo.value)

                }
            }
        }
    }
}
