package com.gutelements.app.analytics

import android.util.Log

/**
 * Product events from the roadmap brief. Events never carry health values
 * (no Bristol type, no timestamps). Swap [LogcatAnalytics] for a real
 * provider once one is chosen.
 */
enum class AnalyticsEvent(val key: String) {
    APP_OPENED("app_opened"),
    ONBOARDING_STARTED("onboarding_started"),
    ONBOARDING_COMPLETED("onboarding_completed"),
    LOG_STARTED("log_started"),
    BRISTOL_TYPE_SELECTED("bristol_type_selected"),
    BOWEL_MOVEMENT_SAVED("bowel_movement_saved"),
    ENTRY_EDITED("entry_edited"),
    ENTRY_DELETED("entry_deleted"),
    HISTORY_OPENED("history_opened"),
    INSIGHTS_OPENED("insights_opened"),
}

fun interface Analytics {
    fun track(event: AnalyticsEvent)
}

class LogcatAnalytics : Analytics {
    override fun track(event: AnalyticsEvent) {
        Log.d("Analytics", event.key)
    }
}
