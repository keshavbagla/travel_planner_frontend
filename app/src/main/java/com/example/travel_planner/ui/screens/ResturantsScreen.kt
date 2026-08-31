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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travel_planner.ui.theme.*
import androidx.compose.ui.tooling.preview.Preview

private data class Restaurant(val name: String, val cuisine: String, val rating: Double, val priceTier: String)

private val sampleRestaurants = listOf(
    Restaurant("Kichi Coffee & Tea House", "Japanese • Cafe", 4.8, "$$" ),
    Restaurant("Gion Karyo", "Kaiseki • Fine Dining", 4.9, "$$$$"),
    Restaurant("Nishiki Warai", "Street Food • Local", 4.6, "$")
)

@Composable
fun RestaurantsScreen(onSelectRestaurant: (String) -> Unit = {}) {
    Column(modifier = Modifier.fillMaxSize().background(Background)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Restaurants in Kyoto", fontWeight = FontWeight.Bold, fontSize = 26.sp, color = Navy)
            Text("Curated by Voyago AI based on your taste", style = MaterialTheme.typography.bodySmall, color = Slate , fontSize = 14.sp)
        }
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleRestaurants) { restaurant ->
                RestaurantCard(restaurant, onClick = { onSelectRestaurant(restaurant.name) })
            }
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