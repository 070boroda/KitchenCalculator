package com.zelianko.kitchencalculator.yandex_ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.yandex.mobile.ads.appopenad.AppOpenAd
import com.yandex.mobile.ads.appopenad.AppOpenAdLoadListener
import com.yandex.mobile.ads.appopenad.AppOpenAdLoader
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import kotlin.apply

object AppOpenAdManager {
    private var appOpenAdLoader: AppOpenAdLoader? = null
    private var isLoadingAd = false
    private var isShowingAd = false
    private var currentAds: AppOpenAd? = null
    private var onAdLoadedCallback: (() -> Unit)? = null

    fun setOnAdLoadedCallback(callback: () -> Unit) {
        onAdLoadedCallback = callback
    }
    // Загружаем рекламу
    fun loadAd(
        adUnitId: String = "R-M-14504374-2",
        context: Context
    ) {
        if (isLoadingAd || isAdAvailable()) {
            return
        }
        isLoadingAd = true

        if (appOpenAdLoader == null) {
            appOpenAdLoader = AppOpenAdLoader(context).apply {
                setAdLoadListener(object : AppOpenAdLoadListener {
                    override fun onAdFailedToLoad(error: AdRequestError) {
                        Log.d("Load Open ADS", error.description)
                        isLoadingAd = false
                        currentAds = null
                    }

                    override fun onAdLoaded(appOpenAd: AppOpenAd) {
                        currentAds = appOpenAd
                        Log.d("Load Open ADS", "Open App Ads Was Loaded")
                        isLoadingAd = false
                        onAdLoadedCallback?.invoke()
                    }
                })
            }
        }

        appOpenAdLoader?.loadAd(
            AdRequestConfiguration.Builder(adUnitId).build()
        )
    }

    private fun clearAppOpenAd() {
        currentAds?.setAdEventListener(null)
        currentAds = null
    }

    // Проверяем, доступна ли реклама
    fun isAdAvailable(): Boolean {
        return currentAds != null
    }

    // Показываем рекламу
    fun showAdIfAvailable(activity: Activity) {
        Log.d("Load Open ADS", "1. Try show open Ads")
        if (isShowingAd) {
            Log.d("Load Open ADS", "2 Try show open Ads")
            return
        }
        if (isAdAvailable()) {
            Log.d("Load Open ADS", "3. Try show open Ads")
            currentAds?.show(activity)
        } else {
            Log.d("Load Open ADS", "4. Try show open Ads")
            clearAppOpenAd()
        }
    }
}




