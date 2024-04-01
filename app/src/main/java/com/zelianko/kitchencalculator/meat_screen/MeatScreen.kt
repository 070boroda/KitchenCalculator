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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zelianko.kitchencalculator.R


/**
 * Экран виды прожарки мяса
 */
@Composable
//@Preview(showBackground = true)
fun MeatScreen(
    paddingValues: PaddingValues
) {
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
                heightCard = 500.dp,
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
            .height(heightCard)
            .padding(horizontal = 6.dp)
            .clip(shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.orange_primary),
            contentColor = Color.Black
        )
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(165.dp),
            painter = painterResource(id = imageId),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = stringResource(id = stringId).trimIndent(),
            textAlign = TextAlign.Center,
            style = TextStyle(
                color = Color.Black,
                fontSize = 18.sp
            ),
        )
        Spacer(modifier = Modifier.height(5.dp))
    }
}