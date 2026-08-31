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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travel_planner.ui.theme.*

private data class Activity(val name: String, val duration: String, val price: Int)

private val sampleActivities = listOf(
    Activity("Arashiyama Bamboo Grove Walking Tour", "2 hours", 45),
    Activity("Traditional Tea Ceremony Experience", "1.5 hours", 60),
    Activity("Fushimi Inari Sunrise Hike", "3 hours", 0)
)

@Composable
fun ActivitiesScreen(onSelectActivity: (String) -> Unit = {}) {
    Column(modifier = Modifier.fillMaxSize().background(Background)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Things to Do in Kyoto", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Navy)
            Text("Hand-picked experiences for your trip", style = MaterialTheme.typography.bodySmall, color = Slate)
        }
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleActivities) { activity ->
                ActivityCard(activity, onClick = { onSelectActivity(activity.name) })
            }
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