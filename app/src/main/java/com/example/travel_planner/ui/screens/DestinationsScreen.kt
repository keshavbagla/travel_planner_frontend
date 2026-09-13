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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.delay


@Composable
fun DestinationsScreen(
    onDestinationClick: (String) -> Unit = {},
    initialQuery: String = "",
    initialDestinationType: String = "", // <-- added
    viewModel: ResourceViewModel<List<Destination>> = viewModel(
        factory = viewModelFactory {
            initializer { ResourceViewModel { TravelRepository.loadDestinationsUi(initialQuery, initialDestinationType) } }
        }
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf(initialQuery) }
    var selectedType by remember { mutableStateOf(initialDestinationType) }

    var isFirstLaunch by remember { mutableStateOf(true) }
    LaunchedEffect(searchQuery, selectedType) {
        if (isFirstLaunch) {
            isFirstLaunch = false
            return@LaunchedEffect
        }
        delay(400)
        viewModel.refresh { TravelRepository.loadDestinationsUi(searchQuery, selectedType) }
    }

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
                listOf("Region", "Budget Tier", "Season").forEach { filter ->
                    FilterPill(filter)
                }
                TripTypeFilterPill(selected = selectedType, onSelect = { selectedType = it })
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
                if (state.data.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                        Text(
                            when {
                                searchQuery.isNotBlank() && selectedType.isNotBlank() ->
                                    "No \"$selectedType\" destinations match \"$searchQuery\"."
                                searchQuery.isNotBlank() -> "No destinations match \"$searchQuery\"."
                                selectedType.isNotBlank() -> "No \"$selectedType\" destinations found."
                                else -> "No destinations found yet."
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f).fillMaxWidth().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.data) { destination ->
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
val DESTINATION_TYPES = listOf("Beach", "Mountains", "City", "Adventure", "Cultural")

@Composable
private fun TripTypeFilterPill(selected: String, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(if (selected.isNotBlank()) TealTint else White)
                .border(1.dp, if (selected.isNotBlank()) Teal else Border, RoundedCornerShape(16.dp))
                .clickable { expanded = true }
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(selected.ifBlank { "Trip Type" }, style = MaterialTheme.typography.bodyMedium, color = Navy)
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Navy, modifier = Modifier.size(14.dp))
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            if (selected.isNotBlank()) {
                DropdownMenuItem(text = { Text("All (clear filter)") }, onClick = { onSelect(""); expanded = false })
            }
            DESTINATION_TYPES.forEach { type ->
                DropdownMenuItem(text = { Text(type) }, onClick = { onSelect(type); expanded = false })
            }
        }
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
                    if (destination.rating > 0.0) { // <-- changed: don't show "0.0★" for genuinely unrated destinations
                        Icon(Icons.Filled.Star, contentDescription = null, tint = White, modifier = Modifier.size(12.dp))
                        Text("${destination.rating}", color = White, style = MaterialTheme.typography.labelMedium)
                    } else {
                        Text("New", color = White, style = MaterialTheme.typography.labelMedium)
                    }
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