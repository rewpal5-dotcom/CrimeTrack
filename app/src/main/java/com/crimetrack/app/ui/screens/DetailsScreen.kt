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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.crimetrack.app.data.DemoData
import com.crimetrack.app.ui.AppViewModel
import com.crimetrack.app.ui.Screen
import com.crimetrack.app.ui.components.DetailRow
import com.crimetrack.app.ui.components.TabRow3
import com.crimetrack.app.ui.components.TopBar
import com.crimetrack.app.ui.theme.Green
import com.crimetrack.app.ui.theme.GreenInk

/** Screen 3 — Number Details (Overview / More Info / Activity). */
@Composable
fun DetailsScreen(vm: AppViewModel) {
    val record = vm.state.record ?: return
    var tab by remember { mutableStateOf(0) }

    Column(Modifier.fillMaxSize()) {
        TopBar(
            title = "Number Details",
            onBack = { vm.back() },
            trailing = {
                Icon(Icons.Filled.Shield, contentDescription = null, tint = Green, modifier = Modifier.size(20.dp))
            }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(4.dp))
            ProfileCard(record = record, showStatus = true)
            Spacer(Modifier.height(14.dp))

            TabRow3(
                options = listOf("Overview", "More Info", "Activity"),
                selected = tab,
                onSelect = { index ->
                    if (index == 1) vm.go(Screen.MoreInfo) else tab = index
                }
            )

            Spacer(Modifier.height(4.dp))
            when (tab) {
                0 -> {
                    DetailRow("Full Name", record.name)
                    DetailRow("Mobile Number", "+91 " + record.number)
                    DetailRow("Email ID", record.email)
                    DetailRow("Location", record.location)
                    DetailRow("Operator", record.operator)
                    DetailRow("Circle", record.circle)
                    DetailRow("WhatsApp", record.whatsapp, Green)
                    DetailRow("Risk Score", record.riskScore, Green)
                    DetailRow("Caller ID", record.callerId)
                    DetailRow("Reports", record.reports.toString() + " Reports")
                    DetailRow("Last Seen", record.lastSeen)
                }
                2 -> {
                    Spacer(Modifier.height(8.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        DemoData.activity.forEach { item ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(Modifier.padding(12.dp)) {
                                    Text(item.title, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold)
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        item.detail,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        val saved = vm.isSaved(record.number)
        Button(
            onClick = { vm.toggleSave() },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (saved) MaterialTheme.colorScheme.surfaceVariant else Green,
                contentColor = if (saved) MaterialTheme.colorScheme.onSurface else GreenInk
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .height(46.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (saved) {
                    Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.size(6.dp))
                }
                Text(
                    text = if (saved) "Saved in Contacts" else "Save to Contacts",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
