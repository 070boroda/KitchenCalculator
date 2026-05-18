package com.zelianko.kitchencalculator

import android.app.Application
import com.yandex.mobile.ads.common.YandexAds

import dagger.hilt.android.HiltAndroidApp
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        YandexAds.initialize(this) {}

        val config = AppMetricaConfig
            .newConfigBuilder("59d2d76c-5e34-4fd3-812a-5ba2e2b969e7")
            .build()
        AppMetrica.activate(this, config)
    }
}
