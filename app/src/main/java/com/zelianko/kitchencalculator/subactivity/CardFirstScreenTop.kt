package com.zelianko.kitchencalculator.subactivity

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.ui.theme.Dimens

/**
 * Карточка меры (стакан/ложка) на экране калькулятора. Цвет фона задаётся
 * вызывающей стороной (пастельные тона), текст всегда тёмный — фоны светлые
 * в обеих темах. Размеры и типографика — из общей шкалы.
 */
@Composable
fun CardFirstScreenTop(
    title: Int = R.string.table_spoon,
    titleGramm: Int,
    value: String = "200g",
    colorCard: Color = colorResource(id = R.color.table_spoon_card),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.heightIn(min = 116.dp),
        shape = RoundedCornerShape(Dimens.RadiusL),
        colors = CardDefaults.cardColors(
            containerColor = colorCard,
            contentColor = Color.Black,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingS),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(id = title),
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                textAlign = TextAlign.Center,
            )
            if (titleGramm != 0) {
                Text(
                    text = stringResource(id = titleGramm),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
