package com.gutelements.app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutelements.app.data.BowelMovementRepository
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: BowelMovementRepository) : ViewModel() {
    fun deleteAll(onDone: () -> Unit) = viewModelScope.launch {
        repository.deleteAll()
        onDone()
    }
}
