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
    primary = HealthcareTealDarkTheme,
    onPrimary = Color(0xFF003730),
    primaryContainer = HealthcareTealDark,
    onPrimaryContainer = HealthcareTealLight,
    secondary = MedicalBlueDarkTheme,
    onSecondary = Color(0xFF00344F),
    secondaryContainer = MedicalBlueDark,
    onSecondaryContainer = MedicalBlueLight,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = EmergencyRedDark,
    onErrorContainer = EmergencyRedLight,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = Color(0xFF3F4947),
    onBackground = Color(0xFFE1E3E2),
    onSurface = Color(0xFFE1E3E2)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = HealthcareTeal,
    onPrimary = Color.White,
    primaryContainer = HealthcareTealLight,
    onPrimaryContainer = HealthcareTealDark,
    secondary = MedicalBlue,
    onSecondary = Color.White,
    secondaryContainer = MedicalBlueLight,
    onSecondaryContainer = MedicalBlueDark,
    tertiary = SuccessGreen,
    error = EmergencyRed,
    onError = Color.White,
    errorContainer = EmergencyRedLight,
    onErrorContainer = EmergencyRedDark,
    background = BackgroundLight,
    surface = SurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onBackground = OnSurfaceLight,
    onSurface = OnSurfaceLight,
    outline = OutlineLight
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep branded healthcare teal/blue
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
