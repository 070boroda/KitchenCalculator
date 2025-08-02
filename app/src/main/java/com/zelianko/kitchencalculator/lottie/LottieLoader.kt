package com.zelianko.kitchencalculator.lottie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import java.time.LocalDate

@Composable
fun LottieLoader(
    isPlaying: Boolean,
    changeIsPlaying: (isPlaying: Boolean) -> Unit
) {
    val name = if(LocalDate.now().dayOfWeek.value % 2 == 0) "food_two.json" else "food_two.json"
    val composition by rememberLottieComposition(LottieCompositionSpec
        .Asset(name))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying
    )

    LaunchedEffect(key1 = progress) {
        if (progress >= 0.99f) {
            changeIsPlaying(false)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress }
        )
    }

}