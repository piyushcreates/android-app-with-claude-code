package com.gutelements.app.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutelements.app.analytics.Analytics
import com.gutelements.app.analytics.AnalyticsEvent
import com.gutelements.app.data.BowelMovement
import com.gutelements.app.data.BowelMovementRepository
import com.gutelements.app.domain.PeriodStats
import com.gutelements.app.domain.computeStats
import com.gutelements.app.domain.inLastDays
import com.gutelements.app.ui.currentDateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

data class TodayUiState(
    val loading: Boolean = true,
    val date: LocalDate = LocalDate.now(),
    /** Today's entries, newest first. */
    val todayEntries: List<BowelMovement> = emptyList(),
    val week: PeriodStats = computeStats(emptyList(), 7),
)

class TodayViewModel(
    private val repository: BowelMovementRepository,
    private val analytics: Analytics,
) : ViewModel() {
    val state: StateFlow<TodayUiState> = combine(repository.entries, currentDateFlow()) { entries, today ->
        val zone = ZoneId.systemDefault()
        TodayUiState(
            loading = false,
            date = today,
            todayEntries = entries.inLastDays(1, today, zone),
            week = computeStats(entries.inLastDays(7, today, zone), 7),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TodayUiState())

    fun update(id: Long, bristolType: Int, timestamp: Long) = viewModelScope.launch {
        repository.update(id, bristolType, timestamp)
        analytics.track(AnalyticsEvent.ENTRY_EDITED)
    }

    fun delete(id: Long) = viewModelScope.launch {
        repository.delete(id)
        analytics.track(AnalyticsEvent.ENTRY_DELETED)
    }
}
