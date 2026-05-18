package com.zelianko.kitchencalculator.ui.theme

import androidx.compose.ui.graphics.Color

// Брендовые цвета — оранжевый акцент, переехавший из colors.xml.
internal val BrandOrange = Color(0xFFEA6313)
internal val BrandOrangeLight = Color(0xFFFFCFA4)
internal val BrandOrangeDeep = Color(0xFFB8480A)

// Light palette
internal val md_light_primary = BrandOrange
internal val md_light_onPrimary = Color.White
internal val md_light_primaryContainer = BrandOrangeLight
internal val md_light_onPrimaryContainer = Color(0xFF2A1100)
internal val md_light_secondary = Color(0xFF7B5836)
internal val md_light_onSecondary = Color.White
internal val md_light_background = Color(0xFFFFFBFF)
internal val md_light_onBackground = Color(0xFF201A17)
internal val md_light_surface = Color(0xFFFFFBFF)
internal val md_light_onSurface = Color(0xFF201A17)
internal val md_light_surfaceVariant = Color(0xFFF4DED2)
internal val md_light_onSurfaceVariant = Color(0xFF52443B)
internal val md_light_outline = Color(0xFF85746A)

// Dark palette
internal val md_dark_primary = BrandOrangeLight
internal val md_dark_onPrimary = Color(0xFF4A2200)
internal val md_dark_primaryContainer = BrandOrangeDeep
internal val md_dark_onPrimaryContainer = BrandOrangeLight
internal val md_dark_secondary = Color(0xFFEDBE92)
internal val md_dark_onSecondary = Color(0xFF452B0E)
internal val md_dark_background = Color(0xFF201A17)
internal val md_dark_onBackground = Color(0xFFEDE0DA)
internal val md_dark_surface = Color(0xFF201A17)
internal val md_dark_onSurface = Color(0xFFEDE0DA)
internal val md_dark_surfaceVariant = Color(0xFF52443B)
internal val md_dark_onSurfaceVariant = Color(0xFFD7C2B6)
internal val md_dark_outline = Color(0xFFA08D82)

// Legacy palette (оставлено для совместимости с существующими import-ами).
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
