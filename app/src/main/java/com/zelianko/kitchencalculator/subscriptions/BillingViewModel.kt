//package com.zelianko.kitchencalculator.subscriptions
//
//import android.app.Activity
//import android.content.Context
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.android.billingclient.api.AcknowledgePurchaseParams
//import com.android.billingclient.api.BillingClient
//import com.android.billingclient.api.BillingClient.BillingResponseCode
//import com.android.billingclient.api.BillingClientStateListener
//import com.android.billingclient.api.BillingFlowParams
//import com.android.billingclient.api.BillingResult
//import com.android.billingclient.api.PendingPurchasesParams
//import com.android.billingclient.api.ProductDetails
//import com.android.billingclient.api.Purchase
//import com.android.billingclient.api.PurchasesUpdatedListener
//import com.android.billingclient.api.QueryProductDetailsParams
//import com.android.billingclient.api.QueryPurchasesParams
//import com.zelianko.kitchencalculator.constants.StringConstants.Companion.MONTHLY
//import dagger.hilt.android.lifecycle.HiltViewModel
//
//@HiltViewModel
//class BillingViewModel : ViewModel() {
//
//    companion object {
//        private const val TAG = "BillingViewModel"
//    }
//
//    // ------------------------------------------------------------------------
//    // LiveData
//    // ------------------------------------------------------------------------
//
//    private val _isActiveSub = MutableLiveData(false)
//    val isActiveSub: LiveData<Boolean> = _isActiveSub
//
//    private val _textSub = MutableLiveData("")
//    val textSub: LiveData<String> = _textSub
//
//    private val _textPrice = MutableLiveData("")
//    val textPrice: LiveData<String> = _textPrice
//
//    private val _textDiscr = MutableLiveData("")
//    val textDiscr: LiveData<String> = _textDiscr
//
//    private val _offerToken = MutableLiveData("")
//    val offerToken: LiveData<String> = _offerToken
//
//    private val _productDetails = MutableLiveData<ProductDetails>()
//    val productDetails: LiveData<ProductDetails> = _productDetails
//
//    // ------------------------------------------------------------------------
//    // Billing
//    // ------------------------------------------------------------------------
//
//    private var billingClient: BillingClient? = null
//
//    private val purchasesUpdatedListener =
//        PurchasesUpdatedListener { billingResult, purchases ->
//
//            when (billingResult.responseCode) {
//
//                BillingResponseCode.OK -> {
//
//                    if (purchases.isNullOrEmpty()) {
//                        Log.d(TAG, "Purchase list is empty")
//                        return@PurchasesUpdatedListener
//                    }
//
//                    purchases.forEach {
//                        handlePurchase(it)
//                    }
//                }
//
//                BillingResponseCode.USER_CANCELED -> {
//                    Log.d(TAG, "Purchase canceled")
//                }
//
//                BillingResponseCode.ITEM_ALREADY_OWNED -> {
//                    Log.d(TAG, "Subscription already owned")
//                    queryPurchases()
//                }
//
//                else -> {
//                    Log.e(
//                        TAG,
//                        "Purchase error: ${billingResult.responseCode} ${billingResult.debugMessage}"
//                    )
//                }
//            }
//        }
//
//    // ------------------------------------------------------------------------
//    // Initialization
//    // ------------------------------------------------------------------------
//
//    fun initBillingClient(context: Context) {
//
//        if (billingClient?.isReady == true) {
//            return
//        }
//
//        billingClient =
//            BillingClient.newBuilder(context.applicationContext)
//                .setListener(purchasesUpdatedListener)
//                .enablePendingPurchases(
//                    PendingPurchasesParams.newBuilder()
//                        .enableOneTimeProducts()
//                        .build()
//                )
//                .build()
//
//        establishConnection()
//    }
//
//    // ------------------------------------------------------------------------
//    // Connection
//    // ------------------------------------------------------------------------
//
//    fun establishConnection() {
//
//        val client = billingClient ?: return
//
//        if (client.isReady) {
//            showProducts()
//            queryPurchases()
//            return
//        }
//
//        client.startConnection(object : BillingClientStateListener {
//
//            override fun onBillingSetupFinished(
//                billingResult: BillingResult
//            ) {
//
//                if (billingResult.responseCode ==
//                    BillingResponseCode.OK
//                ) {
//
//                    Log.d(TAG, "Billing connected")
//
//                    showProducts()
//
//                    queryPurchases()
//
//                } else {
//
//                    Log.e(
//                        TAG,
//                        "Billing connection error ${billingResult.debugMessage}"
//                    )
//                }
//            }
//
//            override fun onBillingServiceDisconnected() {
//
//                Log.w(TAG, "Billing disconnected")
//
//                establishConnection()
//            }
//        })
//    }
//
//    // ------------------------------------------------------------------------
//    // Products
//    // ------------------------------------------------------------------------
//
//    fun showProducts() {
//
//        val client = billingClient ?: return
//
//        if (!client.isReady) {
//            establishConnection()
//            return
//        }
//
//        val params =
//            QueryProductDetailsParams.newBuilder()
//                .setProductList(
//                    listOf(
//                        QueryProductDetailsParams.Product
//                            .newBuilder()
//                            .setProductId(MONTHLY)
//                            .setProductType(BillingClient.ProductType.SUBS)
//                            .build()
//                    )
//                )
//                .build()
//
//        client.queryProductDetailsAsync(params) { billingResult, products ->
//
//            if (billingResult.responseCode != BillingResponseCode.OK) {
//
//                Log.e(
//                    TAG,
//                    "queryProductDetailsAsync error ${billingResult.debugMessage}"
//                )
//
//                return@queryProductDetailsAsync
//            }
//
//            if (products.isEmpty()) {
//
//                Log.e(TAG, "Subscription product not found")
//
//                return@queryProductDetailsAsync
//            }
//
//            val product = products.first()
//
//            _productDetails.postValue(product)
//
//            _textSub.postValue(product.name)
//
//            _textDiscr.postValue(product.description)
//
//            val offer = product.subscriptionOfferDetails
//                ?.firstOrNull()
//
//            _offerToken.postValue(
//                offer?.offerToken ?: ""
//            )
//
//            val price =
//                offer
//                    ?.pricingPhases
//                    ?.pricingPhaseList
//                    ?.firstOrNull()
//                    ?.formattedPrice
//                    ?: ""
//
//            _textPrice.postValue(price)
//
//            Log.d(TAG, "Loaded subscription: ${product.productId}")
//        }
//    }
//
//    // ------------------------------------------------------------------------
//    // Purchase
//    // ------------------------------------------------------------------------
//
//    fun launchPurchaseFlow(
//        productDetails: ProductDetails,
//        activity: Activity,
//        selectedOfferToken: String
//    ) {
//
//        val client = billingClient ?: return
//
//        if (!client.isReady) {
//            establishConnection()
//            return
//        }
//
//        val productDetailsParams =
//            BillingFlowParams.ProductDetailsParams
//                .newBuilder()
//                .setProductDetails(productDetails)
//                .setOfferToken(selectedOfferToken)
//                .build()
//
//        val billingFlowParams =
//            BillingFlowParams.newBuilder()
//                .setProductDetailsParamsList(
//                    listOf(productDetailsParams)
//                )
//                .build()
//
//        val result =
//            client.launchBillingFlow(
//                activity,
//                billingFlowParams
//            )
//
//        Log.d(
//            TAG,
//            "launchBillingFlow: ${result.responseCode} ${result.debugMessage}"
//        )
//    }
//
//    // ------------------------------------------------------------------------
//    // Compatibility
//    // ------------------------------------------------------------------------
//
//    /**
//     * Старый публичный метод.
//     * Оставлен, чтобы не менять существующий код проекта.
//     */
//    fun checkSubscription(context: Context) {
//
//        if (billingClient == null) {
//            initBillingClient(context)
//            return
//        }
//
//        queryPurchases()
//    }
//
//    // ------------------------------------------------------------------------
//    // Existing Purchases
//    // ------------------------------------------------------------------------
//
//    private fun queryPurchases() {
//
//        val client = billingClient ?: return
//
//        if (!client.isReady) {
//            return
//        }
//
//        val params =
//            QueryPurchasesParams.newBuilder()
//                .setProductType(
//                    BillingClient.ProductType.SUBS
//                )
//                .build()
//
//        client.queryPurchasesAsync(params) { billingResult, purchases ->
//
//            if (billingResult.responseCode !=
//                BillingResponseCode.OK
//            ) {
//
//                Log.e(
//                    TAG,
//                    "queryPurchasesAsync error ${billingResult.debugMessage}"
//                )
//
//                _isActiveSub.postValue(false)
//
//                return@queryPurchasesAsync
//            }
//
//            if (purchases.isEmpty()) {
//
//                Log.d(TAG, "No active subscriptions")
//
//                _isActiveSub.postValue(false)
//
//                return@queryPurchasesAsync
//            }
//
//            var hasActiveSubscription = false
//
//            purchases.forEach { purchase ->
//
//                Log.d(
//                    TAG,
//                    "Purchase state=${purchase.purchaseState}"
//                )
//
//                Log.d(
//                    TAG,
//                    purchase.originalJson
//                )
//
//                if (purchase.purchaseState ==
//                    Purchase.PurchaseState.PURCHASED
//                ) {
//
//                    hasActiveSubscription = true
//
//                    handlePurchase(purchase)
//                }
//            }
//
//            _isActiveSub.postValue(
//                hasActiveSubscription
//            )
//        }
//    }
//
//    // ------------------------------------------------------------------------
//    // Purchase processing
//    // ------------------------------------------------------------------------
//
//    private fun handlePurchase(
//        purchase: Purchase
//    ) {
//
//        if (purchase.purchaseState !=
//            Purchase.PurchaseState.PURCHASED
//        ) {
//
//            Log.d(
//                TAG,
//                "Purchase is not completed"
//            )
//
//            return
//        }
//
//        _isActiveSub.postValue(true)
//
//        if (purchase.isAcknowledged) {
//
//            Log.d(
//                TAG,
//                "Purchase already acknowledged"
//            )
//
//            return
//        }
//
//        val acknowledgeParams =
//            AcknowledgePurchaseParams.newBuilder()
//                .setPurchaseToken(
//                    purchase.purchaseToken
//                )
//                .build()
//
//        billingClient?.acknowledgePurchase(
//            acknowledgeParams
//        ) { billingResult ->
//
//            if (billingResult.responseCode ==
//                BillingResponseCode.OK
//            ) {
//
//                Log.d(
//                    TAG,
//                    "Purchase acknowledged"
//                )
//
//                _isActiveSub.postValue(true)
//
//            } else {
//
//                Log.e(
//                    TAG,
//                    "Acknowledge failed ${billingResult.debugMessage}"
//                )
//            }
//        }
//    }
//
//    // ------------------------------------------------------------------------
//    // Helpers
//    // ------------------------------------------------------------------------
//
//    /**
//     * Возвращает первый доступный offer.
//     *
//     * Если позже появится несколько Base Plans,
//     * достаточно изменить только этот метод.
//     */
//    private fun ProductDetails.bestOffer() =
//        subscriptionOfferDetails?.firstOrNull()
//
//    /**
//     * Есть ли активная покупка.
//     */
//    private fun Purchase.isPurchased(): Boolean =
//        purchaseState == Purchase.PurchaseState.PURCHASED
//
//    /**
//     * Требуется ли подтверждение покупки.
//     */
//    private fun Purchase.needAcknowledge(): Boolean =
//        !isAcknowledged
//
//    /**
//     * Можно ли выполнять запросы Billing.
//     */
//    private fun isBillingReady(): Boolean =
//        billingClient?.isReady == true
//
//    // ------------------------------------------------------------------------
//    // Lifecycle
//    // ------------------------------------------------------------------------
//
//    override fun onCleared() {
//
//        super.onCleared()
//
//        try {
//
//            billingClient?.endConnection()
//
//            Log.d(TAG, "Billing disconnected")
//
//        } catch (_: Exception) {
//        }
//
//        billingClient = null
//    }
//}