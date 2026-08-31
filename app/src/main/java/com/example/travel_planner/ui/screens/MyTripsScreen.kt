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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travel_planner.ui.theme.*

private val tabs = listOf("Upcoming", "Past", "Saved")

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyTripsScreen(onPlanNewTrip: () -> Unit = {}) {
    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize().background(Background)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(26.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("My Itineraries", fontWeight = FontWeight.Bold, fontSize = 26.sp, color = Navy)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(Coral)
                    .clickable { onPlanNewTrip() }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Filled.Add, contentDescription = null, tint = White, modifier = Modifier.size(20.dp))
                    Text("Plan Trip", color = White, style = MaterialTheme.typography.labelMedium)
                }
            }
        }

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 26.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tabs.forEachIndexed { index, tab ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(26.dp))
                        .background(if (selectedTab == index)Navy else White)
                        .border(1.dp, if (selectedTab == index)Navy else Border, RoundedCornerShape(26.dp))
                        .clickable { selectedTab = index }
                        .padding(horizontal = 26.dp, vertical = 8.dp)
                ) {
                    Text(tab, color = if (selectedTab == index) White else Slate, style = MaterialTheme.typography.labelMedium)
                }
            }
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(26.dp)) {
            if (selectedTab == 0) {
                TripCard()
            } else {
                EmptyTripsState()
            }
        }
    }
}

@Composable
private fun TripCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(White)
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(170.dp).clip(RoundedCornerShape(12.dp)).background(Border))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Dreamy Amalfi Getaway", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Navy)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(TealTint)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text("PLANNING", color = Teal, style = MaterialTheme.typography.labelSmall)
            }
        }
        Text("Oct 14 - Oct 21, 2026", style = MaterialTheme.typography.bodyMedium, color = Slate , fontSize = 14.sp)
        Text("2 Travelers • 8 Days in Positano, Amalfi & Capri", style = MaterialTheme.typography.bodySmall, color = Slate ,fontSize = 14.sp)

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Planning Progress", style = MaterialTheme.typography.labelMedium, color = Slate ,fontSize = 14.sp)
                Text("75%", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Teal)
            }
            Box(modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)).background(Border)) {
                Box(modifier = Modifier.fillMaxWidth(0.75f).height(14.dp).clip(RoundedCornerShape(4.dp)).background(Teal))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Navy)
                .padding(vertical = 18.dp),
        ) {
            Text("Resume Planning", color = White, style = MaterialTheme.typography.labelLarge, modifier = Modifier.fillMaxWidth().padding(horizontal = 92.dp),fontSize = 16.sp)
        }
    }
}

@Composable
private fun EmptyTripsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(White)
            .border(1.dp, Border, RoundedCornerShape(16.dp))
            .padding(vertical = 48.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(TealTint))
        Text("No Saved Journeys Yet", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Navy)
        Text(
            "Explore custom destinations and save your favorite travel blueprints to view them anytime.",
            style = MaterialTheme.typography.bodyMedium,
            color = Slate,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}