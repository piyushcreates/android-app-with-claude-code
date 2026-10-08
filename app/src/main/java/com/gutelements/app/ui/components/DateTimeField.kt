package com.gutelements.app.ui.components

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gutelements.app.R
import com.gutelements.app.ui.format.relativeDateTime
import com.gutelements.app.ui.format.toEpochMillis
import com.gutelements.app.ui.theme.Charcoal
import com.gutelements.app.ui.theme.Destructive
import com.gutelements.app.ui.theme.Sage
import com.gutelements.app.ui.theme.TextMuted
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

/** Card showing when an entry happened, with a date-then-time picker to change it. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeField(value: LocalDateTime, onChange: (LocalDateTime) -> Unit, isFuture: Boolean, modifier: Modifier = Modifier) {
    var step by rememberSaveable { mutableStateOf(PickerStep.None) }
    var pickedDate by rememberSaveable { mutableStateOf(value.toLocalDate()) }

    Column(modifier) {
        GutCard(Modifier.fillMaxWidth(), onClick = { step = PickerStep.Date }, contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(GutIcons.Clock, null, tint = Sage, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(stringResource(R.string.when_label), style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    Text(relativeDateTime(value.toEpochMillis()), style = MaterialTheme.typography.titleSmall, color = Charcoal)
                }
                Text(stringResource(R.string.change), style = MaterialTheme.typography.labelMedium, color = Sage)
            }
        }
        if (isFuture) {
            Text(
                stringResource(R.string.future_time_error),
                style = MaterialTheme.typography.bodySmall,
                color = Destructive,
                modifier = Modifier.padding(start = 4.dp, top = 6.dp),
            )
        }
    }

    when (step) {
        PickerStep.Date -> {
            val todayUtc = remember { LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli() }
            val state = rememberDatePickerState(
                initialSelectedDateMillis = value.toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
                selectableDates = object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long) = utcTimeMillis <= todayUtc
                    override fun isSelectableYear(year: Int) = year <= LocalDate.now().year
                },
            )
            DatePickerDialog(
                onDismissRequest = { step = PickerStep.None },
                confirmButton = {
                    TextButton(onClick = {
                        state.selectedDateMillis?.let {
                            pickedDate = java.time.Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()
                        }
                        step = PickerStep.Time
                    }) { Text(stringResource(R.string.next)) }
                },
                dismissButton = { TextButton(onClick = { step = PickerStep.None }) { Text(stringResource(R.string.cancel)) } },
            ) { DatePicker(state, showModeToggle = false) }
        }
        PickerStep.Time -> {
            val context = LocalContext.current
            val state = rememberTimePickerState(value.hour, value.minute, DateFormat.is24HourFormat(context))
            AlertDialog(
                onDismissRequest = { step = PickerStep.None },
                title = { Text(stringResource(R.string.select_time), style = MaterialTheme.typography.titleMedium) },
                text = { TimePicker(state) },
                confirmButton = {
                    TextButton(onClick = {
                        onChange(LocalDateTime.of(pickedDate, LocalTime.of(state.hour, state.minute)))
                        step = PickerStep.None
                    }) { Text(stringResource(R.string.ok)) }
                },
                dismissButton = { TextButton(onClick = { step = PickerStep.None }) { Text(stringResource(R.string.cancel)) } },
            )
        }
        PickerStep.None -> Unit
    }
}

private enum class PickerStep { None, Date, Time }
