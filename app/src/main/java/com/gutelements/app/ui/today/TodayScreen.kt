package com.gutelements.app.ui.today

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gutelements.app.R
import com.gutelements.app.ui.AppViewModels
import com.gutelements.app.ui.components.BottomBarClearance
import com.gutelements.app.ui.components.BristolIllustration
import com.gutelements.app.ui.components.EntryCard
import com.gutelements.app.ui.components.GutCard
import com.gutelements.app.ui.components.GutIcons
import com.gutelements.app.ui.components.PrimaryButton
import com.gutelements.app.ui.components.ScreenPadding
import com.gutelements.app.ui.components.SectionLabel
import com.gutelements.app.ui.components.TopHeader
import com.gutelements.app.ui.edit.EditEntrySheet
import com.gutelements.app.ui.format.formatLongDate
import com.gutelements.app.ui.format.formatTime
import com.gutelements.app.ui.theme.BorderSubtle
import com.gutelements.app.ui.theme.Charcoal
import com.gutelements.app.ui.theme.Sage
import com.gutelements.app.ui.theme.TextMuted

@Composable
fun TodayScreen(
    onLog: () -> Unit,
    onSettings: () -> Unit,
    viewModel: TodayViewModel = viewModel(factory = AppViewModels.Factory),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var editingId by rememberSaveable { mutableStateOf<Long?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().statusBarsPadding(),
        contentPadding = PaddingValues(bottom = BottomBarClearance),
    ) {
        item {
            TopHeader(
                title = stringResource(R.string.app_name),
                wordmark = true,
                actionIcon = GutIcons.Settings,
                actionLabel = stringResource(R.string.settings),
                onAction = onSettings,
            )
        }
        item {
            Column(Modifier.padding(horizontal = ScreenPadding)) {
                Spacer(Modifier.height(12.dp))
                SectionLabel(formatLongDate(state.date))
                Spacer(Modifier.height(6.dp))
                Text(stringResource(R.string.today_headline), style = MaterialTheme.typography.headlineMedium, color = Charcoal)
                Spacer(Modifier.height(20.dp))
                TodayCard(state)
                Spacer(Modifier.height(16.dp))
                PrimaryButton(
                    text = stringResource(R.string.log_bowel_movement),
                    icon = GutIcons.Add,
                    onClick = onLog,
                    modifier = Modifier.heightIn(min = 64.dp),
                )
                Spacer(Modifier.height(28.dp))
                WeekCard(state)
            }
        }
        if (state.todayEntries.isNotEmpty()) {
            item {
                SectionLabel(
                    stringResource(R.string.today_entries),
                    Modifier.padding(start = ScreenPadding, end = ScreenPadding, top = 28.dp, bottom = 10.dp),
                )
            }
            items(state.todayEntries, key = { it.id }) { entry ->
                EntryCard(
                    entry = entry,
                    onClick = { editingId = entry.id },
                    modifier = Modifier.padding(horizontal = ScreenPadding, vertical = 4.dp).animateItem(),
                )
            }
        }
    }

    val editing = state.todayEntries.firstOrNull { it.id == editingId }
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
private fun TodayCard(state: TodayUiState) {
    val context = LocalContext.current
    val count = state.todayEntries.size
    val latest = state.todayEntries.firstOrNull()
    GutCard(Modifier.fillMaxWidth(), contentPadding = PaddingValues(20.dp)) {
        SectionLabel(stringResource(R.string.today_card_label))
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text("$count", style = MaterialTheme.typography.displaySmall, color = if (count > 0) Sage else Charcoal)
            Spacer(Modifier.width(10.dp))
            Text(
                pluralStringResource(R.plurals.today_count, count),
                style = MaterialTheme.typography.bodyLarge,
                color = TextMuted,
                modifier = Modifier.padding(bottom = 6.dp),
            )
        }
        Spacer(Modifier.height(14.dp))
        HorizontalDivider(color = BorderSubtle)
        Spacer(Modifier.height(14.dp))
        if (latest != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BristolIllustration(latest.bristolType, size = 32.dp)
                Spacer(Modifier.width(12.dp))
                Text(
                    stringResource(R.string.today_latest, latest.bristolType, formatTime(context, latest.timestamp)),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Charcoal,
                )
            }
        } else if (!state.loading) {
            Text(stringResource(R.string.today_empty), style = MaterialTheme.typography.bodyMedium, color = TextMuted)
        }
    }
}

@Composable
private fun WeekCard(state: TodayUiState) {
    GutCard(Modifier.fillMaxWidth(), contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp)) {
        SectionLabel(stringResource(R.string.this_week))
        Spacer(Modifier.height(12.dp))
        Row(Modifier.height(IntrinsicSizeHeight), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            Metric(
                value = "${state.week.total}",
                label = stringResource(R.string.week_movements),
                modifier = Modifier.weight(1f),
            )
            VerticalDivider(color = BorderSubtle)
            Metric(
                value = state.week.type34Percent?.let { stringResource(R.string.percent, it) } ?: stringResource(R.string.no_value),
                label = stringResource(R.string.week_type34),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

private val IntrinsicSizeHeight = 52.dp

@Composable
private fun Metric(value: String, label: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(value, style = MaterialTheme.typography.headlineSmall, color = Charcoal)
        Text(label, style = MaterialTheme.typography.bodySmall, color = TextMuted)
    }
}
