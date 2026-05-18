package com.zelianko.kitchencalculator.yandex_ads

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mobile.ads.common.AdBindingResult
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.nativeads.MediaView
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdEventListener
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener
import com.yandex.mobile.ads.nativeads.NativeAdLoader
import com.yandex.mobile.ads.nativeads.NativeAdView
import com.yandex.mobile.ads.nativeads.NativeAdViewBinder
import com.zelianko.kitchencalculator.R

/**
 * Compose-обёртка для нативной рекламы Яндекса (SDK 8.0.0).
 *
 * Реклама подгружается один раз при первом появлении в композиции.
 * Если SDK не вернул объявление — composable не рендерит ничего (slot пропадает).
 */
@Composable
fun NativeAdRecipeItem(
    adUnitId: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val loader = remember(adUnitId) { NativeAdLoader(context) }
    var loadedAd by remember(adUnitId) { mutableStateOf<NativeAd?>(null) }
    var failed by remember(adUnitId) { mutableStateOf(false) }

    LaunchedEffect(adUnitId) {
        loader.loadAd(
            AdRequest.Builder(adUnitId).build(),
            object : NativeAdLoadListener {
                override fun onAdLoaded(nativeAd: NativeAd) {
                    loadedAd = nativeAd
                }

                override fun onAdFailedToLoad(error: AdRequestError) {
                    Log.d(TAG, "Native ad failed: ${error.code} ${error.description}")
                    failed = true
                }
            }
        )
    }

    DisposableEffect(adUnitId) {
        onDispose {
            loadedAd?.setNativeAdEventListener(null)
            loadedAd = null
        }
    }

    val ad = loadedAd
    if (ad == null || failed) return

    // key() гарантирует пересоздание AndroidView при смене рекламы.
    key(ad) {
        NativeAdCard(ad, modifier.fillMaxWidth())
    }
}

@SuppressLint("InflateParams")
@Composable
private fun NativeAdCard(nativeAd: NativeAd, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val view = LayoutInflater.from(context)
                .inflate(R.layout.view_native_ad_recipe, null, false) as NativeAdView

            val binder = NativeAdViewBinder.Builder(view)
                .setTitleView(view.findViewById<TextView>(R.id.title))
                .setBodyView(view.findViewById<TextView>(R.id.body))
                .setCallToActionView(view.findViewById<Button>(R.id.call_to_action))
                .setIconView(view.findViewById<ImageView>(R.id.icon))
                .setFaviconView(view.findViewById<ImageView>(R.id.favicon))
                .setSponsoredView(view.findViewById<TextView>(R.id.sponsored))
                .setDomainView(view.findViewById<TextView>(R.id.domain))
                .setAgeView(view.findViewById<TextView>(R.id.age))
                .setWarningView(view.findViewById<TextView>(R.id.warning))
                .setFeedbackView(view.findViewById<ImageView>(R.id.feedback))
                .setMediaView(view.findViewById<MediaView>(R.id.media))
                .setPriceView(view.findViewById<TextView>(R.id.price))
                .setRatingView(view.findViewById(R.id.rating))
                .setReviewCountView(view.findViewById<TextView>(R.id.review_count))
                .build()

            when (val result = nativeAd.bindNativeAd(binder)) {
                is AdBindingResult.Failure -> Log.e(
                    TAG,
                    "Bind failed: missingAsset=${result.missingAssetName}, msg=${result.exception.message}"
                )
                AdBindingResult.Success -> Unit
            }

            nativeAd.setNativeAdEventListener(object : NativeAdEventListener {
                override fun onAdClicked() = Unit
                override fun onImpression(impressionData: ImpressionData?) = Unit
            })

            view
        }
    )
}

private const val TAG = "NativeAdRecipeItem"
