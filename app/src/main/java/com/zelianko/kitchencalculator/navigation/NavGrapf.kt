package com.zelianko.kitchencalculator.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zelianko.kitchencalculator.util.Routes

@Composable
fun NavGraf(
    navHostController: NavHostController,
    generalScreenContent: @Composable () -> Unit,
    secondScreenContent: @Composable () -> Unit,
    timerScreen: @Composable () -> Unit
    ) {
    NavHost(
        navController = navHostController,
        startDestination = Routes.RECIPE_LIST_SCREEN,
        modifier = Modifier.background(Color.Black)
    ) {
        composable(Routes.COUNTER_SCREEN) {
            generalScreenContent()
        }
        composable(Routes.RECIPE_LIST_SCREEN) {
            secondScreenContent()
        }
        composable(Routes.TIMER_SCREEN) {
            timerScreen()
        }

    }
}