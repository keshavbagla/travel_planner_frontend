package com.example.travel_planner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.travel_planner.data.TravelRepository
import com.example.travel_planner.model.Hotel
import com.example.travel_planner.ui.theme.*
import com.example.travel_planner.ui.viewmodel.ResourceViewModel
import com.example.travel_planner.ui.viewmodel.UiState
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay

@Composable
fun HotelsScreen(
    onSelectHotel: (String) -> Unit = {},
    viewModel: ResourceViewModel<List<Hotel>> = viewModel(
        factory = viewModelFactory {
            initializer { ResourceViewModel { TravelRepository.loadHotelsUi() } }
        }
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    var isFirstLaunch by remember { mutableStateOf(true) }
    LaunchedEffect(searchQuery) {
        if (isFirstLaunch) {
            isFirstLaunch = false
            return@LaunchedEffect
        }
        delay(400)
        viewModel.refresh { TravelRepository.loadHotelsUi(searchQuery) }
    }

    Column(modifier = Modifier.fillMaxSize().background(Background)) {
        Column(
            modifier = Modifier.fillMaxWidth().background(Navy).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Find Hotels", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = White)
            EditableSearchField(label = "DESTINATION / HOTEL NAME", value = searchQuery, onValueChange = { searchQuery = it })
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StaticField(label = "CHECK-IN / OUT", value = "Oct 14 - 18", modifier = Modifier.weight(1f))
                StaticField(label = "GUESTS", value = "2 Adults, 1 Room", modifier = Modifier.weight(1f))
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
                    Text("Couldn't load hotels: ${state.message}", style = MaterialTheme.typography.bodyMedium, color = Slate)
                }
            }
            is UiState.Success -> {
                if (state.data.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                        Text(
                            if (searchQuery.isBlank()) "No hotels found yet." else "No hotels match \"$searchQuery\".",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.data) { hotel ->
                            HotelCard(hotel = hotel, onSelect = { onSelectHotel(hotel.id) })
                        }
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
private fun EditableSearchField(label: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = White.copy(alpha = 0.7f))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(White)
                .padding(12.dp)
        ) {
            if (value.isEmpty()) {
                Text("e.g. Kyoto, or a hotel name", style = MaterialTheme.typography.bodyMedium, color = Slate)
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(fontSize = 14.sp, color = Navy),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun StaticField(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = White.copy(alpha = 0.7f))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(White)
                .padding(12.dp)
        ) {
            Text(value, style = MaterialTheme.typography.bodyMedium, color =Navy)
        }
    }
}

@Composable
private fun HotelCard(hotel: Hotel, onSelect: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(140.dp).clip(RoundedCornerShape(12.dp)).background(Border))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(hotel.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Navy)
                Text(hotel.area, style = MaterialTheme.typography.bodySmall, color =Slate)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(androidx.compose.ui.graphics.Color(0xFFD1FAE5))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(hotel.rating, color = androidx.compose.ui.graphics.Color(0xFF28A745), style = MaterialTheme.typography.labelSmall)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            repeat(hotel.stars) {
                Icon(Icons.Filled.Star, contentDescription = null, tint = Teal, modifier = Modifier.size(12.dp))
            }
            Text("  ${hotel.amenities}", style = MaterialTheme.typography.bodySmall, color = Slate)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("${hotel.currency} ${hotel.price} / night", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Navy)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Teal)
                    .clickable { onSelect() }
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text("Select", color = White, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HotelsScreenPreview() {
    HotelsScreen()
}