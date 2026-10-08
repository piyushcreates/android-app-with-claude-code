package com.gutelements.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutelements.app.analytics.Analytics
import com.gutelements.app.analytics.AnalyticsEvent
import com.gutelements.app.data.PreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val preferences: PreferencesRepository,
    private val analytics: Analytics,
) : ViewModel() {
    /** Null until preferences have loaded. */
    val onboardingCompleted: StateFlow<Boolean?> =
        preferences.onboardingCompleted.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun completeOnboarding() {
        analytics.track(AnalyticsEvent.ONBOARDING_COMPLETED)
        viewModelScope.launch { preferences.setOnboardingCompleted() }
    }

    fun track(event: AnalyticsEvent) = analytics.track(event)
}
