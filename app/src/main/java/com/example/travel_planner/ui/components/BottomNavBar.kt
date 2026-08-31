package com.example.travel_planner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.travel_planner.navigation.Destination
import com.example.travel_planner.ui.theme.*

private data class NavItem(
    val destination: Destination,
    val label: String,
    val icon: ImageVector
)

private val navItems = listOf(
    NavItem(Destination.Home, "Home", Icons.Filled.Home),
    NavItem(Destination.Destinations, "Explore", Icons.Filled.Map),
    NavItem(Destination.Flights, "Trips", Icons.Filled.Work),
    NavItem(Destination.AiPlanner, "AI Planner", Icons.Outlined.AutoAwesome)
)

@Composable
fun BottomNavBar(
    current: Destination,
    onNavigate: (Destination) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .border(
                width = 0.5.dp,
                color = Border
            )
            .height(84.dp)
            .padding(
                horizontal = 12.dp,
                vertical = 8.dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            navItems.forEach { item ->

                val selected = item.destination == current

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            onNavigate(item.destination)
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    if (item.destination == Destination.AiPlanner) {

                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(
                                    color = TealTint,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = Teal,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                    } else {

                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (selected) Teal else Slate,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (selected) Teal else Slate
                    )
                }
            }
        }
    }
}
