package com.gutelements.app.ui.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gutelements.app.R
import com.gutelements.app.ui.AppViewModels
import com.gutelements.app.ui.components.BottomBarClearance
import com.gutelements.app.ui.components.Dot
import com.gutelements.app.ui.components.EntryCard
import com.gutelements.app.ui.components.GutCard
import com.gutelements.app.ui.components.GutIcons
import com.gutelements.app.ui.components.ScreenPadding
import com.gutelements.app.ui.components.SectionLabel
import com.gutelements.app.ui.components.SegmentedControl
import com.gutelements.app.ui.components.TopHeader
import com.gutelements.app.ui.edit.EditEntrySheet
import com.gutelements.app.ui.format.formatLongDate
import com.gutelements.app.ui.format.formatMonth
import com.gutelements.app.ui.format.relativeDay
import com.gutelements.app.ui.theme.CardWhite
import com.gutelements.app.ui.theme.Charcoal
import com.gutelements.app.ui.theme.Sage
import com.gutelements.app.ui.theme.TextMuted
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

@Composable
fun HistoryScreen(onSettings: () -> Unit, viewModel: HistoryViewModel = viewModel(factory = AppViewModels.Factory)) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var editingId by rememberSaveable { mutableStateOf<Long?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().statusBarsPadding(),
        contentPadding = PaddingValues(bottom = BottomBarClearance),
    ) {
        item {
            TopHeader(
                title = stringResource(R.string.history_title),
                actionIcon = GutIcons.Settings,
                actionLabel = stringResource(R.string.settings),
                onAction = onSettings,
            )
        }
        item {
            SegmentedControl(
                options = listOf(stringResource(R.string.calendar), stringResource(R.string.list)),
                selectedIndex = state.mode.ordinal,
                onSelect = { viewModel.setMode(HistoryMode.entries[it]) },
                modifier = Modifier.padding(horizontal = ScreenPadding, vertical = 8.dp),
            )
        }

        when (state.mode) {
            HistoryMode.CALENDAR -> {
                item {
                    MonthCalendar(
                        state = state,
                        onPrevious = { viewModel.showMonth(state.month.minusMonths(1)) },
                        onNext = { viewModel.showMonth(state.month.plusMonths(1)) },
                        onSelect = viewModel::selectDate,
                        modifier = Modifier.padding(horizontal = ScreenPadding, vertical = 8.dp),
                    )
                }
                item {
                    DayHeader(
                        title = formatLongDate(state.selectedDate),
                        count = state.selectedEntries.size,
                        modifier = Modifier.padding(start = ScreenPadding, end = ScreenPadding, top = 20.dp, bottom = 10.dp),
                    )
                }
                if (state.selectedEntries.isEmpty() && !state.loading) {
                    item { EmptyText(stringResource(R.string.history_day_empty)) }
                }
                items(state.selectedEntries, key = { it.id }) { entry ->
                    EntryCard(entry, onClick = { editingId = entry.id }, modifier = Modifier.padding(horizontal = ScreenPadding, vertical = 4.dp).animateItem())
                }
            }
            HistoryMode.LIST -> {
                if (state.byDate.isEmpty() && !state.loading) {
                    item { EmptyText(stringResource(R.string.history_empty), Modifier.padding(top = 24.dp)) }
                }
                state.byDate.forEach { (date, entries) ->
                    item(key = "header-$date") {
                        DayHeader(
                            title = relativeDay(date, state.today),
                            count = entries.size,
                            modifier = Modifier.padding(start = ScreenPadding, end = ScreenPadding, top = 20.dp, bottom = 8.dp),
                        )
                    }
                    items(entries, key = { it.id }) { entry ->
                        EntryCard(entry, onClick = { editingId = entry.id }, modifier = Modifier.padding(horizontal = ScreenPadding, vertical = 4.dp).animateItem())
                    }
                }
            }
        }
    }

    val editing = editingId?.let { id -> state.byDate.values.firstNotNullOfOrNull { list -> list.firstOrNull { it.id == id } } }
    if (editing != null) {
        EditEntrySheet(
            entry = editing,
            onDismiss = { editingId = null },
            onSave = { type, ts -> viewModel.update(editing.id, type, ts); editingId = null },
            onDelete = { viewModel.delete(editing.id); editingId = null },
        )
    }
}

