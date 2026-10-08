package com.gutelements.app.ui.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutelements.app.analytics.Analytics
import com.gutelements.app.analytics.AnalyticsEvent
import com.gutelements.app.data.BowelMovementRepository
import com.gutelements.app.ui.format.toEpochMillis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class LogUiState(
    val selectedType: Int? = null,
    val dateTime: LocalDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
    /** False while the time still means "now"; the save then uses the actual save moment. */
    val timeEdited: Boolean = false,
    val saving: Boolean = false,
) {
    val isFuture: Boolean get() = timeEdited && dateTime.toEpochMillis() > System.currentTimeMillis()
    val canSave: Boolean get() = selectedType != null && !isFuture && !saving
}

class LogViewModel(
    private val repository: BowelMovementRepository,
    private val analytics: Analytics,
) : ViewModel() {
    private val _state = MutableStateFlow(LogUiState())
    val state: StateFlow<LogUiState> = _state.asStateFlow()

    init {
        analytics.track(AnalyticsEvent.LOG_STARTED)
    }

    fun select(type: Int) {
        if (_state.value.selectedType != type) analytics.track(AnalyticsEvent.BRISTOL_TYPE_SELECTED)
        _state.update { it.copy(selectedType = type) }
    }

    fun setDateTime(value: LocalDateTime) = _state.update { it.copy(dateTime = value, timeEdited = true) }

    fun save(onSaved: () -> Unit) {
        val current = _state.value
        val type = current.selectedType ?: return
        if (!current.canSave) return
        _state.update { it.copy(saving = true) }
        viewModelScope.launch {
            val timestamp = if (current.timeEdited) current.dateTime.toEpochMillis() else System.currentTimeMillis()
            repository.log(type, timestamp)
            analytics.track(AnalyticsEvent.BOWEL_MOVEMENT_SAVED)
            onSaved()
        }
    }
}
