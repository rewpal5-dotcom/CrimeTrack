package com.crimetrack.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import com.crimetrack.app.ui.AppViewModel
import com.crimetrack.app.ui.CrimeTrackApp
import com.crimetrack.app.ui.theme.CrimeTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: AppViewModel = viewModel()
            val context = LocalContext.current

            CrimeTrackTheme(dark = vm.state.darkTheme) {
                LaunchedEffect(vm.state.message) {
                    val msg = vm.state.message
                    if (msg != null) {
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        vm.clearMessage()
                    }
                }

                BackHandler(enabled = vm.state.screen != com.crimetrack.app.ui.Screen.Home) {
                    vm.back()
                }

                CrimeTrackApp(vm)
            }
        }
    }
}
