# ============================================================================
# Базовые настройки
# ============================================================================
# Сохраняем имена файлов/номера строк в стек-трейсах (важно для Crashlytics).
-keepattributes SourceFile,LineNumberTable
# Маскируем оригинальное имя исходного файла.
-renamesourcefileattribute SourceFile

# Аннотации нужны рантайму (Hilt, Room, Yandex SDK через рефлексию).
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses,EnclosingMethod
-keepattributes Exceptions

# ============================================================================
# Kotlin
# ============================================================================
-keep class kotlin.Metadata { *; }
-keep class kotlin.reflect.** { *; }
-dontwarn kotlin.**

# Coroutines — внутренние оптимизации
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# ============================================================================
# Hilt / Dagger
# ============================================================================
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel
-keep @dagger.Module class *
-keep @dagger.hilt.InstallIn class *
-keep @dagger.hilt.android.HiltAndroidApp class *
-keep @dagger.hilt.android.AndroidEntryPoint class *
-keepclasseswithmembers class * {
    @javax.inject.Inject <init>(...);
}
-dontwarn dagger.hilt.**
-dontwarn javax.inject.**

# ============================================================================
# Room
# ============================================================================
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao interface *
-keep class androidx.room.** { *; }
-keepclassmembers class * {
    @androidx.room.* <methods>;
}
-dontwarn androidx.room.paging.**

# Наши Room entity — сохраняем поля (нужно для рефлексии Room)
-keep class com.zelianko.kitchencalculator.data.Recipe { *; }
-keep class com.zelianko.kitchencalculator.data.ProductEn { *; }
-keep class com.zelianko.kitchencalculator.data.CookingTime { *; }
-keep class com.zelianko.kitchencalculator.data.RecipeDescription { *; }

# ============================================================================
# Compose / AndroidX
# ============================================================================
-dontwarn androidx.compose.**
-keep class androidx.compose.runtime.** { *; }

# DataStore
-keep class androidx.datastore.** { *; }

# Glance (App Widgets)
-keep class androidx.glance.** { *; }
-keep class * extends androidx.glance.appwidget.GlanceAppWidget
-keep class * extends androidx.glance.appwidget.GlanceAppWidgetReceiver
-dontwarn androidx.glance.**

# ============================================================================
# Yandex Mobile Ads + медиация (AppLovin / Bigo / Pangle / Tapjoy / IronSource)
# ============================================================================
# Yandex Ads нативно использует рефлексию для адаптеров медиации.
-keep class com.yandex.mobile.ads.** { *; }
-keep class com.yandex.android.** { *; }
-dontwarn com.yandex.mobile.ads.**
-dontwarn com.yandex.android.**

# Google Play Services Ads (тянется транзитивно)
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.android.gms.common.** { *; }
-dontwarn com.google.android.gms.**

# AppLovin
-keep class com.applovin.** { *; }
-dontwarn com.applovin.**

# Bigo Ads
-keep class sg.bigo.ads.** { *; }
-dontwarn sg.bigo.ads.**

# Pangle / ByteDance
-keep class com.bytedance.** { *; }
-keep class com.bykv.** { *; }
-dontwarn com.bytedance.**
-dontwarn com.bykv.**

# Tapjoy
-keep class com.tapjoy.** { *; }
-dontwarn com.tapjoy.**

# IronSource
-keep class com.ironsource.** { *; }
-dontwarn com.ironsource.**

# my.target (mediation)
-keep class com.my.target.** { *; }
-dontwarn com.my.target.**

# zMaticoo
-keep class com.zmaticoo.** { *; }
-dontwarn com.zmaticoo.**

# Chartboost
-keep class com.chartboost.** { *; }
-dontwarn com.chartboost.**

# AppNext
-keep class com.appnext.** { *; }
-dontwarn com.appnext.**

# ============================================================================
# AppMetrica
# ============================================================================
-keep class io.appmetrica.analytics.** { *; }
-keep interface io.appmetrica.analytics.** { *; }
-dontwarn io.appmetrica.analytics.**

# ============================================================================
# Firebase / Crashlytics
# ============================================================================
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

# ============================================================================
# Coil (загрузка картинок)
# ============================================================================
-dontwarn coil.**
-keep class coil.** { *; }

# ============================================================================
# Lottie
# ============================================================================
-keep class com.airbnb.lottie.** { *; }
-dontwarn com.airbnb.lottie.**

# ============================================================================
# Billing
# ============================================================================
-keep class com.android.billingclient.** { *; }
-dontwarn com.android.billingclient.**

# ============================================================================
# Apache Commons Lang3
# ============================================================================
-dontwarn org.apache.commons.lang3.**
-keep class org.apache.commons.lang3.** { *; }

# ============================================================================
# Sheets compose dialogs (maxkeppeler)
# ============================================================================
-dontwarn com.maxkeppeler.sheets.**

# ============================================================================
# WebView с JS (если WebView в Yandex/Crashlytics использует JS interface)
# ============================================================================
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
