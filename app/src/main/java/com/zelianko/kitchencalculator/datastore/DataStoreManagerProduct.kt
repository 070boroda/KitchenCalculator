package com.zelianko.kitchencalculator.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore


const val DATA_STORE_NAME = "preference_store_products"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATA_STORE_NAME)
class DataStoreManagerProduct(val context: Context) {
}