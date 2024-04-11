package com.zelianko.kitchencalculator.subscriptions

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.constants.StringConstants.Companion.MONTHLY

/**
 * Экран подписки
 */
@Composable
fun SubscribesScreen(
    paddingValues: PaddingValues,
    billingViewModel: BillingViewModel
) {
    val isActiveSub = billingViewModel.isActiveSub.observeAsState()

    val textPrice = billingViewModel.textPrice.observeAsState("")
    val tokenOffer = billingViewModel.offerToken.observeAsState("")
    val productDetails = billingViewModel.productDetails.observeAsState()

    val activity = LocalContext.current as Activity

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        //  color = colorResource(id = R.color.grey_light)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            colorResource(R.color.all_background),
                            colorResource(R.color.grey_light)
                        )
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.size(120.dp))

            if (isActiveSub.value == true) {
                Text(
                    text = "Premium is already available",
                    modifier = Modifier.padding(top = 5.dp),
                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = stringResource(id = R.string.premium_text),
                    modifier = Modifier.padding(top = 5.dp),
                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(10.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.backgroud_row),
                        contentColor = Color.White
                    ),
                    onClick = {
                        productDetails.value?.let {
                            billingViewModel.launchPurchaseFlow(
                                it,
                                activity,
                                tokenOffer.value
                            )
                        }

                    }) {
                    Text(
                        stringResource(id = R.string.subscribe) + textPrice.value + stringResource(
                            id = R.string.month
                        ),
                        fontSize = 22.sp
                    )
                }
                Spacer(modifier = Modifier.size(80.dp))
            }
        }
    }
}
