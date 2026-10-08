package com.gutelements.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gutelements.app.ui.theme.BorderSubtle
import com.gutelements.app.ui.theme.Linen
import com.gutelements.app.ui.theme.Sage
import com.gutelements.app.ui.theme.TextMuted

data class BottomBarItem(val label: String, val icon: ImageVector, val selected: Boolean, val onClick: () -> Unit)

/** Floating three-destination pill bar ("Triad Tab Bar" in the design system). */
@Composable
fun GutBottomBar(items: List<BottomBarItem>, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .navigationBarsPadding()
            .padding(horizontal = ScreenPadding, vertical = 10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        color = Linen.copy(alpha = 0.97f),
        border = BorderStroke(1.dp, BorderSubtle),
        shadowElevation = 2.dp,
    ) {
        Row(
            Modifier.padding(horizontal = 8.dp, vertical = 6.dp).selectableGroup(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            items.forEach { item ->
                val tint by animateColorAsState(if (item.selected) Sage else TextMuted, label = "tab")
                Column(
                    Modifier
                        .weight(1f)
                        .heightIn(min = 52.dp)
                        .selectable(selected = item.selected, onClick = item.onClick, role = Role.Tab),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(item.icon, null, tint = tint, modifier = Modifier.size(22.dp))
                    Text(
                        item.label,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (item.selected) FontWeight.SemiBold else FontWeight.Medium,
                        color = tint,
                    )
                    Spacer(Modifier.size(3.dp))
                    Dot(Sage, Modifier.alpha(if (item.selected) 1f else 0f))
                }
            }
        }
    }
}
