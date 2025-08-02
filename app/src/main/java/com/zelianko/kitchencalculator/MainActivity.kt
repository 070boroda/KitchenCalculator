package com.zelianko.kitchencalculator


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.zelianko.kitchencalculator.lottie.LottieLoader
import com.zelianko.kitchencalculator.modelview.ProductViewModel
import com.zelianko.kitchencalculator.navigation.RecipeNavGraph
import com.zelianko.kitchencalculator.yandex_ads.AppOpenAdManager
import dagger.hilt.android.AndroidEntryPoint
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig

@AndroidEntryPoint
class MainActivity : ComponentActivity(
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
//        val billingViewModel = ViewModelProvider(this)[BillingViewModel::class.java]

        // Creating an extended library configuration.
        com.yandex.mobile.ads.common.MobileAds.initialize(this) {}
        val config =
            AppMetricaConfig.newConfigBuilder("59d2d76c-5e34-4fd3-812a-5ba2e2b969e7").build()
        // Initializing the AppMetrica SDK.
        AppMetrica.activate(this, config)

        if (savedInstanceState == null) {
            AppMetrica.reportAppOpen(this)
        }

        setContent {

            var showLoader by remember { mutableStateOf(true) }


            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                if (showLoader) {
                    LottieLoader(
                        isPlaying = true,
                        changeIsPlaying = { showLoader = false })
                } else {
                    RecipeNavGraph(
                        productViewModel = productViewModel,
//                    billingViewModel = billingViewModel
                    )
                }
            }

            LaunchedEffect(showLoader) {
                if (!showLoader && savedInstanceState == null) {
                    AppOpenAdManager.setOnAdLoadedCallback {
                        if (AppOpenAdManager.isAdAvailable()) {
                            runOnUiThread {
                                AppOpenAdManager.showAdIfAvailable(this@MainActivity)
                            }
                        }
                    }
                    AppOpenAdManager.loadAd(context = this@MainActivity)
                }
            }

        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        AppMetrica.reportAppOpen(intent)
    }
}
