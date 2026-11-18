package com.crimetrack.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crimetrack.app.data.Report
import com.crimetrack.app.data.formatDate
import com.crimetrack.app.ui.Actions
import com.crimetrack.app.ui.AppViewModel
import com.crimetrack.app.ui.Screen
import com.crimetrack.app.ui.components.DetailRow
import com.crimetrack.app.ui.components.TabRow3
import com.crimetrack.app.ui.components.TopBar
import com.crimetrack.app.ui.theme.Green
import com.crimetrack.app.ui.theme.GreenInk

@Composable
fun DetailsScreen(vm: AppViewModel) {
    val info = vm.state.info ?: return
    val context = LocalContext.current
    var tab by remember { mutableStateOf(0) }

    Column(Modifier.fillMaxSize()) {
        TopBar(
            title = "Number Details",
            onBack = { vm.back() },
            trailing = { Icon(Icons.Filled.Shield, null, tint = Green, modifier = Modifier.size(20.dp)) }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(4.dp))
            ProfileCard(info = info, showStatus = true)
            Spacer(Modifier.height(12.dp))

            ActionRow(
                onCall = { Actions.dial(context, info.e164) },
                onWhatsApp = { Actions.whatsapp(context, info.number) },
                onCopy = { Actions.copy(context, "Number", info.e164) },
                onContact = { Actions.addContact(context, info.knownName, info.e164) },
                onShare = {
                    Actions.share(
                        context,
                        "CrimeTrack check: " + info.international + " — " + info.riskLabel +
                            " (" + info.reportCount.toString() + " reports)"
                    )
                }
            )

            Spacer(Modifier.height(14.dp))
            TabRow3(
                options = listOf("Overview", "More Info", "Activity"),
                selected = tab,
                onSelect = { index -> if (index == 1) vm.go(Screen.MoreInfo) else tab = index }
            )
            Spacer(Modifier.height(4.dp))

            when (tab) {
                0 -> {
                    DetailRow("Mobile Number", info.national)
                    DetailRow("Valid Number", if (info.valid) "Yes" else "No", if (info.valid) Green else MaterialTheme.colorScheme.error)
                    DetailRow("Number Type", info.type)
                    DetailRow("Region", info.region)
                    DetailRow("International", info.international)
                    DetailRow("Community Reports", info.reportCount.toString())
                    DetailRow("Risk Score", info.riskScore.toString() + " / 100", Green)
                    DetailRow("Risk Level", info.riskLabel)
                    DetailRow("Top Category", info.topCategory)
                    DetailRow("Last Report", formatDate(info.lastReport))
                }
                2 -> {
                    Spacer(Modifier.height(8.dp))
                    if (info.reports.isEmpty()) {
                        Box(Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = "Is number par abhi koi report nahi hai.\nNeeche 'Report' dabakar pehli report aap add karein.",
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            info.reports.forEach { report ->
                                ReportCard(report = report, onDelete = { vm.deleteReport(report.id) })
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        val saved = vm.isSaved(info.number)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = { vm.toggleSave() },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
            ) {
                if (saved) {
                    Icon(Icons.Filled.Check, null, modifier = Modifier.size(15.dp))
                    Spacer(Modifier.width(5.dp))
                }
                Text(if (saved) "Saved" else "Save", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { vm.go(Screen.Report) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Green, contentColor = GreenInk),
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
            ) {
                Icon(Icons.Filled.Edit, null, modifier = Modifier.size(15.dp))
                Spacer(Modifier.width(5.dp))
                Text("Report", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ActionRow(
    onCall: () -> Unit,
    onWhatsApp: () -> Unit,
    onCopy: () -> Unit,
    onContact: () -> Unit,
    onShare: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier.padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionButton(Icons.Filled.Call, "Call", onCall)
            ActionButton(Icons.Filled.Send, "WhatsApp", onWhatsApp)
            ActionButton(Icons.Filled.Share, "Copy", onCopy)
            ActionButton(Icons.Filled.Person, "Contact", onContact)
            ActionButton(Icons.Filled.Edit, "Share", onShare)
        }
    }
}

@Composable
private fun ActionButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 6.dp, vertical = 4.dp)
    ) {
        Icon(icon, contentDescription = label, tint = Green, modifier = Modifier.size(19.dp))
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 9.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ReportCard(report: Report, onDelete: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(start = 12.dp, top = 10.dp, bottom = 10.dp, end = 4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = RoundedCornerShape(50), color = Green.copy(alpha = 0.16f)) {
                    Text(
                        report.category,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Green,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (report.reporter.isBlank()) "Anonymous" else report.reporter,
                    fontSize = 10.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Filled.Delete, "Delete", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                }
            }
            if (report.comment.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(report.comment, fontSize = 12.sp)
            }
            Spacer(Modifier.height(3.dp))
            Text(formatDate(report.time), fontSize = 9.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
