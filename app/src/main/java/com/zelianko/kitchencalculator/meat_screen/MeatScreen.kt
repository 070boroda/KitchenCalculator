package com.zelianko.kitchencalculator.meat_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.ui.theme.Dimens
import com.zelianko.kitchencalculator.subscriptions.BillingViewModel
import com.zelianko.kitchencalculator.yandex_ads.BannerId
import com.zelianko.kitchencalculator.yandex_ads.BannerSticky


/**
 * Экран виды прожарки мяса
 */
@Composable
//@Preview(showBackground = true)
fun MeatScreen(
    paddingValues: PaddingValues,
//    billingViewModel: BillingViewModel
) {
    val isActiveSub = false
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = colorResource(id = R.color.grey_light)
    ) {
        // Column composable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isActiveSub == false) {
                BannerSticky(id = BannerId.TWO_BANNER.bannerId)
//                GoogleBannerAd(textId = StringConstants.BannerSteakScreenId)
            }
            // Child components of the Column
            MeatCard(
                heightCard = 530.dp,
                R.drawable.blue_hand,
                R.string.blue_hand_steak
            )
            Spacer(modifier = Modifier.height(5.dp))
            MeatCard(
                heightCard = 400.dp,
                R.drawable.rare_hand,
                R.string.rare_hand_steak
            )
            Spacer(modifier = Modifier.height(5.dp))
            MeatCard(
                heightCard = 450.dp,
                R.drawable.medium_rare_hand,
                R.string.medium_rare_hand
            )
            Spacer(modifier = Modifier.height(5.dp))
            MeatCard(
                heightCard = 400.dp,
                R.drawable.medium_hand,
                R.string.medium_hand
            )
            Spacer(modifier = Modifier.height(5.dp))
            MeatCard(
                heightCard = 440.dp,
                R.drawable.medium_well_no_hand,
                R.string.medium_well_no_hand
            )
            Spacer(modifier = Modifier.height(5.dp))
            MeatCard(
                heightCard = 510.dp,
                R.drawable.well_done_hand,
                R.string.well_done_hand
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun MeatCard(
    heightCard: Dp,
    imageId: Int,
    stringId: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = Dimens.SpacingS),
        shape = RoundedCornerShape(Dimens.RadiusL),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.card_surface),
            contentColor = Color.Black
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingM),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                shape = RoundedCornerShape(Dimens.RadiusM),
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = imageId),
                    contentDescription = stringResource(id = stringId),
                    contentScale = ContentScale.Crop,
                )
            }
            Spacer(modifier = Modifier.height(Dimens.SpacingM))
            Text(
                text = stringResource(id = stringId).trimIndent(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}