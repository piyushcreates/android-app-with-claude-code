package com.gutelements.app

import android.app.Application
import com.gutelements.app.analytics.Analytics
import com.gutelements.app.analytics.LogcatAnalytics
import com.gutelements.app.data.BowelMovementRepository
import com.gutelements.app.data.GutDatabase
import com.gutelements.app.data.PreferencesRepository

class GutElementsApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}

/** Manual dependency container; small enough that a DI framework isn't warranted. */
class AppContainer(app: Application) {
    private val database = GutDatabase.create(app)
    val entries = BowelMovementRepository(database.bowelMovementDao())
    val preferences = PreferencesRepository(app)
    // Debug builds log events to Logcat; release builds send nothing until a provider is chosen.
    val analytics: Analytics = if (BuildConfig.DEBUG) LogcatAnalytics() else Analytics {}
}
