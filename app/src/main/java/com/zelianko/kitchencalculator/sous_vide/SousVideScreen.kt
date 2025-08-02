package com.zelianko.kitchencalculator.sous_vide

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
import com.zelianko.kitchencalculator.subscriptions.BillingViewModel
import com.zelianko.kitchencalculator.yandex_ads.BannerId
import com.zelianko.kitchencalculator.yandex_ads.BannerSticky

/**
 * Су-вид экран
 */
@Composable
fun SousVideScreen(
    paddingValues: PaddingValues,
//    billingViewModel: BillingViewModel
) {
    val isActiveSub = false

    val dataCookingTempLists = listOf(
        SousVideTemp(R.drawable.bull, stringResource(id = R.string.beef), "     ", "  ", true),
        SousVideTemp(
            R.drawable.bull,
            stringResource(id = R.string.raw_20_30),
            "15-30 min.",
            "50 \u2103",
            false
        ),
        SousVideTemp(
            R.drawable.bull,
            stringResource(id = R.string.raw_30_40),
            "25-30 min",
            "50 \u2103",
            false
        ),
        SousVideTemp(
            R.drawable.bull,
            stringResource(id = R.string.rare_20_30),
            "40-120 min.",
            "55 \u2103",
            false
        ),
        SousVideTemp(
            R.drawable.bull,
            stringResource(id = R.string.rare_30_40),
            "65-120 min",
            "55 \u2103",
            false
        ),
        SousVideTemp(
            R.drawable.bull,
            stringResource(id = R.string.medium_rare_20_30),
            "45-180 min.",
            "58 \u2103",
            false
        ),
        SousVideTemp(
            R.drawable.bull,
            stringResource(id = R.string.medium_rare_30_40),
            "80-180 min",
            "58 \u2103",
            false
        ),
        SousVideTemp(
            R.drawable.bull,
            stringResource(id = R.string.ribs),
            "60-240 min",
            "58 \u2103",
            false
        ),
        SousVideTemp(
            R.drawable.bull,
            stringResource(id = R.string.tongue),
            "18-24 hours",
            "70 \u2103",
            false
        ),


        SousVideTemp(
            R.drawable.pig_temp,
            stringResource(id = R.string.pork_temp),
            "     ",
            "  ",
            true
        ),
        SousVideTemp(
            R.drawable.pig_temp,
            stringResource(id = R.string.pork_loin),
            "150-600 min",
            "80 \u2103",
            false
        ),
        SousVideTemp(
            R.drawable.pig_temp,
            stringResource(id = R.string.fillet_30_40),
            "65-120 min",
            "60 \u2103",
            false
        ),
        SousVideTemp(
            R.drawable.pig_temp,
            stringResource(id = R.string.fillet_40_50),
            "100-120 min",
            "60 \u2103",
            false
        ),
        SousVideTemp(
            R.drawable.pig_temp,
            stringResource(id = R.string.neck),
            "600 min",
            "75 \u2103",
            false
        ),
        SousVideTemp(
            R.drawable.pig_temp,
            stringResource(id = R.string.tenderloin_20_30),
            "35-170 min",
            "60 \u2103",
            false
        ),
        SousVideTemp(
            R.drawable.pig_temp,
            stringResource(id = R.string.tenderloin_30_40),
            "60-170 min",
            "60 \u2103",
            false
        ),
        SousVideTemp(
            R.drawable.pig_temp,
            stringResource(id = R.string.ham),
            "20 hourse",
            "65 \u2103",
            false
        ),
        SousVideTemp(
            R.drawable.pig_temp,
            stringResource(id = R.string.shoulder),
            "600 min",
            "80 \u2103",
            false
        ),
        SousVideTemp(
            R.drawable.pig_temp,
            stringResource(id = R.string.knuckle),
            "300-420 min",
            "70 \u2103",
            false
        ),
        SousVideTemp(
            R.drawable.pig_temp,
            stringResource(id = R.string.kebabs),
            "120 min",
            "70 \u2103",
            false
        ),
        SousVideTemp(
            R.drawable.pig_temp,
            stringResource(id = R.string.brisket),
            "300 min",
            "70 \u2103",
            false
        ),

        SousVideTemp(
            R.drawable.chicken,
            stringResource(id = R.string.chicken_sous_vide),
            "     ",
            "  ",
            true
        ),
        SousVideTemp(
            R.drawable.chicken,
            stringResource(id = R.string.fillet),
            "40-70 min",
            "65 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.chicken,
            stringResource(id = R.string.leg_sous_vide),
            "180 min",
            "65 ℃",
            false
        ),

        SousVideTemp(
            R.drawable.duck,
            stringResource(id = R.string.goose_duck),
            "     ",
            "  ",
            true
        ),
        SousVideTemp(
            R.drawable.duck,
            stringResource(id = R.string.duck_fillet),
            "90-150 min",
            "58 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.duck,
            stringResource(id = R.string.duck_legs),
            "130-240 min",
            "80 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.duck,
            stringResource(id = R.string.goose_liver),
            "30-45 min",
            "55 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.duck,
            stringResource(id = R.string.goose_legs),
            "70-130 min",
            "58 ℃",
            false
        ),

        SousVideTemp(R.drawable.fish, stringResource(id = R.string.fish), "    ", "  ", true),
        SousVideTemp(
            R.drawable.fish,
            stringResource(id = R.string.salmon),
            "15-20 min",
            "50 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.fish,
            stringResource(id = R.string.halibut),
            "15-30 min",
            "52 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.fish,
            stringResource(id = R.string.tuna),
            "20-50 min",
            "58 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.fish,
            stringResource(id = R.string.perch),
            "15-60 min",
            "52 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.fish,
            stringResource(id = R.string.catfish),
            "60 min",
            "50 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.fish,
            stringResource(id = R.string.mackerel),
            "10-15 min",
            "52 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.fish,
            stringResource(id = R.string.octopus),
            "240 min",
            "85 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.fish,
            stringResource(id = R.string.shrimp_grey),
            "25 min",
            "50 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.fish,
            stringResource(id = R.string.lobster),
            "30-60 min",
            "58 ℃",
            false
        ),

        SousVideTemp(
            R.drawable.baseline_egg_24,
            stringResource(id = R.string.egg),
            "    ",
            "  ",
            true
        ),
        SousVideTemp(
            R.drawable.baseline_egg_24,
            stringResource(id = R.string.soft_boiled_egg),
            "60 min",
            "65 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.baseline_egg_24,
            stringResource(id = R.string.hard_boiled_egg),
            "80 min",
            "68 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.baseline_egg_24,
            stringResource(id = R.string.poached_egg),
            "60 min",
            "62 ℃",
            false
        ),


        SousVideTemp(
            R.drawable.vegetables_salad_svgrepo_com,
            stringResource(id = R.string.vegetables),
            "    ",
            "  ",
            true
        ),
        SousVideTemp(
            R.drawable.vegetables_salad_svgrepo_com,
            stringResource(id = R.string.cabbage),
            "60 min",
            "85 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.vegetables_salad_svgrepo_com,
            stringResource(id = R.string.carrot),
            "50 min",
            "85 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.vegetables_salad_svgrepo_com,
            stringResource(id = R.string.corn),
            "60 min",
            "85 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.vegetables_salad_svgrepo_com,
            stringResource(id = R.string.string_beans),
            "120 min",
            "85 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.vegetables_salad_svgrepo_com,
            stringResource(id = R.string.mushrooms),
            "15 min",
            "85 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.vegetables_salad_svgrepo_com,
            stringResource(id = R.string.potatoes),
            "50 min",
            "85 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.vegetables_salad_svgrepo_com,
            stringResource(id = R.string.turnip),
            "60 min",
            "85 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.vegetables_salad_svgrepo_com,
            stringResource(id = R.string.sparrowgrass),
            "35-55 min",
            "85 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.vegetables_salad_svgrepo_com,
            stringResource(id = R.string.celery),
            "90 min",
            "85 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.vegetables_salad_svgrepo_com,
            stringResource(id = R.string.pumpkin),
            "10-95 min",
            "85 ℃",
            false
        ),

        SousVideTemp(
            R.drawable.fruits_banana_svgrepo_com,
            stringResource(id = R.string.fruits),
            "    ",
            "  ",
            true
        ),
        SousVideTemp(
            R.drawable.fruits_banana_svgrepo_com,
            stringResource(id = R.string.cherry),
            "25 min",
            "70 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.fruits_banana_svgrepo_com,
            stringResource(id = R.string.pear),
            "25 min",
            "85 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.fruits_banana_svgrepo_com,
            stringResource(id = R.string.plum),
            "25 min",
            "70 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.fruits_banana_svgrepo_com,
            stringResource(id = R.string.apple),
            "25-35 min",
            "85 ℃",
            false
        ),
        SousVideTemp(
            R.drawable.fruits_banana_svgrepo_com,
            stringResource(id = R.string.berry),
            "45 min",
            "70 ℃",
            false
        ),


//                Фрукты ФРУКТЫ	Время приготовления	Температура приготовления
//                Вишня	25 мин.	70ºС
//                Груша	25 мин.	85ºС
//                Сливы и алыча	25 мин.	70ºС
//                Яблоки	25-35 мин.	85ºС
//                Ягоды	45 мин.	70ºС
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
                if (isActiveSub == false) {
                    BannerSticky(id = BannerId.SEVEN_BANNER.bannerId)
//                    GoogleBannerAd(textId = StringConstants.BannerSousVideId)
                }
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
                        text = stringResource(R.string.cooking_time),
                        weight = column3Weight,
                        title = true
                    )
                    TableCell(
                        text = stringResource(R.string.cooking_temp),
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


data class SousVideTemp(
    val icon: Int,
    val nameProduct: String,
    val tempOne: String,
    val tempTwo: String,
    val isTitle: Boolean
)