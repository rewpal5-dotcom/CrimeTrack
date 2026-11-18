package com.crimetrack.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Green = Color(0xFF22C55E)
val GreenLight = Color(0xFF4ADE80)
val GreenInk = Color(0xFF04240F)

val ScreenDark = Color(0xFF0B100E)
val SurfaceDark = Color(0xFF141B18)
val SurfaceDark2 = Color(0xFF1B2421)
val LineDark = Color(0xFF26312C)
val TextDark = Color(0xFFF3F7F5)
val DimDark = Color(0xFF8D9A94)

val TileGreen = Color(0xFF16341F)
val TileAmber = Color(0xFF3A2A12)
val TileTeal = Color(0xFF12312F)
val TileBlue = Color(0xFF152A3D)
val TileViolet = Color(0xFF251C3A)
val TileRed = Color(0xFF3A1A1A)

val Amber = Color(0xFFF0A83C)
val Teal = Color(0xFF37C9B0)
val Blue = Color(0xFF4A9BEA)
val Violet = Color(0xFF9B8BF0)
val Red = Color(0xFFEF6B5E)

private val DarkColors = darkColorScheme(
    primary = Green,
    onPrimary = GreenInk,
    primaryContainer = TileGreen,
    onPrimaryContainer = GreenLight,
    secondary = GreenLight,
    background = ScreenDark,
    onBackground = TextDark,
    surface = SurfaceDark,
    onSurface = TextDark,
    surfaceVariant = SurfaceDark2,
    onSurfaceVariant = DimDark,
    outline = LineDark,
    error = Red
)

private val LightColors = lightColorScheme(
    primary = Color(0xFF15803D),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD7F5E0),
    onPrimaryContainer = Color(0xFF0A3D1F),
    secondary = Color(0xFF166534),
    background = Color(0xFFF5FAF7),
    onBackground = Color(0xFF0E1512),
    surface = Color.White,
    onSurface = Color(0xFF0E1512),
    surfaceVariant = Color(0xFFE7F0EA),
    onSurfaceVariant = Color(0xFF54635C),
    outline = Color(0xFFD3DEDA),
    error = Color(0xFFB3261E)
)

@Composable
fun CrimeTrackTheme(dark: Boolean = true, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (dark) DarkColors else LightColors,
        content = content
    )
}
