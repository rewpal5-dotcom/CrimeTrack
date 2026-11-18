package com.crimetrack.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crimetrack.app.ui.AppViewModel
import com.crimetrack.app.ui.components.SectionTitle
import com.crimetrack.app.ui.components.TopBar
import com.crimetrack.app.ui.theme.Green

/** Settings screen — theme, demo mode, data clear, about. */
@Composable
fun SettingsScreen(vm: AppViewModel) {
    val state = vm.state
    var confirmClear by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        TopBar(title = "Settings")
        Spacer(Modifier.height(4.dp))

        SectionTitle("Appearance")
        Spacer(Modifier.height(10.dp))
        SettingRow(
            title = "Dark theme",
            subtitle = "CrimeTrack ka default look",
            checked = state.darkTheme,
            onCheckedChange = vm::setDarkTheme
        )

        Spacer(Modifier.height(18.dp))
        SectionTitle("Data")
        Spacer(Modifier.height(10.dp))
        SettingRow(
            title = "Demo mode",
            subtitle = "Offline demo database se results",
            checked = state.demoMode,
            onCheckedChange = vm::setDemoMode
        )
        Spacer(Modifier.height(9.dp))
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = { confirmClear = true }) {
                Text("Saara local data clear karein", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(Modifier.height(18.dp))
        SectionTitle("About")
        Spacer(Modifier.height(10.dp))
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(14.dp)) {
                Text("CrimeTrack", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Green)
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Version 1.0 — number info finder ka Android app. Abhi ye demo database par chalta hai; asli lookup API aap data/Lookup.kt me jod sakte hain.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "github.com/rewpal5-dotcom/CrimeTrack",
                    fontSize = 10.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(Modifier.height(24.dp))
    }

    if (confirmClear) {
        AlertDialog(
            onDismissRequest = { confirmClear = false },
            title = { Text("Sab data clear karein?") },
            text = { Text("History, saved contacts, premium aur settings sab reset ho jayenge.") },
            confirmButton = {
                TextButton(onClick = {
                    vm.clearAllData()
                    confirmClear = false
                }) { Text("Clear") }
            },
            dismissButton = {
                TextButton(onClick = { confirmClear = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun SettingRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
                Text(title, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    subtitle,
                    fontSize = 10.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}
