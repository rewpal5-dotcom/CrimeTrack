package com.crimetrack.app.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crimetrack.app.ui.AppViewModel
import com.crimetrack.app.ui.components.SectionTitle
import com.crimetrack.app.ui.components.TopBar
import com.crimetrack.app.ui.theme.Amber
import com.crimetrack.app.ui.theme.Green
import com.crimetrack.app.ui.theme.GreenInk

private val features = listOf(
    "Unlimited Searches",
    "Full Email Access",
    "Alternate Numbers",
    "Social Media Profiles",
    "Address & Family Info",
    "No Ads",
    "Priority Support"
)

/** Screen 6 — Premium. */
@Composable
fun PremiumScreen(vm: AppViewModel) {
    val premium = vm.state.premium
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        TopBar(
            title = "Premium",
            trailing = {
                Icon(Icons.Filled.WorkspacePremium, contentDescription = null, tint = Green, modifier = Modifier.size(20.dp))
            }
        )
        Spacer(Modifier.height(4.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Go Premium", fontSize = 19.sp, fontWeight = FontWeight.ExtraBold)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = if (premium) "Premium active hai — sab features unlocked." else "Unlock all premium features and get unlimited access",
                            fontSize = 10.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        Icons.Filled.WorkspacePremium,
                        contentDescription = null,
                        tint = Amber,
                        modifier = Modifier.size(56.dp)
                    )
                }
                Spacer(Modifier.height(14.dp))
                Button(
                    onClick = { if (!premium) showDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (premium) MaterialTheme.colorScheme.surfaceVariant else Green,
                        contentColor = if (premium) MaterialTheme.colorScheme.onSurface else GreenInk
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Text(
                        text = if (premium) "Premium Active" else "Upgrade Now",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(18.dp))
        SectionTitle("Premium Features")
        Spacer(Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(11.dp)) {
            features.forEach { feature ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = RoundedCornerShape(5.dp), color = Green.copy(alpha = 0.16f), modifier = Modifier.size(17.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.Check, contentDescription = null, tint = Green, modifier = Modifier.size(11.dp))
                        }
                    }
                    Spacer(Modifier.width(9.dp))
                    Text(feature, fontSize = 11.5.sp)
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Premium (demo)") },
            text = { Text("Ye ek demo upgrade hai — abhi koi payment nahi hota. Aap apna billing / Google Play subscription baad me jod sakte hain.") },
            confirmButton = {
                TextButton(onClick = {
                    vm.upgrade()
                    showDialog = false
                }) { Text("Unlock karein") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Baad me") }
            }
        )
    }
}
