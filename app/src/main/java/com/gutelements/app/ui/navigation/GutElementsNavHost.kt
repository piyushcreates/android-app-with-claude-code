package com.gutelements.app.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gutelements.app.MainViewModel
import com.gutelements.app.R
import com.gutelements.app.analytics.AnalyticsEvent
import com.gutelements.app.ui.components.BottomBarItem
import com.gutelements.app.ui.components.GutBottomBar
import com.gutelements.app.ui.components.GutIcons
import com.gutelements.app.ui.history.HistoryScreen
import com.gutelements.app.ui.insights.InsightsScreen
import com.gutelements.app.ui.log.LogScreen
import com.gutelements.app.ui.onboarding.OnboardingScreen
import com.gutelements.app.ui.settings.SettingsScreen
import com.gutelements.app.ui.theme.Linen
import com.gutelements.app.ui.today.TodayScreen

object Routes {
    const val ONBOARDING = "onboarding"
    const val TODAY = "today"
    const val HISTORY = "history"
    const val INSIGHTS = "insights"
    const val LOG = "log"
    const val SETTINGS = "settings"
}

private val tabRoutes = setOf(Routes.TODAY, Routes.HISTORY, Routes.INSIGHTS)

@Composable
fun GutElementsNavHost(mainViewModel: MainViewModel) {
    val onboardingCompleted by mainViewModel.onboardingCompleted.collectAsStateWithLifecycle()
    val done = onboardingCompleted ?: run {
        // Preferences load in a few milliseconds; keep the linen canvas until then.
        Box(Modifier.fillMaxSize().background(Linen))
        return
    }

    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val route = backStack?.destination?.route
    // Keep the last tab highlighted while the bar animates out over Log/Settings.
    var activeTab by rememberSaveable { mutableStateOf(Routes.TODAY) }
    if (route in tabRoutes && route != activeTab) activeTab = route!!

    Box(Modifier.fillMaxSize().background(Linen)) {
        NavHost(
            navController = navController,
            startDestination = if (done) Routes.TODAY else Routes.ONBOARDING,
            modifier = Modifier.fillMaxSize(),
            enterTransition = { fadeIn(tween(180)) },
            exitTransition = { fadeOut(tween(180)) },
        ) {
            composable(Routes.ONBOARDING) {
                OnboardingScreen(
                    onStarted = { mainViewModel.track(AnalyticsEvent.ONBOARDING_STARTED) },
                    onFinished = {
                        mainViewModel.completeOnboarding()
                        navController.navigate(Routes.TODAY) { popUpTo(Routes.ONBOARDING) { inclusive = true } }
                    },
                )
            }
            composable(Routes.TODAY) {
                TodayScreen(
                    onLog = { navController.navigate(Routes.LOG) },
                    onSettings = { navController.navigate(Routes.SETTINGS) },
                )
            }
            composable(Routes.HISTORY) { HistoryScreen(onSettings = { navController.navigate(Routes.SETTINGS) }) }
            composable(Routes.INSIGHTS) { InsightsScreen(onSettings = { navController.navigate(Routes.SETTINGS) }) }
            composable(
                Routes.LOG,
                enterTransition = { slideInVertically(tween(260)) { it / 6 } + fadeIn(tween(200)) },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { slideOutVertically(tween(220)) { it / 6 } + fadeOut(tween(180)) },
            ) {
                LogScreen(onClose = { navController.popBackStack() })
            }
            composable(Routes.SETTINGS) { SettingsScreen(onBack = { navController.popBackStack() }) }
        }

        AnimatedVisibility(
            visible = route in tabRoutes,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = fadeIn(tween(200, delayMillis = 120)) + slideInVertically(tween(260, delayMillis = 120)) { it / 2 },
            exit = fadeOut(tween(120)) + slideOutVertically(tween(160)) { it / 2 },
        ) {
            GutBottomBar(
                items = listOf(
                    BottomBarItem(stringResource(R.string.nav_today), GutIcons.Today, activeTab == Routes.TODAY) { navController.switchTab(Routes.TODAY) },
                    BottomBarItem(stringResource(R.string.nav_history), GutIcons.History, activeTab == Routes.HISTORY) { navController.switchTab(Routes.HISTORY) },
                    BottomBarItem(stringResource(R.string.nav_insights), GutIcons.Insights, activeTab == Routes.INSIGHTS) { navController.switchTab(Routes.INSIGHTS) },
                ),
            )
        }
    }
}

private fun NavHostController.switchTab(route: String) {
    navigate(route) {
        // Today is always the root once onboarding is done.
        popUpTo(Routes.TODAY) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
