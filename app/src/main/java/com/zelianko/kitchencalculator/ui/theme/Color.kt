package com.zelianko.kitchencalculator.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================================================
// Брендовая палитра 2.0 — тёплая «кухонная» гамма: терракота + специи.
// Менее «кричащий» оранжевый, чем старый #EA6313, лучше читается и в свете, и в тьме.
// ============================================================================
internal val BrandTerracotta = Color(0xFFE35728)      // основной акцент (свет)
internal val BrandTerracottaLight = Color(0xFFFFB59B) // акцент для тёмной темы
internal val BrandTerracottaDeep = Color(0xFFB8430F)  // контейнер/нажатое состояние

// Light palette
internal val md_light_primary = BrandTerracotta
internal val md_light_onPrimary = Color.White
internal val md_light_primaryContainer = Color(0xFFFFDBCC)
internal val md_light_onPrimaryContainer = Color(0xFF3A0E00)
internal val md_light_secondary = Color(0xFF77574A)
internal val md_light_onSecondary = Color.White
internal val md_light_secondaryContainer = Color(0xFFFFDBCC)
internal val md_light_onSecondaryContainer = Color(0xFF2C160D)
internal val md_light_background = Color(0xFFFBF7F4)
internal val md_light_onBackground = Color(0xFF1C1714)
internal val md_light_surface = Color(0xFFFFFFFF)
internal val md_light_onSurface = Color(0xFF1C1714)
internal val md_light_surfaceVariant = Color(0xFFF2E7E0)
internal val md_light_onSurfaceVariant = Color(0xFF53443D)
internal val md_light_outline = Color(0xFF867169)
internal val md_light_outlineVariant = Color(0xFFE2D2C9)

// Dark palette
internal val md_dark_primary = BrandTerracottaLight
internal val md_dark_onPrimary = Color(0xFF541900)
internal val md_dark_primaryContainer = BrandTerracottaDeep
internal val md_dark_onPrimaryContainer = Color(0xFFFFDBCC)
internal val md_dark_secondary = Color(0xFFE6BEAD)
internal val md_dark_onSecondary = Color(0xFF44291E)
internal val md_dark_secondaryContainer = Color(0xFF5D4033)
internal val md_dark_onSecondaryContainer = Color(0xFFFFDBCC)
internal val md_dark_background = Color(0xFF16120F)
internal val md_dark_onBackground = Color(0xFFEDE0DA)
internal val md_dark_surface = Color(0xFF1E1A17)
internal val md_dark_onSurface = Color(0xFFEDE0DA)
internal val md_dark_surfaceVariant = Color(0xFF52443D)
internal val md_dark_onSurfaceVariant = Color(0xFFD7C2B7)
internal val md_dark_outline = Color(0xFFA08D83)
internal val md_dark_outlineVariant = Color(0xFF52443D)

// Legacy palette (оставлено для совместимости с существующими import-ами).
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
