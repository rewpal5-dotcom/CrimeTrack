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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crimetrack.app.data.NumberInfo
import com.crimetrack.app.data.formatDate
import com.crimetrack.app.ui.AppViewModel
import com.crimetrack.app.ui.Screen
import com.crimetrack.app.ui.components.Avatar
import com.crimetrack.app.ui.components.InfoTile
import com.crimetrack.app.ui.components.SectionTitle
import com.crimetrack.app.ui.components.TopBar
import com.crimetrack.app.ui.theme.Amber
import com.crimetrack.app.ui.theme.Blue
import com.crimetrack.app.ui.theme.Green
import com.crimetrack.app.ui.theme.GreenInk
import com.crimetrack.app.ui.theme.Red
import com.crimetrack.app.ui.theme.TileAmber
import com.crimetrack.app.ui.theme.TileBlue
import com.crimetrack.app.ui.theme.TileGreen
import com.crimetrack.app.ui.theme.TileRed
import com.crimetrack.app.ui.theme.TileTeal
import com.crimetrack.app.ui.theme.TileViolet
import com.crimetrack.app.ui.theme.Teal
import com.crimetrack.app.ui.theme.Violet

@Composable
fun ResultScreen(vm: AppViewModel) {
    val info = vm.state.info ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        TopBar(
            title = "CrimeTrack",
            trailing = { Icon(Icons.Filled.Shield, null, tint = Green, modifier = Modifier.size(20.dp)) }
        )

        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Row(Modifier.padding(6.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = info.international,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                )
                Surface(
                    shape = RoundedCornerShape(11.dp),
                    color = Green,
                    modifier = Modifier
                        .size(42.dp)
                        .clickable { vm.go(Screen.Home) }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.Search, null, tint = GreenInk, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        SectionTitle("Quick Overview")
        Spacer(Modifier.height(10.dp))

        ProfileCard(info = info, showStatus = false)

        Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                InfoTile("Status", if (info.valid) "Valid number" else "Invalid", Icons.Filled.CheckCircle, Green, TileGreen, Modifier.weight(1f)) {
                    vm.go(Screen.Details)
                }
                InfoTile("Type", info.type, Icons.Filled.Call, Violet, TileViolet, Modifier.weight(1f)) {
                    vm.go(Screen.Details)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                InfoTile("Region", info.region, Icons.Filled.Place, Teal, TileTeal, Modifier.weight(1f)) {
                    vm.go(Screen.Details)
                }
                InfoTile("Reports", info.reportCount.toString() + " reports", Icons.Filled.Email, Amber, TileAmber, Modifier.weight(1f)) {
                    vm.go(Screen.Details)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                InfoTile("Risk Score", info.riskLabel, Icons.Filled.Warning, Red, TileRed, Modifier.weight(1f)) {
                    vm.go(Screen.Details)
                }
                InfoTile("Top Category", info.topCategory, Icons.Filled.Shield, Blue, TileBlue, Modifier.weight(1f)) {
                    vm.go(Screen.Details)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                InfoTile("Last Report", formatDate(info.lastReport), Icons.Filled.Email, Green, TileGreen, Modifier.weight(1f)) {
                    vm.go(Screen.Details)
                }
                InfoTile("Format", info.national, Icons.Filled.Place, Amber, TileAmber, Modifier.weight(1f)) {
                    vm.go(Screen.Details)
                }
            }
        }

        Spacer(Modifier.height(18.dp))
        Button(
            onClick = { vm.go(Screen.Details) },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Green, contentColor = GreenInk),
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
        ) {
            Text("View Full Details", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
internal fun ProfileCard(info: NumberInfo, showStatus: Boolean) {
    val title = if (info.knownName.isNotBlank()) info.knownName else "Number " + info.national
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Avatar(name = title, size = 52.dp)
            Spacer(Modifier.width(13.dp))
            Column {
                Text(title, fontSize = 14.5.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(3.dp))
                Text(
                    text = info.international,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = info.reportCount.toString() + " community reports · " + info.region,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (showStatus) {
                    Spacer(Modifier.height(7.dp))
                    Surface(shape = RoundedCornerShape(50), color = Green.copy(alpha = 0.16f)) {
                        Text(
                            text = info.riskLabel,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Green,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }
}
