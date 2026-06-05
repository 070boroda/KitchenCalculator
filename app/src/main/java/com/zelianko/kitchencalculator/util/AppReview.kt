package com.zelianko.kitchencalculator.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import com.google.android.play.core.ktx.launchReview
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewManagerFactory

private const val TAG = "AppReview"

/**
 * Запускает нативный диалог оценки приложения (Google Play In-App Review).
 *
 * Особенности API, важные для понимания поведения:
 * - Google сам решает, показывать ли диалог (есть квоты на частоту). Поэтому
 *   диалог может НЕ появиться — это нормально, не баг.
 * - В дебаг-сборке (установка не из Play) реальный диалог не показывается;
 *   для проверки нужен внутренний тест-трек Play или FakeReviewManager.
 * - Любые ошибки гасим молча: оценка — необязательный поток, он не должен
 *   ломать пользовательский сценарий.
 */
suspend fun requestInAppReview(activity: Activity) {
    try {
        val manager = ReviewManagerFactory.create(activity)
        val reviewInfo = manager.requestReview()
        manager.launchReview(activity, reviewInfo)
    } catch (e: Exception) {
        Log.w(TAG, "In-app review skipped: ${e.message}")
    }
}

/** Достаёт Activity из Compose-контекста (LocalContext часто обёрнут в ContextWrapper). */
fun Context.findActivity(): Activity? {
    var ctx: Context = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}
