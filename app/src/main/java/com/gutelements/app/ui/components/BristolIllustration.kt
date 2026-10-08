package com.gutelements.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gutelements.app.ui.theme.Clay
import com.gutelements.app.ui.theme.ClayTile
import com.gutelements.app.ui.theme.Sage
import com.gutelements.app.ui.theme.SageTile

/** Abstract, clinical-style drawing of a Bristol type on a soft rounded tile. */
@Composable
fun BristolIllustration(type: Int, modifier: Modifier = Modifier, size: Dp = 48.dp, selected: Boolean = false) {
    val tile = if (selected) SageTile else ClayTile
    val ink = if (selected) Sage else Clay
    Box(
        modifier
            .size(size)
            .background(tile, RoundedCornerShape(size * 0.3f)),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.fillMaxSize().padding(size * 0.12f)) { drawBristol(type, ink, tile) }
    }
}

private fun DrawScope.drawBristol(type: Int, ink: Color, background: Color) {
    val s = size.minDimension
    fun p(x: Float, y: Float) = Offset(x * s, y * s)
    fun dot(x: Float, y: Float, r: Float) = drawCircle(ink, r * s, p(x, y))
    fun capsule(x1: Float, x2: Float, cy: Float, h: Float) = drawRoundRect(
        ink, p(x1, cy - h / 2), Size((x2 - x1) * s, h * s), CornerRadius(h * s / 2)
    )

    when (type) {
        1 -> {
            dot(0.30f, 0.36f, 0.10f); dot(0.60f, 0.30f, 0.09f); dot(0.46f, 0.60f, 0.11f)
            dot(0.74f, 0.58f, 0.08f); dot(0.24f, 0.68f, 0.07f)
        }
        2 -> {
            capsule(0.16f, 0.84f, 0.5f, 0.18f)
            listOf(0.24f, 0.41f, 0.59f, 0.76f).forEach { dot(it, 0.5f, 0.12f) }
        }
        3 -> {
            capsule(0.14f, 0.86f, 0.5f, 0.24f)
            val crack = Stroke(width = 0.035f * s, cap = StrokeCap.Round)
            listOf(0.34f, 0.5f, 0.66f).forEach { x ->
                drawLine(background, p(x, 0.38f), p(x + 0.03f, 0.47f), crack.width, StrokeCap.Round)
            }
            drawLine(background, p(0.42f, 0.62f), p(0.45f, 0.56f), crack.width, StrokeCap.Round)
            drawLine(background, p(0.58f, 0.62f), p(0.61f, 0.56f), crack.width, StrokeCap.Round)
        }
        4 -> {
            val snake = Path().apply {
                moveTo(0.16f * s, 0.6f * s)
                cubicTo(0.36f * s, 0.34f * s, 0.62f * s, 0.68f * s, 0.84f * s, 0.4f * s)
            }
            drawPath(snake, ink, style = Stroke(width = 0.22f * s, cap = StrokeCap.Round))
        }
        5 -> {
            drawOval(ink, p(0.14f, 0.28f), Size(0.36f * s, 0.24f * s))
            drawOval(ink, p(0.52f, 0.24f), Size(0.32f * s, 0.24f * s))
            drawOval(ink, p(0.32f, 0.56f), Size(0.38f * s, 0.24f * s))
        }
        6 -> {
            listOf(
                Triple(0.22f, 0.58f, 0.10f), Triple(0.34f, 0.48f, 0.11f), Triple(0.48f, 0.42f, 0.12f),
                Triple(0.62f, 0.47f, 0.11f), Triple(0.76f, 0.57f, 0.10f), Triple(0.40f, 0.62f, 0.10f),
                Triple(0.58f, 0.62f, 0.10f), Triple(0.30f, 0.36f, 0.06f), Triple(0.70f, 0.36f, 0.05f),
                Triple(0.86f, 0.66f, 0.05f), Triple(0.13f, 0.68f, 0.05f),
            ).forEach { (x, y, r) -> dot(x, y, r) }
        }
        else -> {
            drawOval(ink, p(0.12f, 0.56f), Size(0.76f * s, 0.2f * s))
            val drop = Path().apply {
                moveTo(0.5f * s, 0.12f * s)
                cubicTo(0.58f * s, 0.26f * s, 0.64f * s, 0.32f * s, 0.64f * s, 0.38f * s)
                cubicTo(0.64f * s, 0.46f * s, 0.36f * s, 0.46f * s, 0.36f * s, 0.38f * s)
                cubicTo(0.36f * s, 0.32f * s, 0.42f * s, 0.26f * s, 0.5f * s, 0.12f * s)
                close()
            }
            drawPath(drop, ink)
        }
    }
}
