package com.example.travel_planner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.travel_planner.data.TravelRepository
import com.example.travel_planner.model.Activity
import com.example.travel_planner.ui.theme.*
import com.example.travel_planner.ui.viewmodel.ResourceViewModel
import com.example.travel_planner.ui.viewmodel.UiState

@Composable
fun ActivitiesScreen(
    onSelectActivity: (String) -> Unit = {},
    viewModel: ResourceViewModel<List<Activity>> = viewModel(
        factory = viewModelFactory {
            initializer { ResourceViewModel { TravelRepository.loadActivitiesUi() } }
        }
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(Background)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Things to Do in Kyoto", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Navy)
            Text("Hand-picked experiences for your trip", style = MaterialTheme.typography.bodySmall, color = Slate)
        }

        when (val state = uiState) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text("Couldn't load activities: ${state.message}", style = MaterialTheme.typography.bodyMedium, color = Slate)
                }
            }
            is UiState.Success -> {
                if (state.data.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                        Text("No activities found yet.", style = MaterialTheme.typography.bodyMedium, color = Slate)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.data) { activity ->
                            ActivityCard(activity, onClick = { onSelectActivity(activity.id) })
                        }
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
private fun ActivityCard(activity: Activity, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(White)
            .clickable { onClick() }
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(8.dp)).background(Border))
        Text(activity.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Navy)
        Text(
            if (activity.price == 0) "${activity.duration} • Free" else "${activity.duration} • $${activity.price}",
            style = MaterialTheme.typography.bodySmall,
            color = Slate
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ActivitiesScreenPreview() {
    ActivitiesScreen()
}