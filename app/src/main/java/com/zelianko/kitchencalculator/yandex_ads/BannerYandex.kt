package com.zelianko.kitchencalculator.yandex_ads

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.window.layout.WindowMetricsCalculator
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import kotlin.math.roundToInt

@Composable
fun BannerSticky(id: String) {
    val context = LocalContext.current
    val bannerAdView = remember(context, id) {
        createBannerAdView(context, id, sticky = true, divider = 0)
    }

    AndroidView(factory = { bannerAdView })

    DisposableEffect(bannerAdView) {
        onDispose { bannerAdView.destroy() }
    }
}

@Composable
fun BannerInline(id: String, divider: Int) {
    val context = LocalContext.current
    val bannerAdView = remember(context, id, divider) {
        createBannerAdView(context, id, sticky = false, divider = divider)
    }

    AndroidView(factory = { bannerAdView })

    DisposableEffect(bannerAdView) {
        onDispose { bannerAdView.destroy() }
    }
}

private fun createBannerAdView(
    context: Context,
    adUnitId: String,
    sticky: Boolean,
    divider: Int,
): BannerAdView {
    val density = context.resources.displayMetrics.density

    var widthPx = context.resources.displayMetrics.widthPixels.toFloat()
    if (widthPx == 0f) {
        val bounds = WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(context).bounds
        widthPx = bounds.width().toFloat()
    }
    val widthDp = (widthPx / density).toInt()

    return BannerAdView(context).apply {
        setAdSize(
            if (sticky) {
                BannerAdSize.sticky(context, widthDp)
            } else {
                val screenHeightDp =
                    (context.resources.displayMetrics.heightPixels / density).roundToInt()
                val maxAdHeight = if (divider > 0) screenHeightDp / divider else screenHeightDp
                BannerAdSize.inline(context, widthDp, maxAdHeight)
            }
        )
        loadAd(AdRequest.Builder(adUnitId).build())
    }
}
