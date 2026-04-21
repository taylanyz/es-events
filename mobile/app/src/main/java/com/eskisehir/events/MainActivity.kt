package com.eskisehir.events

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.eskisehir.events.presentation.navigation.AppNavigation
import com.eskisehir.events.presentation.theme.EskisehirEventsTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single-Activity architecture entry point.
 * All screens are rendered as Composable destinations via Navigation Compose.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EskisehirEventsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}
