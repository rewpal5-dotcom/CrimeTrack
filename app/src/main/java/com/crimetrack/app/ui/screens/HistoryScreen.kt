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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crimetrack.app.data.HistoryItem
import com.crimetrack.app.ui.AppViewModel
import com.crimetrack.app.ui.components.Avatar
import com.crimetrack.app.ui.components.Segmented
import com.crimetrack.app.ui.components.TopBar
import com.crimetrack.app.ui.theme.Green
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Screen 4 — History (All / Viewed / Saved). */
@Composable
fun HistoryScreen(vm: AppViewModel) {
    val history = vm.state.history
    var filter by remember { mutableStateOf(0) }
    var confirmClear by remember { mutableStateOf(false) }

    val shown: List<HistoryItem> = when (filter) {
        1 -> history
        2 -> history.filter { it.saved }
        else -> history
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        TopBar(
            title = "History",
            trailing = {
                TextButton(onClick = { confirmClear = true }, enabled = history.isNotEmpty()) {
                    Text("Clear", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Green)
                }
            }
        )

        Spacer(Modifier.height(4.dp))
        Segmented(listOf("All", "Viewed", "Saved"), filter) { filter = it }
        Spacer(Modifier.height(14.dp))

        if (shown.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Abhi koi search nahi hai.\nHome se number search karein.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                shown.forEach { item ->
                    HistoryRow(
                        item = item,
                        onOpen = { vm.openNumber(item.number) },
                        onDelete = { vm.removeHistory(item.number) }
                    )
                }
            }
        }
        Spacer(Modifier.height(20.dp))
    }

    if (confirmClear) {
        AlertDialog(
            onDismissRequest = { confirmClear = false },
            title = { Text("History clear karein?") },
            text = { Text("Poori search history delete ho jayegi. Saved contacts bhi hat jayenge.") },
            confirmButton = {
                TextButton(onClick = {
                    vm.clearHistory()
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
private fun HistoryRow(item: HistoryItem, onOpen: () -> Unit, onDelete: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(13.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpen() }
    ) {
        Row(
            modifier = Modifier.padding(start = 11.dp, end = 4.dp, top = 9.dp, bottom = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(name = item.name, size = 34.dp)
            Spacer(Modifier.width(11.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = "+91 " + item.number,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.name + (if (item.saved) "  ·  Saved" else ""),
                    fontSize = 10.5.sp,
                    color = if (item.saved) Green else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = formatTime(item.time),
                fontSize = 9.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.End
            )
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}

private fun formatTime(millis: Long): String {
    return try {
        SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(millis))
    } catch (e: Exception) {
        "—"
    }
}
