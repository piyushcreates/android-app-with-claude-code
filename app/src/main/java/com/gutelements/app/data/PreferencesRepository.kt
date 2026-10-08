package com.gutelements.app.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "preferences")

class PreferencesRepository(private val context: Context) {
    private val onboardingCompletedKey = booleanPreferencesKey("onboarding_completed")

    val onboardingCompleted: Flow<Boolean> =
        context.dataStore.data.map { it[onboardingCompletedKey] ?: false }

    suspend fun setOnboardingCompleted() {
        context.dataStore.edit { it[onboardingCompletedKey] = true }
    }
}
