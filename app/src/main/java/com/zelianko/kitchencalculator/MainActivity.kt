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
import com.zelianko.kitchencalculator.modelview.ProductViewModel
import com.zelianko.kitchencalculator.subactivity.Drawer

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        setContent {
            // KitchenCalculatorTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Drawer(
                    productViewModel = productViewModel
                )
            }
        }
    }
}