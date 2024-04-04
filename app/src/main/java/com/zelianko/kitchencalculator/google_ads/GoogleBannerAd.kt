package com.zelianko.kitchencalculator.google_ads

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.window.layout.WindowMetrics
import androidx.window.layout.WindowMetricsCalculator
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

/**
 * Баннер рекламы гугл
 * даптивно под ширину экрана
 */
@Composable
fun GoogleBannerAd(
    modifier: Modifier = Modifier,
    textId: String = "ca-app-pub-3940256099942544/9214589741"
) {
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = {
            val windowMetrics: WindowMetrics =
                WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(it)
            val bounds = windowMetrics.bounds

            var adWithPixels = it.resources.displayMetrics.widthPixels.toFloat()

            if (adWithPixels == 0f) {
                adWithPixels = bounds.width().toFloat()
            }

            val density = it.resources.displayMetrics.density
            val adWith = (adWithPixels / density).toInt()


            AdView(it).apply {
                setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(it, adWith))
                adUnitId = textId
                loadAd(AdRequest.Builder().build())
            }
        })
}