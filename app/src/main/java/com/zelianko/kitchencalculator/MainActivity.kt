package com.zelianko.kitchencalculator

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
//import com.google.android.gms.ads.MobileAds
//import com.mbridge.msdk.MBridgeConstans
//import com.mbridge.msdk.out.MBridgeSDKFactory
//import com.vungle.ads.VunglePrivacySettings
import com.zelianko.kitchencalculator.constants.StringConstants
//import com.zelianko.kitchencalculator.google_ads.AppOpenAdManager
import com.zelianko.kitchencalculator.modelview.ProductViewModel
import com.zelianko.kitchencalculator.navigation.RecipeNavGraph
import com.zelianko.kitchencalculator.subscriptions.BillingViewModel
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
        val config = AppMetricaConfig.newConfigBuilder("59d2d76c-5e34-4fd3-812a-5ba2e2b969e7").build()
        // Initializing the AppMetrica SDK.
        AppMetrica.activate(this, config)

        if (savedInstanceState == null) {
            AppMetrica.reportAppOpen(this)
        }

        setContent {

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                RecipeNavGraph(
                    productViewModel = productViewModel,
//                    billingViewModel = billingViewModel
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        AppMetrica.reportAppOpen(intent)
    }
}
