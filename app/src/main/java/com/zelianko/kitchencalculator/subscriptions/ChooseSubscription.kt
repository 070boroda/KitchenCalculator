package com.zelianko.kitchencalculator.subscriptions

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ConsumeResponseListener
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.ImmutableList
import com.zelianko.kitchencalculator.constants.StringConstants.Companion.MONTHLY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Класс подписки к приложению
 * https://medium.com/@daniel.atitienei/implementing-in-app-subscriptions-and-products-using-jetpack-compose-e867141bec7b
 */
class ChooseSubscription(
    private val activity: Activity
) {
    private val _subscriptions = MutableStateFlow<List<String>>(emptyList())
    val subscriptions = _subscriptions.asStateFlow()
    private var _textPrice = MutableLiveData("")
        private set
    val textPrice: LiveData<String> = _textPrice

    private val purchaseUpdateListener = PurchasesUpdatedListener { result, purchases ->
        if (result.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            Log.d("BILLING", "OK")
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        } else if (result.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.d("BILLING", "USER_CANCELED")
            // User canceled the purchase
        } else {
            Log.d("BILLING", result.responseCode.toString())
            // Handle other error cases
        }
    }

    private var billingClient: BillingClient = BillingClient.newBuilder(activity)
        .setListener(purchaseUpdateListener)
        .enablePendingPurchases()
        .build()

    private fun handlePurchase(purchase: Purchase) {
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        val listener = ConsumeResponseListener { billingResult, s -> }

        billingClient.consumeAsync(consumeParams, listener)

        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams
                    .newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()

                billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        _subscriptions.update {
                            val newList = it.toMutableList()
                            newList.addAll(purchase.products)
                            newList
                        }
                    }
                }
            }
        }
    }

    fun billingSetup() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d("BILLING", "billingSetup OK")
                    hasSubscription()
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.d("BILLING", "DISCONECCT")
                // Handle billing service disconnection
            }
        })
    }

    //Как я понял проверяет наличие текущих подписок, КУПЛЕННЫХ
    fun hasSubscription() {
        val queryPurchaseParams = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()
        Log.d("BILLING", "hasSubscription queryPurchaseParams OK")
        billingClient.queryPurchasesAsync(
            queryPurchaseParams
        ) { result, purchases ->
            Log.d("BILLING", "hasSubscription queryPurchasesAsync OK")
            when (result.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    Log.d("BILLING", "hasSubscription in WHEN OK")
                    Log.d("BILLING",  purchases.size.toString())
                    for (purchase in purchases) {
                        Log.d("BILLING", purchases.size.toString())
                        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                            // User has an active subscription
                            _subscriptions.update {
                                val newList = it.toMutableList()
                                newList.addAll(purchase.products)
                                newList
                            }
                            return@queryPurchasesAsync
                        }
                    }
                }

                BillingClient.BillingResponseCode.USER_CANCELED -> {
                    Log.d("BILLING", "hasSubscription in WHEN USER_CANCELED")
                    // User canceled the purchase
                }

                else -> {
                    Log.d("BILLING", "hasSubscription in WHEN ELSE")
                    // Handle other error cases
                }
            }

            // User does not have an active subscription

        }
    }

    fun checkSubscriptionStatus(
        subscriptionPlanId: String,
    ) {
        val queryPurchaseParams = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        billingClient.queryPurchasesAsync(
            queryPurchaseParams
        ) { result, purchases ->
            when (result.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    Log.d("BILLING", "checkSubscriptionStatus OK OK")
                    for (purchase in purchases) {
                        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && purchase.products.contains(
                                subscriptionPlanId
                            )
                        ) {
                            _subscriptions.update {
                                val newList = it.toMutableList()
                                newList.addAll(purchase.products)
                                newList
                            }
                            return@queryPurchasesAsync
                        }
                    }
                }

                BillingClient.BillingResponseCode.USER_CANCELED -> {
                    // User canceled the purchase
                }

                else -> {
                    // Handle other error cases
                }
            }
            // User does not have an active subscription
            querySubscriptionPlans(subscriptionPlanId)
        }
    }

    private fun querySubscriptionPlans(
        subscriptionPlanId: String,
    ) {
        val queryProductDetailsParams =
            QueryProductDetailsParams.newBuilder()
                .setProductList(
                    ImmutableList.of(
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(MONTHLY)
                            .setProductType(BillingClient.ProductType.SUBS)
                            .build(),
                    )
                )
                .build()

        billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.d("querySubscriptionPlans", "checkSubscriptionStatus OK OK")
                var offerToken = ""
                val productDetails = productDetailsList.firstOrNull { productDetails ->
                    productDetails.subscriptionOfferDetails?.any {
                        if (it.basePlanId == subscriptionPlanId) {
                            offerToken = it.offerToken
                            _textPrice.postValue(it.pricingPhases.pricingPhaseList[0].formattedPrice)
                            true
                        } else {
                            false
                        }
                    } == true
                }
                productDetails?.let {
                    val productDetailsParamsList = listOf(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(it)
                            .setOfferToken(offerToken)
                            .build()
                    )

                    val billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build()

                    billingClient.launchBillingFlow(activity, billingFlowParams)
                }
            }
        }
    }
}