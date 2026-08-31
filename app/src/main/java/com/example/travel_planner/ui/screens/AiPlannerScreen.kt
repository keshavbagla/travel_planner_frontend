package com.voyago.app.ui.screens.aiplanner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.travel_planner.data.SampleData
import com.example.travel_planner.model.ChatMessage
import com.example.travel_planner.navigation.Destination
import com.example.travel_planner.ui.theme.*


@Composable
fun AiPlannerScreen(
    onBackClick: () -> Unit = {},
    onSendMessage: (String) -> Unit = {}
) {
    var input by remember { mutableStateOf("") }
    val messages = remember { SampleData.chatMessages }

    Column(modifier = Modifier.fillMaxSize().background(Background)) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .border(0.5.dp, Border)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Navy,
                    modifier = Modifier.clickable { onBackClick() }
                )
                Text("AI Travel Copilot", style = MaterialTheme.typography.titleLarge, color = Navy)
            }
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(TealTint)
                    .border(1.dp,Border, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Tune, contentDescription = "Filters", tint = Teal, modifier = Modifier.size(16.dp))
            }
        }

        // Active preferences
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .border(0.5.dp, Border)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("ACTIVE TRIP PREFERENCES", style = MaterialTheme.typography.labelMedium, color = Slate)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PreferencePill(label = "Relaxed Pace", selected = true)
                PreferencePill(label = "Luxury Budget")
                PreferencePill(label = "Oct 14-17")
            }
        }

        // Chat content
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(messages) { message ->
                ChatBubble(message)
            }
            item {
                AiItineraryCard()
            }
        }

        // Input bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .border(0.5.dp, Border)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ask AI to add a lunch spot nearby...") },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Border,
                    focusedBorderColor =Teal
                ),
                trailingIcon = {
                    Icon(
                        Icons.Filled.Send,
                        contentDescription = "Send",
                        tint = Teal,
                        modifier = Modifier.clickable {
                            if (input.isNotBlank()) {
                                onSendMessage(input)
                                input = ""
                            }
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun PreferencePill(label: String, selected: Boolean = false) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) TealTint else Background)
            .border(1.dp, if (selected)Teal else Border, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = if (selected)Teal else Navy)
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.fromUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.fromUser) 16.dp else 4.dp,
                        bottomEnd = if (message.fromUser) 4.dp else 16.dp
                    )
                )
                .background(if (message.fromUser) Navy else White)
                .padding(12.dp)
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium,
                color = if (message.fromUser) White else Navy
            )
        }
    }
}

@Composable
private fun AiItineraryCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Navy)
                .padding(12.dp)
        ) {
            Text("Serene Kyoto: 3-Day Retreat", style = MaterialTheme.typography.titleMedium, color = White)
            Text(
                "Tailored history & nature overview",
                style = MaterialTheme.typography.bodySmall,
                color = White.copy(alpha = 0.8f)
            )
        }
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("DAY 1: HISTORIC TEMPLES", style = MaterialTheme.typography.labelMedium, color = Teal)
            ItineraryRow(time = "09:00 AM", title = "Arashiyama Bamboo Grove", detail = "Quiet morning walk before crowds arrive.")
            ItineraryRow(time = "11:30 AM", title = "Tea at Kichi Coffee", detail = "Matcha overlooking a Zen rock garden.")
        }
    }
}

@Composable
private fun ItineraryRow(time: String, title: String, detail: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(time, style = MaterialTheme.typography.labelMedium, color = Slate, modifier = Modifier.width(50.dp))
        Column {
            Text(title, style = MaterialTheme.typography.labelLarge, color = Navy)
            Text(detail, style = MaterialTheme.typography.bodySmall, color = Slate)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AiPlannerScreenPreview() {
    AiPlannerScreen()
}