package com.example.travel_planner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travel_planner.ui.theme.*
import androidx.compose.ui.tooling.preview.Preview
private data class SettingsRow(val label: String, val icon: ImageVector)

private val rows = listOf(
    SettingsRow("Personal Info", Icons.Filled.Person),
    SettingsRow("Travel Preferences", Icons.Filled.Explore),
    SettingsRow("Payment Methods", Icons.Filled.CreditCard),
    SettingsRow("Saved Places", Icons.Filled.Bookmark),
    SettingsRow("Notifications", Icons.Filled.Notifications),
    SettingsRow("Privacy & Security", Icons.Filled.Lock),
    SettingsRow("Help & Support", Icons.Filled.HelpOutline)
)


@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit = {},
    onRowClick: (String) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().background(Background)) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("Profile Settings", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Navy)
            Text(
                "Manage your info, travel documents, and preferences.",
                style = MaterialTheme.typography.bodyMedium,
                color = Slate
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(White)
                .border(1.dp, Border, RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(modifier = Modifier.size(64.dp).clip(CircleShape).background(Border))
            Column {
                Text("Sarah Jenkins", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Navy)
                Text("sarah.jenkins@voyago.io", style = MaterialTheme.typography.bodySmall, color = Slate)
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(rows) { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onRowClick(row.label) }
                        .padding(vertical = 14.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(row.icon, contentDescription = null, tint = Slate, modifier = Modifier.size(20.dp))
                    Text(row.label, style = MaterialTheme.typography.bodyLarge, color = Navy)
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onLogoutClick() }
                        .padding(vertical = 14.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Filled.Logout, contentDescription = null, tint = Teal, modifier = Modifier.size(20.dp))
                    Text("Log Out", style = MaterialTheme.typography.bodyLarge, color = Teal)
                }
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    ProfileScreen()
}


