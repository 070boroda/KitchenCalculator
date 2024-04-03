package com.zelianko.kitchencalculator.google_ads

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

/**
 * Баннер рекламы гугл
 */
@Composable
fun GoogleBannerAd(
    modifier: Modifier = Modifier,
    textId: String = "ca-app-pub-3940256099942544/9214589741"
) {
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = {
        AdView(it).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = textId
            loadAd(AdRequest.Builder().build())
        }
    })
}