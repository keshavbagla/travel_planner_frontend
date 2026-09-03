package com.example.travel_planner.ui.screens.flights

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travel_planner.model.Flight
import com.example.travel_planner.ui.theme.*
import com.example.travel_planner.ui.viewmodel.FlightsViewModel
import com.example.travel_planner.ui.viewmodel.UiState

@Composable
fun FlightsScreen(
    onSearchClick: () -> Unit = {},
    onSelectFlight: (Flight) -> Unit = {},
    viewModel: FlightsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var departureIata by remember { mutableStateOf("") }
    var arrivalIata by remember { mutableStateOf("") }
    var outboundDate by remember { mutableStateOf("") } // "yyyy-MM-dd"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Navy)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Compare Flights",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = White
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EditableField(
                        value = departureIata,
                        onValueChange = { departureIata = it.uppercase() },
                        placeholder = "From (e.g. DEL)",
                        modifier = Modifier.weight(1f)
                    )

                    EditableField(
                        value = arrivalIata,
                        onValueChange = { arrivalIata = it.uppercase() },
                        placeholder = "To (e.g. CDG)",
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EditableField(
                        value = outboundDate,
                        onValueChange = { outboundDate = it },
                        placeholder = "yyyy-MM-dd",
                        modifier = Modifier.weight(1f)
                    )

                    InfoField(
                        text = "1 Adult, Economy",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .clip(RoundedCornerShape(19.dp))
                    .background(Coral)
                    .clickable {
                        viewModel.search(departureIata, arrivalIata, outboundDate)
                        onSearchClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Search Flights",
                    color = White,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        // Filter row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val countLabel = when (val state = uiState) {
                is UiState.Success -> "${state.data.size} Flight Recommendations"
                is UiState.Loading -> "Searching..."
                is UiState.Error -> "Search failed"
                is UiState.Idle -> "Enter route and search"
            }

            Text(
                text = countLabel,
                style = MaterialTheme.typography.labelMedium,
                color = Navy
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = "Filters",
                    style = MaterialTheme.typography.labelMedium,
                    color = Teal
                )

                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Filters",
                    tint = Teal,
                    modifier = Modifier.size(12.dp)
                )
            }
        }

        when (val state = uiState) {
            is UiState.Idle -> {
                Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text(
                        "Enter a departure airport, arrival airport, and date above, then tap Search Flights.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate
                    )
                }
            }
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text(
                        "Couldn't search flights: ${state.message}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate
                    )
                }
            }
            is UiState.Success -> {
                if (state.data.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                        Text("No flights found for that route/date.", style = MaterialTheme.typography.bodyMedium, color = Slate)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 4.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.data) { flight ->
                            FlightCard(
                                flight = flight,
                                onSelect = { onSelectFlight(flight) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EditableField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(White)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        if (value.isEmpty()) {
            Text(placeholder, style = MaterialTheme.typography.bodySmall, color = Slate)
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(fontSize = 13.sp, color = Navy),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun InfoField(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(White)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {

        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = Navy
        )
    }
}

@Composable
private fun FlightCard(
    flight: Flight,
    onSelect: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(White)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Airline + Price
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Border)
                )

                Text(
                    text = flight.airline,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Navy
                )
            }

            Text(
                text = "$${flight.price}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Navy
            )
        }

        // Flight timing
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // Departure
            Column {

                Text(
                    text = flight.departTime,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Navy
                )

                Text(
                    text = flight.departAirport,
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate
                )
            }

            // Duration
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = flight.duration,
                    style = MaterialTheme.typography.labelMedium,
                    color = Teal
                )

                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(1.dp)
                        .background(Border)
                )

                Text(
                    text = flight.stopsLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate
                )
            }

            // Arrival
            Column(
                horizontalAlignment = Alignment.End
            ) {

                Text(
                    text = flight.arriveTime,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Navy
                )

                Text(
                    text = flight.arriveAirport,
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate
                )
            }
        }

        // Bottom section
        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Border)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Round-trip / inclusive",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Teal)
                        .clickable {
                            onSelect()
                        }
                        .padding(
                            horizontal = 16.dp,
                            vertical = 6.dp
                        )
                ) {

                    Text(
                        text = "Select",
                        color = White,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FlightsScreenPreview() {
    FlightsScreen()
}