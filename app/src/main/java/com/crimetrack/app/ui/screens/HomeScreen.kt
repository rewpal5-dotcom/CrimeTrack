package com.crimetrack.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crimetrack.app.data.PhoneUtil
import com.crimetrack.app.ui.AppViewModel
import com.crimetrack.app.ui.theme.Amber
import com.crimetrack.app.ui.theme.Blue
import com.crimetrack.app.ui.theme.Green
import com.crimetrack.app.ui.theme.GreenInk
import com.crimetrack.app.ui.theme.GreenLight
import com.crimetrack.app.ui.theme.TileAmber
import com.crimetrack.app.ui.theme.TileBlue
import com.crimetrack.app.ui.theme.TileGreen
import com.crimetrack.app.ui.theme.TileViolet
import com.crimetrack.app.ui.theme.Violet

@Composable
fun HomeScreen(vm: AppViewModel) {
    val state = vm.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(20.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text("Crime", fontSize = 34.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-1).sp)
            Text("Track", fontSize = 34.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-1).sp, color = Green)
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Number check karein — reports, risk score aur details",
            fontSize = 12.5.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        HeroArt()

        SearchBox(vm)
        Spacer(Modifier.height(8.dp))
        ValidationHint(state.query)

        Spacer(Modifier.height(10.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Lock, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(12.dp))
            Spacer(Modifier.width(6.dp))
            Text(
                text = "Aapke reports sirf is device par save hote hain",
                fontSize = 10.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (state.history.isNotEmpty()) {
            Spacer(Modifier.height(24.dp))
            Text("Recent searches", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(10.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                state.history.take(4).forEach { item ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { vm.openNumber(item.number) }
                    ) {
                        Row(
                            Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = PhoneUtil.national(item.number),
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                            if (item.saved) {
                                Text("Saved", fontSize = 10.sp, color = Green, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(28.dp))
    }
}

@Composable
private fun ValidationHint(query: String) {
    if (query.isEmpty()) return
    val valid = query.length == 10 && PhoneUtil.isValid(query)
    val color = if (valid) Green else Amber
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = if (valid) Icons.Filled.CheckCircle else Icons.Filled.Warning,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(13.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = when {
                valid -> "Valid mobile number — " + PhoneUtil.regionName(query) + " (" + PhoneUtil.typeLabel(query) + ")"
                query.length < 10 -> (10 - query.length).toString() + " digit baaki hain"
                else -> "Ye valid Indian mobile number nahi lagta"
            },
            fontSize = 11.sp,
            color = color
        )
    }
}

@Composable
private fun SearchBox(vm: AppViewModel) {
    val state = vm.state
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(Modifier.padding(6.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("+91", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 10.dp))
            Box(Modifier.width(1.dp).height(24.dp).background(MaterialTheme.colorScheme.outline))
            BasicTextField(
                value = state.query,
                onValueChange = vm::onQueryChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                ),
                cursorBrush = SolidColor(Green),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { vm.search() }),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                decorationBox = { innerTextField ->
                    Box {
                        if (state.query.isEmpty()) {
                            Text("Enter mobile number", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        innerTextField()
                    }
                }
            )
            Surface(
                shape = RoundedCornerShape(11.dp),
                color = Green,
                modifier = Modifier
                    .size(42.dp)
                    .clickable { vm.search() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (state.searching) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = GreenInk, strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.Filled.Search, "Search", tint = GreenInk, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroArt() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(236.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .size(210.dp)
                .background(Brush.radialGradient(listOf(Green.copy(alpha = 0.18f), Color.Transparent)), CircleShape)
        )
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.size(width = 124.dp, height = 192.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.Search, null, tint = Green, modifier = Modifier.size(54.dp))
            }
        }
        Chip(Icons.Filled.Person, TileGreen, GreenLight, Modifier.align(Alignment.TopStart).offset(x = 4.dp, y = 26.dp))
        Chip(Icons.Filled.Email, TileViolet, Violet, Modifier.align(Alignment.TopEnd).offset(x = (-4).dp, y = 40.dp))
        Chip(Icons.Filled.Phone, TileBlue, Blue, Modifier.align(Alignment.CenterStart).offset(x = 2.dp, y = 30.dp))
        Chip(Icons.Filled.Warning, TileAmber, Amber, Modifier.align(Alignment.CenterEnd).offset(x = (-6).dp, y = 6.dp))
        Chip(Icons.Filled.Place, TileGreen, GreenLight, Modifier.align(Alignment.BottomEnd).offset(x = (-32).dp, y = (-14).dp))
    }
}

@Composable
private fun Chip(icon: ImageVector, background: Color, tint: Color, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = background,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = modifier.size(32.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = tint, modifier = Modifier.size(15.dp))
        }
    }
}
