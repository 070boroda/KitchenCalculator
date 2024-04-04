package com.zelianko.kitchencalculator

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.MobileAds
import com.zelianko.kitchencalculator.constants.StringConstants
import com.zelianko.kitchencalculator.google_ads.AppOpenAdManager
import com.zelianko.kitchencalculator.modelview.ProductViewModel
import com.zelianko.kitchencalculator.navigation.RecipeNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        //Инициализация рекламы гугл
        MobileAds.initialize(this) {}
        //
        AppOpenAdManager(this.application, StringConstants.StartAdAppScreenId)
        setContent {
            // KitchenCalculatorTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                RecipeNavGraph(productViewModel = productViewModel)
            }
        }
    }
}
