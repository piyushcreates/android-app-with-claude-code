package com.gutelements.app.ui.insights

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gutelements.app.R
import com.gutelements.app.domain.BristolType
import com.gutelements.app.domain.Period
import com.gutelements.app.domain.PeriodStats
import com.gutelements.app.ui.AppViewModels
import com.gutelements.app.ui.components.BottomBarClearance
import com.gutelements.app.ui.components.BristolIllustration
import com.gutelements.app.ui.components.GutCard
import com.gutelements.app.ui.components.GutIcons
import com.gutelements.app.ui.components.ScreenPadding
import com.gutelements.app.ui.components.SectionLabel
import com.gutelements.app.ui.components.SegmentedControl
import com.gutelements.app.ui.components.TopHeader
import com.gutelements.app.ui.theme.Charcoal
import com.gutelements.app.ui.theme.LinenRecessed
import com.gutelements.app.ui.theme.Sage
import com.gutelements.app.ui.theme.SageSoft
import com.gutelements.app.ui.theme.TextMuted
import java.text.NumberFormat
import kotlin.math.roundToInt

@Composable
fun InsightsScreen(onSettings: () -> Unit, viewModel: InsightsViewModel = viewModel(factory = AppViewModels.Factory)) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val stats = state.stats

    Column(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = BottomBarClearance),
    ) {
        TopHeader(
            title = stringResource(R.string.insights_title),
            actionIcon = GutIcons.Settings,
            actionLabel = stringResource(R.string.settings),
            onAction = onSettings,
        )
        Column(Modifier.padding(horizontal = ScreenPadding)) {
            SegmentedControl(
                options = listOf(
                    stringResource(R.string.period_week),
                    stringResource(R.string.period_month),
                    stringResource(R.string.period_3months),
                ),
                selectedIndex = state.period.ordinal,
                onSelect = { viewModel.setPeriod(Period.entries[it]) },
                modifier = Modifier.padding(vertical = 8.dp),
            )
            Spacer(Modifier.height(12.dp))

            if (!state.loading && stats.total == 0) {
                GutCard(Modifier.fillMaxWidth(), contentPadding = PaddingValues(20.dp)) {
                    Text(stringResource(R.string.insights_empty), style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                }
            } else {
                Summary(state.period, stats)
                Spacer(Modifier.height(16.dp))
                StatCards(stats)
                Spacer(Modifier.height(16.dp))
                DistributionCard(stats)
            }
            Spacer(Modifier.height(16.dp))
            Text(
                stringResource(R.string.insights_note),
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private fun typesLabel(types: List<Int>): String = types.joinToString(", ")

@Composable
private fun Summary(period: Period, stats: PeriodStats) {
    val text = when {
        stats.mostCommon.isEmpty() -> return
        stats.mostCommon.size > 1 -> stringResource(R.string.insights_summary_tie, typesLabel(stats.mostCommon))
        else -> stringResource(
            when (period) {
                Period.WEEK -> R.string.insights_summary_week
                Period.MONTH -> R.string.insights_summary_month
                Period.THREE_MONTHS -> R.string.insights_summary_3months
            },
            stats.mostCommon.first(),
        )
    }
    Text(text, style = MaterialTheme.typography.titleMedium, color = Charcoal)
}

@Composable
private fun StatCards(stats: PeriodStats) {
    val perDay = NumberFormat.getNumberInstance().apply { maximumFractionDigits = 1; minimumFractionDigits = 1 }
        .format(stats.averagePerDay)
    Row(Modifier.height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        StatCard(
            label = stringResource(R.string.stat_total),
            value = "${stats.total}",
            caption = stringResource(R.string.stat_per_day, perDay),
            modifier = Modifier.weight(1f).fillMaxHeight(),
        )
        StatCard(
            label = stringResource(R.string.stat_type34),
            value = stats.type34Percent?.let { stringResource(R.string.percent, it) } ?: stringResource(R.string.no_value),
            caption = stringResource(R.string.stat_type34_caption),
            modifier = Modifier.weight(1f).fillMaxHeight(),
        )
    }
    Spacer(Modifier.height(12.dp))
    GutCard(Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                SectionLabel(stringResource(R.string.stat_most_common))
                Spacer(Modifier.height(6.dp))
                val types = stats.mostCommon
                Text(
                    if (types.size == 1) stringResource(R.string.type_number, types.first())
                    else stringResource(R.string.stat_most_common_tie, typesLabel(types)),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Charcoal,
                )
                if (types.size == 1) {
                    Text(stringResource(BristolType.of(types.first()).nameRes), style = MaterialTheme.typography.bodySmall, color = TextMuted)
                }
            }
            if (stats.mostCommon.size == 1) BristolIllustration(stats.mostCommon.first(), size = 52.dp, selected = true)
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, caption: String, modifier: Modifier = Modifier) {
    GutCard(modifier) {
        SectionLabel(label)
        Spacer(Modifier.height(6.dp))
        Text(value, style = MaterialTheme.typography.headlineMedium, color = Charcoal)
        Text(caption, style = MaterialTheme.typography.bodySmall, color = TextMuted)
    }
}

@Composable
private fun DistributionCard(stats: PeriodStats) {
    val max = (stats.distribution.maxOrNull() ?: 0).coerceAtLeast(1)
    GutCard(Modifier.fillMaxWidth(), contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 10.dp)) {
        Text(stringResource(R.string.chart_title), style = MaterialTheme.typography.titleMedium, color = Charcoal)
        Spacer(Modifier.height(12.dp))
        stats.distribution.forEachIndexed { index, count ->
            val type = index + 1
            val percent = if (stats.total == 0) 0 else (count * 100.0 / stats.total).roundToInt()
            val typeLabel = stringResource(R.string.type_number, type)
            val description = "$typeLabel: $count, $percent%"
            Row(
                Modifier.fillMaxWidth().padding(vertical = 5.dp).clearAndSetSemantics { contentDescription = description },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(typeLabel, style = MaterialTheme.typography.labelMedium, color = TextMuted, modifier = Modifier.width(56.dp))
                Bar(fraction = count / max.toFloat(), highlighted = type in stats.mostCommon, modifier = Modifier.weight(1f))
                Text(
                    "$count",
                    style = MaterialTheme.typography.labelMedium,
                    color = Charcoal,
                    textAlign = TextAlign.End,
                    modifier = Modifier.widthIn(min = 32.dp),
                )
            }
        }
    }
}

@Composable
private fun Bar(fraction: Float, highlighted: Boolean, modifier: Modifier = Modifier) {
    val animated by animateFloatAsState(fraction, tween(450), label = "bar")
    Box(modifier.height(12.dp).background(LinenRecessed, RoundedCornerShape(6.dp))) {
        if (animated > 0f) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animated)
                    .background(if (highlighted) Sage else SageSoft, RoundedCornerShape(6.dp)),
            )
        }
    }
}
