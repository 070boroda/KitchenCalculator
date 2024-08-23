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
import com.google.android.gms.ads.MobileAds
import com.mbridge.msdk.MBridgeConstans
import com.mbridge.msdk.out.MBridgeSDKFactory
import com.vungle.ads.VunglePrivacySettings
import com.zelianko.kitchencalculator.constants.StringConstants
import com.zelianko.kitchencalculator.google_ads.AppOpenAdManager
import com.zelianko.kitchencalculator.modelview.ProductViewModel
import com.zelianko.kitchencalculator.navigation.RecipeNavGraph
import com.zelianko.kitchencalculator.subscriptions.BillingViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig

@AndroidEntryPoint
class MainActivity : ComponentActivity(
) {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        val billingViewModel = ViewModelProvider(this)[BillingViewModel::class.java]

        VunglePrivacySettings.setGDPRStatus(true, "1.0.0")
        VunglePrivacySettings.setCCPAStatus(true)


        val sdk = MBridgeSDKFactory.getMBridgeSDK()
        sdk.setConsentStatus(this, MBridgeConstans.IS_SWITCH_ON)
        sdk.setCoppaStatus(this,true);

        val mBridgeSDK = MBridgeSDKFactory.getMBridgeSDK()
        mBridgeSDK.setDoNotTrackStatus(false)

        // Creating an extended library configuration.
        val config = AppMetricaConfig.newConfigBuilder("59d2d76c-5e34-4fd3-812a-5ba2e2b969e7").build()
        // Initializing the AppMetrica SDK.
        AppMetrica.activate(this, config)

        //Инициализация рекламы гугл
        MobileAds.initialize(this) {}

        if (savedInstanceState == null) {
            AppMetrica.reportAppOpen(this)
        }

        setContent {
            // KitchenCalculatorTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {

//                val chooseSubscriptionModel = remember {
//                    ChooseSubscription(this)
//                }

                val context = LocalContext.current
                billingViewModel.initBillingClient(context)
                billingViewModel.checkSubscription(context)
                val isActiveSub = billingViewModel.isActiveSub.observeAsState()
                //val currentSubscriptionList by chooseSubscriptionModel.subscriptions.collectAsState()

                if (isActiveSub.value == false) {
                    AppOpenAdManager(this.application, StringConstants.StartAdAppScreenId, billingViewModel)
                }

                RecipeNavGraph(
                    productViewModel = productViewModel,
                    billingViewModel = billingViewModel
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        AppMetrica.reportAppOpen(intent)
    }
}
