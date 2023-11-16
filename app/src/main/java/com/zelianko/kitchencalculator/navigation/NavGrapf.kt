package com.zelianko.kitchencalculator.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraf(
    navHostController: NavHostController,
    generalScreenContent: @Composable () -> Unit,
    secondScreenContent: @Composable () -> Unit,
    ) {
    NavHost(
        navController = navHostController,
        startDestination = "firstScreen",
        modifier = Modifier.background(Color.Black)
    ) {
        composable("firstScreen") {
            generalScreenContent()
        }
        composable("secondScreen") {
            secondScreenContent()
        }

    }
}