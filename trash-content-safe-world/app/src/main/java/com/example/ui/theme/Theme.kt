package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = DarkPrimary,
    onPrimary = Color(0xFF022C22),
    primaryContainer = EcoPrimaryDark,
    onPrimaryContainer = EcoMintLight,
    secondary = DarkSecondary,
    onSecondary = Color(0xFF082F49),
    background = DarkBg,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onBackground = Color(0xFFE2E8F0),
    onSurface = Color(0xFFE2E8F0),
  )

private val LightColorScheme =
  lightColorScheme(
    primary = EcoPrimary,
    onPrimary = Color.White,
    primaryContainer = EcoMintLight,
    onPrimaryContainer = EcoPrimaryDark,
    secondary = EcoSecondary,
    onSecondary = Color.White,
    secondaryContainer = EcoSecondaryLight,
    onSecondaryContainer = Color(0xFF0369A1),
    tertiary = EcoTertiary,
    onTertiary = Color.White,
    tertiaryContainer = EcoTertiaryLight,
    background = SafeSpaceBg,
    surface = SafeCardBg,
    surfaceVariant = Color(0xFFF1F5F9),
    onBackground = SafeTextPrimary,
    onSurface = SafeTextPrimary,
    onSurfaceVariant = SafeTextSecondary,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep bespoke branding consistent
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
