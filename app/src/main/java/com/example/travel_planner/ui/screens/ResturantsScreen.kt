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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.travel_planner.data.TravelRepository
import com.example.travel_planner.model.Restaurant
import com.example.travel_planner.ui.theme.*
import com.example.travel_planner.ui.viewmodel.ResourceViewModel
import com.example.travel_planner.ui.viewmodel.UiState
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RestaurantsScreen(
    onSelectRestaurant: (String) -> Unit = {},
    viewModel: ResourceViewModel<List<Restaurant>> = viewModel(
        factory = viewModelFactory {
            initializer {
                ResourceViewModel<List<Restaurant>> { TravelRepository.loadRestaurantsUi() }
            }
        }
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(Background)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Restaurants in Kyoto", fontWeight = FontWeight.Bold, fontSize = 26.sp, color = Navy)
            Text("Curated by Voyago AI based on your taste", style = MaterialTheme.typography.bodySmall, color = Slate , fontSize = 14.sp)
        }

        when (val state = uiState) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text("Couldn't load restaurants: ${state.message}", style = MaterialTheme.typography.bodyMedium, color = Slate)
                }
            }
            is UiState.Success -> {
                if (state.data.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                        Text("No restaurants found yet.", style = MaterialTheme.typography.bodyMedium, color = Slate)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.data) { restaurant ->
                            RestaurantCard(restaurant, onClick = { onSelectRestaurant(restaurant.id) })
                        }
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
private fun RestaurantCard(restaurant: Restaurant, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(White)
            .clickable { onClick() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier.size(120.dp).clip(RoundedCornerShape(8.dp)).background(Border))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(restaurant.name, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Navy )
            Text(restaurant.cuisine, style = MaterialTheme.typography.bodySmall, color = Slate, fontSize=18.sp)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Filled.Star, contentDescription = null, tint = Teal, modifier = Modifier.size(18.dp))
                Text("${restaurant.rating}", style = MaterialTheme.typography.labelSmall, color = Navy , fontSize = 12.sp)
                Text("  ${restaurant.priceTier}", style = MaterialTheme.typography.labelSmall, color =Slate , fontSize = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RestaurantsScreenPreview() {
    RestaurantsScreen()
}