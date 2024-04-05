package com.zelianko.kitchencalculator.subscriptions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
    currentSubscriptionList: List<String>,
    chooseSubscriptionModel: ChooseSubscription
) {

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
            Text(
                text = "Оформив подписку, вы:".trimIndent(),
                modifier = Modifier.padding(top = 2.dp),
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                color = Color.DarkGray
            )

            Text(
                text = "1. Помогаете нам сделать наше приложение лучше!!!",
                modifier = Modifier.padding(top = 5.dp),
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                color = Color.DarkGray
            )
            Text(
                text = "2. Скрывает всю рекламу".trimIndent(),
                modifier = Modifier.padding(all = 8.dp),
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                color = Color.DarkGray
            )

            Text(
                text = "3. Не ограниченное кол-во рецептов",
                modifier = Modifier.padding(all = 8.dp),
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                color = Color.DarkGray
            )

            Text(
                text = "4. Не ограниченное кол-во продуктов в рецептах",
                modifier = Modifier.padding(all = 8.dp),
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                color = Color.DarkGray
            )

            Text(
                text = "Вы всегда можете отменить подписку",
                modifier = Modifier.padding(top = 8.dp),
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                color = Color.DarkGray
            )
            Text(
                text = "в личном кабинете",
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                color = Color.DarkGray
            )
            Text(
                text = "Google Market",
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.size(10.dp))
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.backgroud_row),
                    contentColor = Color.White
                ),
                onClick = {
                    chooseSubscriptionModel.checkSubscriptionStatus(MONTHLY)

                }) {
                Text("Подписаться  ${chooseSubscriptionModel.textPrice.value}/мес.", fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.size(80.dp))
        }
    }
}