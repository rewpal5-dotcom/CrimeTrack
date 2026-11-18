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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
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
import com.crimetrack.app.data.DemoData
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

/** Screen 1 — Home / number search. */
@Composable
fun HomeScreen(vm: AppViewModel) {
    val state = vm.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(22.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = "Crime",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-1).sp
            )
            Text(
                text = "Track",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-1).sp,
                color = Green
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Find information about any mobile number",
            fontSize = 12.5.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        HeroArt()

        SearchBox(vm)

        Spacer(Modifier.height(12.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(12.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = "Your search is 100% secure",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (state.history.isNotEmpty()) {
            Spacer(Modifier.height(26.dp))
            Text(
                text = "Recent searches",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(10.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                state.history.take(3).forEach { item ->
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
                                text = "+91 " + item.number,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = item.name,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        if (state.demoMode) {
            Spacer(Modifier.height(22.dp))
            Text(
                text = "Demo database: " + DemoData.suggestions.take(2).joinToString(", "),
                fontSize = 10.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(24.dp))
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
        Row(
            modifier = Modifier.padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "+91",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Box(
                Modifier
                    .width(1.dp)
                    .height(24.dp)
                    .background(MaterialTheme.colorScheme.outline)
            )
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(onSearch = { vm.search() }),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                decorationBox = { innerTextField ->
                    Box {
                        if (state.query.isEmpty()) {
                            Text(
                                text = "Enter mobile number",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
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
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = GreenInk,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = GreenInk,
                            modifier = Modifier.size(20.dp)
                        )
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
            .height(250.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .size(220.dp)
                .background(
                    Brush.radialGradient(listOf(Green.copy(alpha = 0.18f), Color.Transparent)),
                    CircleShape
                )
        )
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.size(width = 128.dp, height = 200.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = null,
                    tint = Green,
                    modifier = Modifier.size(56.dp)
                )
            }
        }
        Chip(Icons.Filled.Person, TileGreen, GreenLight, Modifier.align(Alignment.TopStart).offset(x = 4.dp, y = 30.dp))
        Chip(Icons.Filled.Email, TileViolet, Violet, Modifier.align(Alignment.TopEnd).offset(x = (-4).dp, y = 44.dp))
        Chip(Icons.Filled.Phone, TileBlue, Blue, Modifier.align(Alignment.CenterStart).offset(x = 2.dp, y = 34.dp))
        Chip(Icons.Filled.Email, TileAmber, Amber, Modifier.align(Alignment.CenterEnd).offset(x = (-6).dp, y = 10.dp))
        Chip(Icons.Filled.Place, TileGreen, GreenLight, Modifier.align(Alignment.BottomEnd).offset(x = (-34).dp, y = (-16).dp))
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
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(15.dp))
        }
    }
}
