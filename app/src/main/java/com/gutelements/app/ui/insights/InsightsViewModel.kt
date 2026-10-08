package com.gutelements.app.ui.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutelements.app.analytics.Analytics
import com.gutelements.app.analytics.AnalyticsEvent
import com.gutelements.app.data.BowelMovementRepository
import com.gutelements.app.domain.Period
import com.gutelements.app.domain.PeriodStats
import com.gutelements.app.domain.computeStats
import com.gutelements.app.domain.inLastDays
import com.gutelements.app.ui.currentDateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.ZoneId

data class InsightsUiState(
    val loading: Boolean = true,
    val period: Period = Period.WEEK,
    val stats: PeriodStats = computeStats(emptyList(), Period.WEEK.days),
)

class InsightsViewModel(
    repository: BowelMovementRepository,
    analytics: Analytics,
) : ViewModel() {
    private val period = MutableStateFlow(Period.WEEK)

    val state: StateFlow<InsightsUiState> =
        combine(repository.entries, period, currentDateFlow()) { entries, period, today ->
            InsightsUiState(
                loading = false,
                period = period,
                stats = computeStats(entries.inLastDays(period.days, today, ZoneId.systemDefault()), period.days),
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), InsightsUiState())

    init {
        analytics.track(AnalyticsEvent.INSIGHTS_OPENED)
    }

    fun setPeriod(value: Period) {
        period.value = value
    }
}