@Composable
private fun DayHeader(title: String, count: Int, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(title, style = MaterialTheme.typography.titleMedium, color = Charcoal, modifier = Modifier.weight(1f))
        if (count > 0) {
            Text(pluralStringResource(R.plurals.entries_count, count, count), style = MaterialTheme.typography.labelMedium, color = TextMuted)
        }
    }
}

@Composable
private fun EmptyText(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        style = MaterialTheme.typography.bodyMedium,
        color = TextMuted,
        modifier = modifier.padding(horizontal = ScreenPadding, vertical = 8.dp),
    )
}

@Composable
private fun MonthCalendar(
    state: HistoryUiState,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onSelect: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val locale = Locale.getDefault()
    val firstDayOfWeek = remember(locale) { WeekFields.of(locale).firstDayOfWeek }
    val weekdays = remember(firstDayOfWeek) { (0L..6L).map { firstDayOfWeek.plus(it) } }
    val cells = remember(state.month, firstDayOfWeek) { monthCells(state.month, firstDayOfWeek) }

    GutCard(modifier.fillMaxWidth(), contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                formatMonth(state.month.atDay(1)),
                style = MaterialTheme.typography.titleMedium,
                color = Charcoal,
                modifier = Modifier.weight(1f).padding(start = 8.dp),
            )
            IconButton(onClick = onPrevious) { Icon(GutIcons.ChevronLeft, stringResource(R.string.previous_month), tint = Charcoal) }
            IconButton(onClick = onNext, enabled = state.canGoForward) {
                Icon(GutIcons.ChevronRight, stringResource(R.string.next_month), tint = if (state.canGoForward) Charcoal else TextMuted.copy(alpha = 0.4f))
            }
        }
        Row(Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 4.dp)) {
            weekdays.forEach { day ->
                Text(
                    day.getDisplayName(TextStyle.NARROW, locale),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
            }
        }
        cells.chunked(7).forEach { week ->
            Row(Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        if (date != null) {
                            DayCell(
                                date = date,
                                selected = date == state.selectedDate,
                                isToday = date == state.today,
                                hasEntries = state.byDate.containsKey(date),
                                enabled = !date.isAfter(state.today),
                                onClick = { onSelect(date) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DayCell(date: LocalDate, selected: Boolean, isToday: Boolean, hasEntries: Boolean, enabled: Boolean, onClick: () -> Unit) {
    val label = formatLongDate(date)
    Column(
        Modifier
            .padding(vertical = 2.dp)
            .clip(CircleShape)
            .clickable(enabled = enabled, onClick = onClick)
            .semantics { contentDescription = label }
            .alpha(if (enabled) 1f else 0.35f)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier
                .size(36.dp)
                .clip(CircleShape)
                .then(if (selected) Modifier.background(Sage) else Modifier)
                .then(if (isToday && !selected) Modifier.border(BorderStroke(1.dp, Sage), CircleShape) else Modifier),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                "${date.dayOfMonth}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (selected) CardWhite else Charcoal,
            )
        }
        Spacer(Modifier.height(3.dp))
        Dot(if (hasEntries) Sage else androidx.compose.ui.graphics.Color.Transparent)
    }
}

/** Calendar grid cells for [month], padded with nulls so weeks start on [firstDayOfWeek]. */
internal fun monthCells(month: YearMonth, firstDayOfWeek: DayOfWeek): List<LocalDate?> {
    val first = month.atDay(1)
    val leading = (first.dayOfWeek.value - firstDayOfWeek.value + 7) % 7
    val days = (1..month.lengthOfMonth()).map { month.atDay(it) }
    val cells = List(leading) { null } + days
    val trailing = (7 - cells.size % 7) % 7
    return cells + List(trailing) { null }
}
