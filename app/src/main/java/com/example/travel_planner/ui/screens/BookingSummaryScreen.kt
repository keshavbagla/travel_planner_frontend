package com.example.travel_planner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travel_planner.ui.theme.*

@Composable
fun BookingSummaryScreen(onConfirmBooking: () -> Unit = {}) {
    Column(modifier = Modifier.fillMaxSize().background(Background).padding(16.dp)) {
        Text("Booking Summary", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Navy)
        Text(
            "Review your trip before confirming",
            style = MaterialTheme.typography.bodyMedium,
            color = Slate,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        SummaryCard(title = "Flight", detail = "Japan Airlines • SFO → KIX • Oct 14", price = 1120)
        Box(modifier = Modifier.height(12.dp))
        SummaryCard(title = "Hotel", detail = "The Ritz-Carlton, Kyoto • 4 nights", price = 1800)
        Box(modifier = Modifier.height(12.dp))
        SummaryCard(title = "Activities", detail = "3 experiences booked", price = 145)

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Navy)
            Text("$3,065", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Navy)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(top = 20.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Coral)
                .clickable { onConfirmBooking() },
            contentAlignment = Alignment.Center
        ) {
            Text("Confirm & Pay", color = White, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun SummaryCard(title: String, detail: String, price: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(White)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Navy)
            Text(detail, style = MaterialTheme.typography.bodySmall, color = Slate)
        }
        Text("$$price", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Navy)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BookingSummaryScreenPreview() {
    BookingSummaryScreen()
}