package com.crimetrack.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.crimetrack.app.data.MoreInfoItem
import com.crimetrack.app.ui.AppViewModel
import com.crimetrack.app.ui.components.ListRow
import com.crimetrack.app.ui.components.TopBar
import com.crimetrack.app.ui.theme.Amber
import com.crimetrack.app.ui.theme.Blue
import com.crimetrack.app.ui.theme.Green
import com.crimetrack.app.ui.theme.Teal
import com.crimetrack.app.ui.theme.TileAmber
import com.crimetrack.app.ui.theme.TileBlue
import com.crimetrack.app.ui.theme.TileGreen
import com.crimetrack.app.ui.theme.TileTeal
import com.crimetrack.app.ui.theme.TileViolet
import com.crimetrack.app.ui.theme.Violet

@Composable
fun MoreInfoScreen(vm: AppViewModel) {
    val info = vm.state.info ?: return
    var selected by remember { mutableStateOf<MoreInfoItem?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        TopBar(title = "More Info", onBack = { vm.back() })
        Spacer(Modifier.height(4.dp))

        Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
            info.moreInfo.forEachIndexed { index, item ->
                val style = styleFor(index)
                ListRow(
                    icon = iconFor(item.icon),
                    tint = style.first,
                    background = style.second,
                    title = item.title,
                    subtitle = item.subtitle,
                    onClick = { selected = item }
                )
            }
        }
        Spacer(Modifier.height(20.dp))
    }

    val current = selected
    if (current != null) {
        AlertDialog(
            onDismissRequest = { selected = null },
            title = { Text(current.title) },
            text = { Text(current.subtitle) },
            confirmButton = { TextButton(onClick = { selected = null }) { Text("Band karein") } }
        )
    }
}

private fun iconFor(key: String): ImageVector = when (key) {
    "hash" -> Icons.Filled.List
    "share" -> Icons.Filled.Share
    "place" -> Icons.Filled.Place
    "groups" -> Icons.Filled.Groups
    "business" -> Icons.Filled.Business
    "school" -> Icons.Filled.School
    "car" -> Icons.Filled.DirectionsCar
    "vote" -> Icons.Filled.HowToVote
    else -> Icons.Filled.Fingerprint
}

private fun styleFor(index: Int): Pair<Color, Color> = when (index % 5) {
    0 -> Violet to TileViolet
    1 -> Blue to TileBlue
    2 -> Teal to TileTeal
    3 -> Amber to TileAmber
    else -> Green to TileGreen
}
