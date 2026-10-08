package com.gutelements.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val GutColors = lightColorScheme(
    primary = Sage,
    onPrimary = CardWhite,
    primaryContainer = MatchaTint,
    onPrimaryContainer = SageDeep,
    secondary = SageDeep,
    onSecondary = CardWhite,
    secondaryContainer = SageTile,
    onSecondaryContainer = SageDeep,
    tertiary = Sage,
    onTertiary = CardWhite,
    tertiaryContainer = SageTile,
    onTertiaryContainer = SageDeep,
    background = Linen,
    onBackground = Charcoal,
    surface = Linen,
    onSurface = Charcoal,
    surfaceVariant = LinenRecessed,
    onSurfaceVariant = TextMuted,
    surfaceContainerLowest = CardWhite,
    surfaceContainerLow = CardWhite,
    surfaceContainer = CardWhite,
    surfaceContainerHigh = CardWhite,
    surfaceContainerHighest = LinenRecessed,
    outline = BorderSubtle,
    outlineVariant = BorderSubtle,
    error = Destructive,
    onError = CardWhite,
)

private val GutShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

/** Gut Elements is light-only for the MVP, matching the approved designs. */
@Composable
fun GutElementsTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = GutColors, typography = GutTypography, shapes = GutShapes, content = content)
}
