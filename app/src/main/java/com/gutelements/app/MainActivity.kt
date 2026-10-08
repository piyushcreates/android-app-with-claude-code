package com.gutelements.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.gutelements.app.analytics.AnalyticsEvent
import com.gutelements.app.ui.AppViewModels
import com.gutelements.app.ui.navigation.GutElementsNavHost
import com.gutelements.app.ui.theme.GutElementsTheme

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels { AppViewModels.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (savedInstanceState == null) mainViewModel.track(AnalyticsEvent.APP_OPENED)
        setContent {
            GutElementsTheme {
                GutElementsNavHost(mainViewModel)
            }
        }
    }
}
