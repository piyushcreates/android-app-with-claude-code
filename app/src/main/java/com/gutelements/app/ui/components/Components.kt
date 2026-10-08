package com.gutelements.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gutelements.app.R
import com.gutelements.app.data.BowelMovement
import com.gutelements.app.domain.BristolType
import com.gutelements.app.ui.format.formatTime
import com.gutelements.app.ui.theme.BorderSubtle
import com.gutelements.app.ui.theme.CardWhite
import com.gutelements.app.ui.theme.Charcoal
import com.gutelements.app.ui.theme.LinenRecessed
import com.gutelements.app.ui.theme.Sage
import com.gutelements.app.ui.theme.TextMuted

val ScreenPadding = 20.dp
val CardShape = RoundedCornerShape(18.dp)

/** White resting card framed by a hairline border, per the design system. */
@Composable
fun GutCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    val border = BorderStroke(1.dp, BorderSubtle)
    if (onClick != null) {
        Surface(onClick = onClick, modifier = modifier, shape = CardShape, color = CardWhite, border = border) {
            Column(Modifier.padding(contentPadding), content = content)
        }
    } else {
        Surface(modifier = modifier, shape = CardShape, color = CardWhite, border = border) {
            Column(Modifier.padding(contentPadding), content = content)
        }
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth().heightIn(min = 56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Sage,
            disabledContainerColor = Sage.copy(alpha = 0.35f),
            disabledContentColor = CardWhite,
        ),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(10.dp))
        }
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

/** Recessed pill with a raised white segment for the selected option. */
@Composable
fun SegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(LinenRecessed)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        options.forEachIndexed { index, label ->
            val selected = index == selectedIndex
            val bg by animateColorAsState(if (selected) CardWhite else Color.Transparent, label = "segment")
            Box(
                Modifier
                    .weight(1f)
                    .heightIn(min = 40.dp)
                    .then(if (selected) Modifier.shadow(1.dp, RoundedCornerShape(10.dp), ambientColor = Charcoal.copy(alpha = 0.08f), spotColor = Charcoal.copy(alpha = 0.08f)) else Modifier)
                    .clip(RoundedCornerShape(10.dp))
                    .background(bg)
                    .semantics { this.selected = selected; role = Role.Tab }
                    .clickable { onSelect(index) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    label,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (selected) Charcoal else TextMuted,
                )
            }
        }
    }
}

/** Header row: wordmark or title on the left, optional action on the right. */
@Composable
fun TopHeader(
    title: String,
    modifier: Modifier = Modifier,
    wordmark: Boolean = false,
    navigationIcon: ImageVector? = null,
    navigationLabel: String? = null,
    onNavigate: () -> Unit = {},
    actionIcon: ImageVector? = null,
    actionLabel: String? = null,
    onAction: () -> Unit = {},
) {
    Row(
        modifier.fillMaxWidth().heightIn(min = 56.dp).padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (navigationIcon != null) {
            IconButton(onClick = onNavigate) { Icon(navigationIcon, navigationLabel) }
        } else {
            Spacer(Modifier.width(12.dp))
        }
        if (wordmark) {
            GutLogo(Modifier.size(28.dp))
            Spacer(Modifier.width(10.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, color = Charcoal, modifier = Modifier.weight(1f))
        } else {
            Text(title, style = MaterialTheme.typography.titleMedium, color = Charcoal, modifier = Modifier.weight(1f))
        }
        if (actionIcon != null) {
            IconButton(onClick = onAction) { Icon(actionIcon, actionLabel, tint = Charcoal) }
        }
    }
}

@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = TextMuted,
        modifier = modifier,
    )
}

/** Tappable card for one log entry: illustration, type, short name and time. */
@Composable
fun EntryCard(entry: BowelMovement, onClick: () -> Unit, modifier: Modifier = Modifier, trailingLabel: String? = null) {
    val type = BristolType.of(entry.bristolType)
    val context = LocalContext.current
    GutCard(modifier.fillMaxWidth(), onClick = onClick, contentPadding = PaddingValues(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BristolIllustration(type.number, size = 44.dp)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(stringResource(R.string.type_number, type.number), style = MaterialTheme.typography.titleSmall, color = Charcoal)
                Text(
                    stringResource(type.nameRes),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                trailingLabel ?: formatTime(context, entry.timestamp),
                style = MaterialTheme.typography.labelMedium,
                color = TextMuted,
            )
        }
    }
}

/** The Gut Elements brand mark (plum tile, cream G, coral dot). Decorative: the name is always shown beside it. */
@Composable
fun GutLogo(modifier: Modifier = Modifier) {
    Image(painterResource(R.drawable.ic_logo), contentDescription = null, modifier = modifier)
}

/** Small round marker, e.g. under calendar days with entries. */
@Composable
fun Dot(color: Color, modifier: Modifier = Modifier) {
    Box(modifier.size(5.dp).background(color, CircleShape))
}

/** Space to leave under scrolling tab content so the floating bottom bar never covers it. */
val BottomBarClearance = 104.dp
