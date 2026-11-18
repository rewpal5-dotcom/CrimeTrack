package com.crimetrack.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.crimetrack.app.ui.components.BottomBar
import com.crimetrack.app.ui.screens.DetailsScreen
import com.crimetrack.app.ui.screens.HistoryScreen
import com.crimetrack.app.ui.screens.HomeScreen
import com.crimetrack.app.ui.screens.MoreInfoScreen
import com.crimetrack.app.ui.screens.PremiumScreen
import com.crimetrack.app.ui.screens.ResultScreen
import com.crimetrack.app.ui.screens.SettingsScreen

@Composable
fun CrimeTrackApp(vm: AppViewModel) {
    val state = vm.state
    val showBottomBar = state.screen == Screen.Home ||
        state.screen == Screen.History ||
        state.screen == Screen.Premium ||
        state.screen == Screen.Settings

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar) {
                BottomBar(current = state.screen, onSelect = vm::selectTab)
            }
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (state.screen) {
                Screen.Home -> HomeScreen(vm)
                Screen.Result -> ResultScreen(vm)
                Screen.Details -> DetailsScreen(vm)
                Screen.MoreInfo -> MoreInfoScreen(vm)
                Screen.History -> HistoryScreen(vm)
                Screen.Premium -> PremiumScreen(vm)
                Screen.Settings -> SettingsScreen(vm)
            }
        }
    }
}
