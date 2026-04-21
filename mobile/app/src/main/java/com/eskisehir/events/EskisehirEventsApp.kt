package com.eskisehir.events

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class annotated with @HiltAndroidApp to enable
 * Hilt dependency injection throughout the app.
 */
@HiltAndroidApp
class EskisehirEventsApp : Application()
