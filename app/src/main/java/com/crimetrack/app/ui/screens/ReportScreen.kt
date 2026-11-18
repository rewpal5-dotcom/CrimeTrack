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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.crimetrack.app.data.Risk
import com.crimetrack.app.ui.AppViewModel
import com.crimetrack.app.ui.components.SectionTitle
import com.crimetrack.app.ui.components.TopBar
import com.crimetrack.app.ui.theme.Green
import com.crimetrack.app.ui.theme.GreenInk

@Composable
fun ReportScreen(vm: AppViewModel) {
    val info = vm.state.info ?: return
    var category by remember { mutableStateOf("Spam") }
    var comment by remember { mutableStateOf("") }
    var reporter by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        TopBar(title = "Report Number", onBack = { vm.back() })
        Spacer(Modifier.height(6.dp))

        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(14.dp)) {
                Text(info.international, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(3.dp))
                Text(
                    text = info.reportCount.toString() + " reports · " + info.riskLabel,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(18.dp))
        SectionTitle("Category")
        Spacer(Modifier.height(10.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Risk.categories.chunked(3).forEach { rowItems ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    rowItems.forEach { item ->
                        val active = category == item
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (active) Green else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, if (active) Green else MaterialTheme.colorScheme.outline),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { category = item }
                        ) {
                            Box(Modifier.padding(vertical = 9.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    text = item,
                                    fontSize = 10.5.sp,
                                    fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
                                    color = if (active) GreenInk else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(18.dp))
        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it.take(240) },
            label = { Text("Kya hua? (optional)") },
            placeholder = { Text("jaise: OTP maang raha tha, fraud call") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
        )

        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = reporter,
            onValueChange = { reporter = it.take(40) },
            label = { Text("Aapka naam (optional)") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))
        Button(
            onClick = { vm.addReport(category, comment, reporter) },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Green, contentColor = GreenInk),
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
        ) {
            Text("Submit Report", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Report aapke device par save hoti hai aur risk score turant update hota hai. Settings me API URL daalne par ye reports server par bhi ja sakti hain.",
            fontSize = 10.5.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(24.dp))
    }
}
