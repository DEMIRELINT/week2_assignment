package com.example.week2.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Fixed palette: wallpaper-derived dynamic colors intentionally stay disabled.
internal val DevClusterColors = darkColorScheme(
    primary = Color(0xFFC4BFFF),
    onPrimary = Color(0xFF232047),
    primaryContainer = Color(0xFF353154),
    onPrimaryContainer = Color(0xFFE7E2FF),
    inversePrimary = Color(0xFF5D568F),
    secondary = Color(0xFF91D5BD),
    onSecondary = Color(0xFF102F25),
    secondaryContainer = Color(0xFF223C34),
    onSecondaryContainer = Color(0xFFB4EED7),
    tertiary = Color(0xFFB8C7EB),
    onTertiary = Color(0xFF1F2E49),
    tertiaryContainer = Color(0xFF354260),
    onTertiaryContainer = Color(0xFFD9E3FF),
    background = Color(0xFF10131C),
    onBackground = Color(0xFFECEEF5),
    surface = Color(0xFF1C2030),
    onSurface = Color(0xFFECEEF5),
    surfaceVariant = Color(0xFF292E40),
    onSurfaceVariant = Color(0xFFB8BECE),
    surfaceTint = Color(0xFFC4BFFF),
    inverseSurface = Color(0xFFECEEF5),
    inverseOnSurface = Color(0xFF1C2030),
    error = Color(0xFFF2B8C4),
    onError = Color(0xFF511B2A),
    errorContainer = Color(0xFF703043),
    onErrorContainer = Color(0xFFFFD9E1),
    outline = Color(0xFF858DA3),
    outlineVariant = Color(0xFF3D4357),
    scrim = Color(0xFF000000),
    surfaceBright = Color(0xFF363B4D),
    surfaceDim = Color(0xFF10131C),
    surfaceContainerLowest = Color(0xFF0C0F17),
    surfaceContainerLow = Color(0xFF171B28),
    surfaceContainer = Color(0xFF1C2030),
    surfaceContainerHigh = Color(0xFF262B3D),
    surfaceContainerHighest = Color(0xFF31364A),
    primaryFixed = Color(0xFFE7E2FF),
    primaryFixedDim = Color(0xFFC4BFFF),
    onPrimaryFixed = Color(0xFF232047),
    onPrimaryFixedVariant = Color(0xFF353154),
    secondaryFixed = Color(0xFFB4EED7),
    secondaryFixedDim = Color(0xFF91D5BD),
    onSecondaryFixed = Color(0xFF102F25),
    onSecondaryFixedVariant = Color(0xFF223C34),
    tertiaryFixed = Color(0xFFD9E3FF),
    tertiaryFixedDim = Color(0xFFB8C7EB),
    onTertiaryFixed = Color(0xFF1F2E49),
    onTertiaryFixedVariant = Color(0xFF354260)
)

private val DevClusterTypography = Typography(
    headlineLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold, fontSize = 32.sp, lineHeight = 40.sp),
    headlineSmall = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold, fontSize = 24.sp, lineHeight = 32.sp),
    titleLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp),
    titleMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium, fontSize = 16.sp, lineHeight = 24.sp),
    bodyLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp),
    labelMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp)
)

@Composable
fun DevClusterTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DevClusterColors,
        typography = DevClusterTypography,
        shapes = Shapes(
            extraSmall = RoundedCornerShape(8.dp),
            small = RoundedCornerShape(12.dp),
            medium = RoundedCornerShape(16.dp),
            large = RoundedCornerShape(24.dp),
            extraLarge = RoundedCornerShape(28.dp)
        ),
        content = content
    )
}
