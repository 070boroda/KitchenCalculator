package com.zelianko.kitchencalculator.subactivity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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

@Composable
fun ResultCardProduct(resultCount: State<String?>, value: String) {
    Card(
        modifier = Modifier
            .height(140.dp)
            .width(360.dp)
            // .padding(start = 48.dp, end = 20.dp)
            .clip(shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.backgroud_row),
            contentColor = Color.Black
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.your_result),
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 30.sp
                )
            )
            Text(
                text = "${resultCount.value} ${value}",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )
        }
    }
}