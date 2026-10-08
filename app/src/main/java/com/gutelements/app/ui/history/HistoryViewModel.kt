package com.gutelements.app.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutelements.app.analytics.Analytics
import com.gutelements.app.analytics.AnalyticsEvent
import com.gutelements.app.data.BowelMovement
import com.gutelements.app.data.BowelMovementRepository
import com.gutelements.app.domain.localDate
import com.gutelements.app.ui.currentDateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

enum class HistoryMode { CALENDAR, LIST }

data class HistoryUiState(
    val loading: Boolean = true,
    val today: LocalDate = LocalDate.now(),
    val mode: HistoryMode = HistoryMode.CALENDAR,
    val month: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    /** Entries grouped by local date, newest date first; each day's entries newest first. */
    val byDate: Map<LocalDate, List<BowelMovement>> = emptyMap(),
) {
    val selectedEntries: List<BowelMovement> get() = byDate[selectedDate].orEmpty()
    val canGoForward: Boolean get() = month < YearMonth.from(today)
}

private data class Selection(val mode: HistoryMode, val month: YearMonth, val date: LocalDate)

class HistoryViewModel(
    private val repository: BowelMovementRepository,
    private val analytics: Analytics,
) : ViewModel() {
    private val selection = MutableStateFlow(Selection(HistoryMode.CALENDAR, YearMonth.now(), LocalDate.now()))

    val state: StateFlow<HistoryUiState> =
        combine(repository.entries, selection, currentDateFlow()) { entries, sel, today ->
            val zone = ZoneId.systemDefault()
            HistoryUiState(
                loading = false,
                today = today,
                mode = sel.mode,
                month = sel.month,
                selectedDate = sel.date,
                byDate = entries.groupBy { it.localDate(zone) },
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HistoryUiState())

    init {
        analytics.track(AnalyticsEvent.HISTORY_OPENED)
    }

    fun setMode(mode: HistoryMode) = selection.update { it.copy(mode = mode) }

    fun showMonth(month: YearMonth) {
        if (month > YearMonth.now()) return
        selection.update { it.copy(month = month) }
    }

    fun selectDate(date: LocalDate) {
        if (date.isAfter(LocalDate.now())) return
        selection.update { it.copy(date = date, month = YearMonth.from(date)) }
    }

    fun update(id: Long, bristolType: Int, timestamp: Long) = viewModelScope.launch {
        repository.update(id, bristolType, timestamp)
        analytics.track(AnalyticsEvent.ENTRY_EDITED)
    }

    fun delete(id: Long) = viewModelScope.launch {
        repository.delete(id)
        analytics.track(AnalyticsEvent.ENTRY_DELETED)
    }
}
