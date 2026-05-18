package com.zelianko.kitchencalculator.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate

private const val APP_PREFS_NAME = "app_prefs"

private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(APP_PREFS_NAME)

/** Preferences уровня приложения: что-то одно поле — что-то одно функция-доступа. */
object AppPreferences {

    private val KEY_LAST_LOTTIE_DATE = longPreferencesKey("last_lottie_date_epoch_day")

    /**
     * @return true, если Lottie-заставку уже показывали сегодня (по календарной дате
     * устройства). Используется чтобы не дёргать пользователя одной и той же анимацией
     * при каждом cold-старте в течение дня.
     */
    suspend fun wasLottieShownToday(context: Context): Boolean {
        val today = LocalDate.now().toEpochDay()
        val stored = context.appDataStore.data
            .map { it[KEY_LAST_LOTTIE_DATE] ?: -1L }
            .first()
        return stored == today
    }

    suspend fun markLottieShownToday(context: Context) {
        val today = LocalDate.now().toEpochDay()
        context.appDataStore.edit { it[KEY_LAST_LOTTIE_DATE] = today }
    }
}
