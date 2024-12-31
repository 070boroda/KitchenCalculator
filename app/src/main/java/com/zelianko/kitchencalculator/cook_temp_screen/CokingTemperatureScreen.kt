package com.zelianko.kitchencalculator.cook_temp_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.ads.BannerSticky
import com.zelianko.kitchencalculator.subscriptions.BillingViewModel

/**
 * Экран температура приготовления продуктов
 */
@Composable
fun CokingTemperatureScreen(
    paddingValues: PaddingValues,
    billingViewModel: BillingViewModel
) {
    val isActiveSub = billingViewModel.isActiveSub.observeAsState()

    val dataCookingTempLists = listOf(
        DataCookingTemp(R.drawable.bull, stringResource(id = R.string.beef_veal_lamb), "     ", "  ", true),
        DataCookingTemp(R.drawable.bull, stringResource(id = R.string.rare_blood), "52-55", "58", false),
        DataCookingTemp(R.drawable.bull, stringResource(id = R.string.medium_rare), "55-60", "63", false),
        DataCookingTemp(R.drawable.bull, stringResource(id = R.string.medium), "60-65", "70", false),
        DataCookingTemp(R.drawable.bull, stringResource(id = R.string.well_done), "70", "75", false),

        DataCookingTemp(R.drawable.pig_temp, stringResource(id = R.string.pork_temp), "     ", "  ", true),
        DataCookingTemp(R.drawable.pig_temp, stringResource(id = R.string.medium), "60-65", "70", false),
        DataCookingTemp(R.drawable.pig_temp, stringResource(id = R.string.well_done), "70", "75", false),

        DataCookingTemp(R.drawable.chicken, stringResource(id = R.string.chicken), "     ", "  ", true),
        DataCookingTemp(R.drawable.chicken, stringResource(id = R.string.whole_baked), "72-75", "82", false),
        DataCookingTemp(R.drawable.chicken, stringResource(id = R.string.breast), "68-70", "75", false),
        DataCookingTemp(R.drawable.chicken, stringResource(id = R.string.legs), "75", "82", false),

        DataCookingTemp(R.drawable.duck, stringResource(id = R.string.goose_duck), "     ", "  ", true),
        DataCookingTemp(R.drawable.duck, stringResource(id = R.string.whole_baked), "68-70", "75", false),
        DataCookingTemp(R.drawable.duck, stringResource(id = R.string.breast), "60-65", "70", false),

        DataCookingTemp(R.drawable.fish, stringResource(id = R.string.fish), "    ", "  ", true),
        DataCookingTemp(R.drawable.fish, stringResource(id = R.string.fish), "60", "63", false),
        )

    val column1Weight = 3f
    val column2Weight = 2.6f
    val column3Weight = 2f
    val column4Weight = 2f
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = colorResource(id = R.color.grey_light)
    ) {
        LazyColumn(Modifier.padding(8.dp)) {
            item {
                BannerSticky(id = "R-M-13532950-1")
//                if (isActiveSub.value == false) {
//                    GoogleBannerAd(textId = StringConstants.BannerTimeCookingScreenId)
//                }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TableCell(
                        text = stringResource(R.string.product),
                        weight = column1Weight,
                        alignment = TextAlign.Left,
                        title = true
                    )
                    //TableCell(text = "Date", weight = column2Weight, title = true)
                    TableCell(
                        text = stringResource(R.string.temp_before_rest),
                        weight = column3Weight,
                        title = true
                    )
                    TableCell(
                        text = stringResource(R.string.temp_after_rest),
                        weight = column4Weight,
                        alignment = TextAlign.Right,
                        title = true
                    )
                }
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxHeight()
                        .fillMaxWidth()
                )
            }

            itemsIndexed(dataCookingTempLists) { _, invoice ->
                if (invoice.isTitle) {
                    //Заголовок
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = invoice.nameProduct,
                            Modifier
                                .padding(10.dp),
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(
                                fontSize = 15.sp
                            )
                        )
                    }
                } else {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Image(
                            imageVector = ImageVector.vectorResource(invoice.icon),
                            contentDescription = "Android"
                        )
//                    TableCell(
//                        text = invoice.invoice,
//                        weight = column1Weight,
//                        alignment = TextAlign.Left
//                    )
                        TableCell(text = invoice.nameProduct, weight = column2Weight)
                        Spacer(modifier = Modifier.width(5.dp))
                        StatusCell(text = invoice.tempOne, weight = column3Weight)
                        TableCell(
                            text = invoice.tempTwo,
                            weight = column4Weight,
                            alignment = TextAlign.Right
                        )
                    }
                }
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxHeight()
                        .fillMaxWidth()
                )
            }
        }

    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    alignment: TextAlign = TextAlign.Center,
    title: Boolean = false
) {
    Text(
        text = text,
        Modifier
            .weight(weight)
            .padding(10.dp),
        fontWeight = if (title) FontWeight.Bold else FontWeight.Normal,
        textAlign = alignment,
        style = TextStyle(
            fontSize = 15.sp
        )
    )
}

@Composable
fun RowScope.StatusCell(
    text: String,
    weight: Float,
    alignment: TextAlign = TextAlign.Center,
) {

    val color = when (text) {
        else -> Color(0xFFB88C8F)
    }
    val textColor = when (text) {
        else -> Color(0xFF473E3D)
    }

    Text(
        text = text,
        Modifier
            .weight(weight)
            .padding(12.dp)
            .background(color, shape = RoundedCornerShape(50.dp)),
        textAlign = alignment,
        color = textColor,
        style = TextStyle(
            fontSize = 15.sp
        )
    )
}


data class DataCookingTemp(
    val icon: Int,
    val nameProduct: String,
    val tempOne: String,
    val tempTwo: String,
    val isTitle: Boolean
)

