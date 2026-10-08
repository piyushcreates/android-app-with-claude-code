package com.gutelements.app.ui.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gutelements.app.R
import com.gutelements.app.data.BowelMovement
import com.gutelements.app.ui.components.BristolTypeRow
import com.gutelements.app.ui.components.DateTimeField
import com.gutelements.app.ui.components.GutIcons
import com.gutelements.app.ui.components.PrimaryButton
import com.gutelements.app.ui.components.ScreenPadding
import com.gutelements.app.ui.format.toEpochMillis
import com.gutelements.app.ui.format.toLocalDateTime
import com.gutelements.app.ui.theme.CardWhite
import com.gutelements.app.ui.theme.Charcoal
import com.gutelements.app.ui.theme.Destructive
import com.gutelements.app.ui.theme.Linen
import kotlinx.coroutines.launch

/** Minimal edit sheet: type, date/time, save, delete. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEntrySheet(
    entry: BowelMovement,
    onDismiss: () -> Unit,
    onSave: (bristolType: Int, timestamp: Long) -> Unit,
    onDelete: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var type by rememberSaveable(entry.id) { mutableIntStateOf(entry.bristolType) }
    var dateTime by rememberSaveable(entry.id) { mutableStateOf(entry.timestamp.toLocalDateTime()) }
    var confirmDelete by remember { mutableStateOf(false) }
    val isFuture = dateTime.toEpochMillis() > System.currentTimeMillis()

    fun close(then: () -> Unit) {
        scope.launch { sheetState.hide() }.invokeOnCompletion { then() }
    }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState, containerColor = Linen) {
        Column(Modifier.padding(horizontal = ScreenPadding).navigationBarsPadding().padding(bottom = 12.dp)) {
            Text(stringResource(R.string.edit_entry), style = MaterialTheme.typography.titleLarge, color = Charcoal)
            Spacer(Modifier.size(16.dp))
            BristolTypeRow(selected = type, onSelect = { type = it })
            Spacer(Modifier.size(12.dp))
            DateTimeField(value = dateTime, onChange = { dateTime = it }, isFuture = isFuture)
            Spacer(Modifier.size(20.dp))
            PrimaryButton(
                text = stringResource(R.string.save_changes),
                enabled = !isFuture,
                onClick = { close { onSave(type, dateTime.toEpochMillis()) } },
            )
            TextButton(
                onClick = { confirmDelete = true },
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).padding(top = 4.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(GutIcons.Trash, null, tint = Destructive, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.delete_entry), color = Destructive, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            containerColor = CardWhite,
            title = { Text(stringResource(R.string.delete_entry_confirm_title)) },
            text = { Text(stringResource(R.string.delete_entry_confirm_body)) },
            confirmButton = {
                TextButton(onClick = { confirmDelete = false; close(onDelete) }) {
                    Text(stringResource(R.string.delete), color = Destructive)
                }
            },
            dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text(stringResource(R.string.cancel)) } },
        )
    }
}
