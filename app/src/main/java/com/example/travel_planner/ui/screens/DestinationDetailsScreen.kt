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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travel_planner.data.SampleData
import com.example.travel_planner.model.Attraction
import com.example.travel_planner.model.ItineraryDay
import com.example.travel_planner.ui.theme.*

@Composable
fun DestinationDetailsScreen(
    destinationId: String,
    onBackClick: () -> Unit = {},
    onPlanTripClick: () -> Unit = {}
) {
    // TODO: use destinationId to fetch real details from your API instead of SampleData.
    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .background(Navy)
                ) {
                    // TODO: swap for AsyncImage hero gallery from your API/CDN.
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CircleIconButton(icon = Icons.Filled.ArrowBack, onClick = onBackClick)
                        CircleIconButton(icon = Icons.Filled.FavoriteBorder, onClick = { /* TODO: save favorite via API */ })
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("ASIA / JAPAN", style = MaterialTheme.typography.labelMedium, color = Teal)
                            Text("Kyoto, Japan", fontWeight = FontWeight.Bold, fontSize = 24.sp, color =Navy)
                        }
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(White)
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(Icons.Filled.Star, contentDescription = null, tint = Teal, modifier = Modifier.size(12.dp))
                            Text("4.9", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Navy)
                        }
                    }
                    Text(
                        "Kyoto, once the capital of Japan, is famous for its classical temples, breathtaking gardens, palaces, and preserved wooden architecture.",
                        style = MaterialTheme.typography.bodyMedium,
                        color =Slate
                    )
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Top Attractions",
                        style = MaterialTheme.typography.titleMedium,
                        color = Navy,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(SampleData.kyotoAttractions) { attraction ->
                            AttractionCard(attraction)
                        }
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Suggested AI Itinerary", style = MaterialTheme.typography.titleMedium, color = Navy)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(White)
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SampleData.kyotoItinerary.forEach { day ->
                            ItineraryRow(day)
                        }
                    }
                }
            }
        }

        // Sticky bottom CTA
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(White)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Instant Travel AI", style = MaterialTheme.typography.bodySmall, color = Slate)
                Text("Ready in 10s", fontWeight = FontWeight.Bold, fontSize = 14.sp, color =Navy)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Coral)
                    .clickable { onPlanTripClick() }
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text("Plan Trip Here", color = White, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun CircleIconButton(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background( White.copy(alpha = 0.2f))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = White, modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun AttractionCard(attraction: Attraction) {
    Row(
        modifier = Modifier
            .width(240.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(White)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Border)
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(attraction.name, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Navy, maxLines = 1)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Filled.Star, contentDescription = null, tint = Teal, modifier = Modifier.size(10.dp))
                Text("${attraction.rating}", style = MaterialTheme.typography.bodySmall, color = Slate)
            }
        }
    }
}

@Composable
private fun ItineraryRow(day: ItineraryDay) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Teal)
        )
        Text(
            buildAnnotatedItineraryLine(day),
            style = MaterialTheme.typography.bodyMedium,
            color = Navy
        )
    }
}

@Composable
private fun buildAnnotatedItineraryLine(day: ItineraryDay) =
    androidx.compose.ui.text.buildAnnotatedString {
        withStyle(androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.SemiBold)) {
            append("${day.label}: ")
        }
        append(day.summary)
    }