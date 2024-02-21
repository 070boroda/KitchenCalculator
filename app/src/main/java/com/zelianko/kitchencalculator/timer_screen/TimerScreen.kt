package com.zelianko.kitchencalculator.timer_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.subactivity.Timer

@Composable
fun TimerScreen(
    paddingValues: PaddingValues
) {
    Surface(
        color = colorResource(id = R.color.grey_light),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            val context = LocalContext.current
            Timer(
                totalTime = 5L * 1000L,
                handleColor = colorResource(id = R.color.orange_primary),
                inactiveBarColor = Color.DarkGray,
                activeBarColor = colorResource(id = R.color.orange_primary),
                modifier = Modifier.size(250.dp),
                context = context
            )
        }
    }
}
