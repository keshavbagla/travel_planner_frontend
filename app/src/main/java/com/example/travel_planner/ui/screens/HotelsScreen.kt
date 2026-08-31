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

private data class Hotel(val name: String, val area: String, val rating: String, val stars: Int, val price: Int, val amenities: String)

private val sampleHotels = listOf(
    Hotel("The Ritz-Carlton, Kyoto", "Kamigyo Ward, Kyoto", "9.4 Wonderful", 5, 450, "Free WiFi • Pool • Spa • Gym"),
    Hotel("Sowaka Ryokan & Hotel", "Gion District, Kyoto", "9.1 Wonderful", 5, 380, "Free WiFi • Onsen • Restaurant"),
    Hotel("Hotel Granvia Kyoto", "Downtown Kyoto", "8.7 Excellent", 4, 210, "Free WiFi • Gym • Restaurant")
)

@Composable
fun HotelsScreen(onSelectHotel: (String) -> Unit = {}) {
    Column(modifier = Modifier.fillMaxSize().background(Background)) {
        Column(
            modifier = Modifier.fillMaxWidth().background(Navy).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Find Hotels", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = White)
            SearchField(label = "DESTINATION", value = "Kyoto, Japan")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SearchField(label = "CHECK-IN / OUT", value = "Oct 14 - 18", modifier = Modifier.weight(1f))
                SearchField(label = "GUESTS", value = "2 Adults, 1 Room", modifier = Modifier.weight(1f))
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Coral),
                contentAlignment = Alignment.Center
            ) {
                Text("Search Hotels", color = White, style = MaterialTheme.typography.labelLarge)
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(sampleHotels) { hotel ->
                HotelCard(hotel = hotel, onSelect = { onSelectHotel(hotel.name) })
            }
        }
    }
}

@Composable
private fun SearchField(label: String, value: String, modifier: Modifier = Modifier) {
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
            Text("$${hotel.price} / night", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Navy)
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