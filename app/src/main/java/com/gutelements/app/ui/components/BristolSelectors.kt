package com.gutelements.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.unit.dp
import com.gutelements.app.R
import com.gutelements.app.domain.BristolType
import com.gutelements.app.ui.theme.BorderSubtle
import com.gutelements.app.ui.theme.CardWhite
import com.gutelements.app.ui.theme.Charcoal
import com.gutelements.app.ui.theme.MatchaTint
import com.gutelements.app.ui.theme.Sage
import com.gutelements.app.ui.theme.TextMuted

/** Full-width tactile card for one Bristol type on the Log screen. */
@Composable
fun BristolOptionCard(type: BristolType, selected: Boolean, onSelect: () -> Unit, modifier: Modifier = Modifier) {
    val fill by animateColorAsState(if (selected) MatchaTint else CardWhite, label = "fill")
    val border by animateColorAsState(if (selected) Sage else BorderSubtle, label = "border")
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onSelect, role = Role.RadioButton),
        shape = CardShape,
        color = fill,
        border = BorderStroke(if (selected) 1.5.dp else 1.dp, border),
    ) {
        Row(Modifier.padding(12.dp).heightIn(min = 52.dp), verticalAlignment = Alignment.CenterVertically) {
            BristolIllustration(type.number, size = 52.dp, selected = selected)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    stringResource(R.string.type_number, type.number) + " · " + stringResource(type.nameRes),
                    style = MaterialTheme.typography.titleSmall,
                    color = Charcoal,
                )
                Text(stringResource(type.descriptionRes), style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
            Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                if (selected) {
                    Surface(shape = CircleShape, color = Sage, modifier = Modifier.size(24.dp)) {
                        Icon(GutIcons.Check, null, tint = CardWhite, modifier = Modifier.padding(4.dp))
                    }
                }
            }
        }
    }
}

/** Compact row of seven type tiles, used in the edit sheet. */
@Composable
fun BristolTypeRow(selected: Int, onSelect: (Int) -> Unit, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        BristolType.entries.forEach { type ->
            val isSelected = type.number == selected
            val label = stringResource(R.string.type_number, type.number) + ", " + stringResource(type.nameRes)
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .selectable(selected = isSelected, onClick = { onSelect(type.number) }, role = Role.RadioButton)
                    .clearAndSetSemantics { contentDescription = label; this.selected = isSelected; role = Role.RadioButton },
                shape = RoundedCornerShape(14.dp),
                color = if (isSelected) MatchaTint else CardWhite,
                border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, if (isSelected) Sage else BorderSubtle),
            ) {
                Column(Modifier.padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    BristolIllustration(type.number, size = 32.dp, selected = isSelected)
                    Spacer(Modifier.size(4.dp))
                    Text("${type.number}", style = MaterialTheme.typography.labelMedium, color = if (isSelected) Sage else TextMuted)
                }
            }
        }
    }
}
