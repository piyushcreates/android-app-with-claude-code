package com.gutelements.app.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/** Small outline icon set drawn on a 24-unit grid, so the app needs no icon library. */
object GutIcons {
    val Today by lazy {
        icon("today") {
            circle(12f, 12f, 4f)
            for ((x1, y1, x2, y2) in listOf(
                listOf(12f, 2.5f, 12f, 4.5f), listOf(12f, 19.5f, 12f, 21.5f),
                listOf(2.5f, 12f, 4.5f, 12f), listOf(19.5f, 12f, 21.5f, 12f),
                listOf(5.3f, 5.3f, 6.7f, 6.7f), listOf(17.3f, 17.3f, 18.7f, 18.7f),
                listOf(5.3f, 18.7f, 6.7f, 17.3f), listOf(17.3f, 6.7f, 18.7f, 5.3f),
            )) line(x1, y1, x2, y2)
        }
    }
    val History by lazy {
        icon("history") {
            circle(12f, 12f, 9f)
            moveTo(12f, 7.5f); lineTo(12f, 12f); lineTo(15f, 14f)
        }
    }
    val Insights by lazy {
        icon("insights") {
            line(5f, 20f, 5f, 13f); line(10f, 20f, 10f, 8f); line(15f, 20f, 15f, 11f); line(20f, 20f, 20f, 4.5f)
        }
    }
    val Settings by lazy {
        icon("settings") {
            line(4f, 7f, 20f, 7f); line(4f, 12f, 20f, 12f); line(4f, 17f, 20f, 17f)
            circle(15f, 7f, 1.8f); circle(8f, 12f, 1.8f); circle(13f, 17f, 1.8f)
        }
    }
    val Add by lazy { icon("add") { line(12f, 5f, 12f, 19f); line(5f, 12f, 19f, 12f) } }
    val Check by lazy { icon("check") { moveTo(5f, 12.5f); lineTo(10f, 17.5f); lineTo(19f, 7f) } }
    val Close by lazy { icon("close") { line(6f, 6f, 18f, 18f); line(18f, 6f, 6f, 18f) } }
    val Back by lazy { icon("back") { line(19f, 12f, 5f, 12f); moveTo(11f, 6f); lineTo(5f, 12f); lineTo(11f, 18f) } }
    val ChevronLeft by lazy { icon("chevron_left") { moveTo(14.5f, 6f); lineTo(8.5f, 12f); lineTo(14.5f, 18f) } }
    val ChevronRight by lazy { icon("chevron_right") { moveTo(9.5f, 6f); lineTo(15.5f, 12f); lineTo(9.5f, 18f) } }
    val Clock by lazy { History }
    val Calendar by lazy {
        icon("calendar") {
            moveTo(5f, 5.5f); lineTo(19f, 5.5f); lineTo(19f, 20f); lineTo(5f, 20f); close()
            line(5f, 10f, 19f, 10f); line(9f, 3.5f, 9f, 7f); line(15f, 3.5f, 15f, 7f)
        }
    }
    val Bolt by lazy { icon("bolt") { moveTo(13f, 3f); lineTo(6f, 13.5f); lineTo(11.5f, 13.5f); lineTo(10.5f, 21f); lineTo(18f, 10f); lineTo(12.5f, 10f); close() } }
    val Lock by lazy {
        icon("lock") {
            moveTo(6f, 11f); lineTo(18f, 11f); lineTo(18f, 20f); lineTo(6f, 20f); close()
            moveTo(8.5f, 11f); lineTo(8.5f, 8f); arcTo(3.5f, 3.5f, 0f, false, true, 15.5f, 8f); lineTo(15.5f, 11f)
        }
    }
    val Info by lazy { icon("info") { circle(12f, 12f, 9f); line(12f, 11f, 12f, 16.5f); line(12f, 7.6f, 12f, 7.7f) } }
    val External by lazy { icon("external") { line(9f, 15f, 19f, 5f); moveTo(13f, 5f); lineTo(19f, 5f); lineTo(19f, 11f); moveTo(17f, 14f); lineTo(17f, 19f); lineTo(5f, 19f); lineTo(5f, 7f); lineTo(10f, 7f) } }
    val Trash by lazy {
        icon("trash") {
            line(4.5f, 7f, 19.5f, 7f); moveTo(9.5f, 7f); lineTo(9.5f, 4.5f); lineTo(14.5f, 4.5f); lineTo(14.5f, 7f)
            moveTo(6.5f, 7f); lineTo(7.5f, 20f); lineTo(16.5f, 20f); lineTo(17.5f, 7f)
        }
    }

    private fun PathBuilder.line(x1: Float, y1: Float, x2: Float, y2: Float) { moveTo(x1, y1); lineTo(x2, y2) }

    private fun PathBuilder.circle(cx: Float, cy: Float, r: Float) {
        moveTo(cx - r, cy)
        arcToRelative(r, r, 0f, true, true, 2 * r, 0f)
        arcToRelative(r, r, 0f, true, true, -2 * r, 0f)
        close()
    }

    private fun icon(name: String, block: PathBuilder.() -> Unit): ImageVector =
        ImageVector.Builder(name, 24.dp, 24.dp, 24f, 24f).apply {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathBuilder = block,
            )
        }.build()
}
