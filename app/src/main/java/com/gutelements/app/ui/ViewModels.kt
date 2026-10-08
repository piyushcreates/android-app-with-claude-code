package com.gutelements.app.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gutelements.app.AppContainer
import com.gutelements.app.GutElementsApp
import com.gutelements.app.MainViewModel
import com.gutelements.app.ui.history.HistoryViewModel
import com.gutelements.app.ui.insights.InsightsViewModel
import com.gutelements.app.ui.log.LogViewModel
import com.gutelements.app.ui.settings.SettingsViewModel
import com.gutelements.app.ui.today.TodayViewModel

private fun CreationExtras.container(): AppContainer = (this[APPLICATION_KEY] as GutElementsApp).container

object AppViewModels {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer { MainViewModel(container().preferences, container().analytics) }
        initializer { TodayViewModel(container().entries, container().analytics) }
        initializer { LogViewModel(container().entries, container().analytics) }
        initializer { HistoryViewModel(container().entries, container().analytics) }
        initializer { InsightsViewModel(container().entries, container().analytics) }
        initializer { SettingsViewModel(container().entries) }
    }
}
