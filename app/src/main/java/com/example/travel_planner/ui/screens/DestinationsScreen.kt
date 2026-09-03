package com.example.travel_planner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.compose.AsyncImage
import com.example.travel_planner.data.TravelRepository
import com.example.travel_planner.model.Destination
import com.example.travel_planner.ui.theme.*
import com.example.travel_planner.ui.viewmodel.ResourceViewModel
import com.example.travel_planner.ui.viewmodel.UiState


@Composable
fun DestinationsScreen(
    onDestinationClick: (String) -> Unit = {},
    initialQuery: String = "",
    viewModel: ResourceViewModel<List<Destination>> = viewModel(
        factory = viewModelFactory {
            initializer { ResourceViewModel { TravelRepository.loadDestinationsUi() } }
        }
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf(initialQuery) }

    Column(modifier = Modifier.fillMaxSize().background(Background)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .border(0.5.dp, Border)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(Background)
                    .border(1.dp, Border, RoundedCornerShape(22.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(Icons.Filled.Search, contentDescription = null, tint = Slate, modifier = Modifier.size(16.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            "Search destination...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate
                        )
                    }
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 14.sp, color = Navy),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Icon(Icons.Filled.Tune, contentDescription = "Filters", tint = Slate, modifier = Modifier.size(16.dp))
            }
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Region", "Budget Tier", "Season", "Trip Type").forEach { filter ->
                    FilterPill(filter)
                }
            }
        }

        when (val state = uiState) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text(
                        "Couldn't load destinations: ${state.message}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate
                    )
                }
            }
            is UiState.Success -> {
                val filtered = if (searchQuery.isBlank()) {
                    state.data
                } else {
                    state.data.filter {
                        it.name.contains(searchQuery, ignoreCase = true) ||
                                it.country.contains(searchQuery, ignoreCase = true) ||
                                it.description.contains(searchQuery, ignoreCase = true)
                    }
                }
                if (filtered.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                        Text(
                            if (searchQuery.isBlank()) "No destinations found yet." else "No destinations match \"$searchQuery\".",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f).fillMaxWidth().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filtered) { destination ->
                            DestinationStackCard(destination = destination, onClick = { onDestinationClick(destination.id) })
                        }
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
private fun FilterPill(label: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(White)
            .border(1.dp, Border, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = Navy)
        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint =Navy, modifier = Modifier.size(14.dp))
    }
}

@Composable
private fun DestinationStackCard(destination: Destination, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Navy)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = destination.imageUrl,
            contentDescription = destination.name,
            modifier = Modifier.fillMaxSize()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(destination.name, color = White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(White.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(destination.priceTier, color = White, style = MaterialTheme.typography.labelSmall)
                    }
                }
                Text(destination.country, color = White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodyMedium)
            }
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = White, modifier = Modifier.size(12.dp))
                    Text("${destination.rating}", color = White, style = MaterialTheme.typography.labelMedium)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Teal)
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text("Explore", color = White, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DestinationsScreenPreview() {
    DestinationsScreen()
}