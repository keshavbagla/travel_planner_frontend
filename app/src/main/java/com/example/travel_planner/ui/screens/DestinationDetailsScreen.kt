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
import coil.compose.AsyncImage
import com.example.travel_planner.data.TravelRepository
import com.example.travel_planner.model.Activity
import com.example.travel_planner.model.Destination
import com.example.travel_planner.ui.theme.*
import com.example.travel_planner.ui.viewmodel.ResourceViewModel
import com.example.travel_planner.ui.viewmodel.UiState

@Composable
fun DestinationDetailsScreen(
    destinationId: String,
    onBackClick: () -> Unit = {},
    onPlanTripClick: () -> Unit = {},
    onViewHotelsClick: () -> Unit = {},
    onViewRestaurantsClick: () -> Unit = {},
    onViewActivitiesClick: () -> Unit = {},
    detailsViewModel: ResourceViewModel<Destination> = viewModel(
        key = "destinationDetails-$destinationId",
        factory = viewModelFactory {
            initializer { ResourceViewModel { TravelRepository.loadDestinationUi(destinationId) } }
        }
    ),
    activitiesViewModel: ResourceViewModel<List<Activity>> = viewModel(
        key = "destinationActivities-$destinationId",
        factory = viewModelFactory {
            initializer { ResourceViewModel { TravelRepository.loadActivitiesUi(destinationId = destinationId) } }
        }
    )
) {
    val detailsState by detailsViewModel.uiState.collectAsState()
    val activitiesState by activitiesViewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        when (val state = detailsState) {
            is UiState.Idle -> {}
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text("Couldn't load this destination: ${state.message}", style = MaterialTheme.typography.bodyMedium, color = Slate)
                }
            }
            is UiState.Success -> {
                val destination = state.data
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
                            AsyncImage(
                                model = destination.imageUrl,
                                contentDescription = destination.name,
                                modifier = Modifier.fillMaxSize()
                            )
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
                                    Text((destination.country ?: "").uppercase(), style = MaterialTheme.typography.labelMedium, color = Teal)
                                    Text(destination.name, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Navy)
                                }
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(White)
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    if (destination.rating > 0.0) { // <-- changed: don't show "0.0★" for genuinely unrated destinations
                                        Icon(Icons.Filled.Star, contentDescription = null, tint = Teal, modifier = Modifier.size(12.dp))
                                        Text("${destination.rating}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Navy)
                                    } else {
                                        Text("New", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Navy)
                                    }
                                }
                            }
                            Text(
                                destination.description?.ifBlank { "No description available yet." } ?: "No description available yet.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Slate
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                destination.recommendedDurationText?.let {
                                    Text("🗓 $it", style = MaterialTheme.typography.bodySmall, color = Slate)
                                }
                                destination.primaryAirportIata?.let {
                                    Text("✈ $it", style = MaterialTheme.typography.bodySmall, color = Slate)
                                }
                            }
                            if (destination.famousFor.isNotEmpty()) {
                                Text(
                                    "Famous for: ${destination.famousFor.joinToString(", ")}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate
                                )
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            QuickLinkButton(label = "Hotels", onClick = onViewHotelsClick, modifier = Modifier.weight(1f))
                            QuickLinkButton(label = "Restaurants", onClick = onViewRestaurantsClick, modifier = Modifier.weight(1f))
                            QuickLinkButton(label = "Activities", onClick = onViewActivitiesClick, modifier = Modifier.weight(1f))
                        }
                    }

                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                "Things to Do",
                                style = MaterialTheme.typography.titleMedium,
                                color = Navy,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            when (val actState = activitiesState) {
                                is UiState.Loading -> {
                                    Box(modifier = Modifier.padding(16.dp)) {
                                        Text("Loading activities...", style = MaterialTheme.typography.bodySmall, color = Slate)
                                    }
                                }
                                is UiState.Error -> {
                                    Box(modifier = Modifier.padding(16.dp)) {
                                        Text("Couldn't load activities.", style = MaterialTheme.typography.bodySmall, color = Slate)
                                    }
                                }
                                is UiState.Success -> {
                                    if (actState.data.isEmpty()) {
                                        Box(modifier = Modifier.padding(16.dp)) {
                                            Text("No activities listed for this destination yet.", style = MaterialTheme.typography.bodySmall, color = Slate)
                                        }
                                    } else {
                                        LazyRow(
                                            contentPadding = PaddingValues(horizontal = 16.dp),
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            items(actState.data) { activity ->
                                                ActivityPreviewCard(activity)
                                            }
                                        }
                                    }
                                }

                                else -> {}
                            }
                        }
                    }

                }

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
                        Text("Ready in 10s", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Navy)
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
    }
}

@Composable
private fun QuickLinkButton(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(White)
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = Navy, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun CircleIconButton(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(White.copy(alpha = 0.2f))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = White, modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun ActivityPreviewCard(activity: Activity) {
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
            Text(activity.name, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Navy, maxLines = 1)
            Text(activity.duration, style = MaterialTheme.typography.bodySmall, color = Slate)
        }
    }
}