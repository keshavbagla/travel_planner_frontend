package com.example.travel_planner.ui.screens.flights

import android.app.DatePickerDialog
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.platform.LocalContext
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
import java.util.Calendar

/**
 * The backend API doc (17 documented endpoints) has no airport-search
 * endpoint — GET /destinations doesn't return IATA codes for the list view
 * either (only GET /destinations/:id has a primaryAirportIata field, one
 * at a time). So this is a curated static list, not API-backed. If the
 * backend adds an airport-search endpoint later, swap this out for a real
 * TravelRepository call + debounced search.
 */
private val AIRPORTS = listOf(
    "DEL" to "Delhi, India", "BOM" to "Mumbai, India", "BLR" to "Bengaluru, India",
    "MAA" to "Chennai, India", "CCU" to "Kolkata, India", "HYD" to "Hyderabad, India",
    "GOI" to "Goa, India", "IXR" to "Ranchi, India", "PNQ" to "Pune, India",
    "AMD" to "Ahmedabad, India", "COK" to "Kochi, India", "JAI" to "Jaipur, India",
    "LKO" to "Lucknow, India", "PAT" to "Patna, India", "IXC" to "Chandigarh, India",
    "CDG" to "Paris, France", "LHR" to "London, UK", "JFK" to "New York, USA",
    "DXB" to "Dubai, UAE", "SIN" to "Singapore", "NRT" to "Tokyo, Japan",
    "SYD" to "Sydney, Australia", "FRA" to "Frankfurt, Germany", "AMS" to "Amsterdam, Netherlands",
    "BKK" to "Bangkok, Thailand"
)

private val TRAVEL_CLASSES = listOf("ECONOMY", "BUSINESS", "FIRST")

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
    var adults by remember { mutableStateOf(1) }
    var travelClass by remember { mutableStateOf("ECONOMY") }

    Column(modifier = Modifier.fillMaxSize().background(Background)) {

        Column(
            modifier = Modifier.fillMaxWidth().background(Navy).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Compare Flights", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = White)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AirportField(
                    label = "FROM",
                    value = departureIata,
                    onValueChange = { departureIata = it },
                    modifier = Modifier.weight(1f)
                )
                AirportField(
                    label = "TO",
                    value = arrivalIata,
                    onValueChange = { arrivalIata = it },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DatePickerField(
                    label = "DEPARTURE DATE",
                    value = outboundDate,
                    onValueChange = { outboundDate = it },
                    modifier = Modifier.weight(1f)
                )
                PassengerStepperField(
                    label = "PASSENGERS",
                    count = adults,
                    onCountChange = { adults = it },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TravelClassField(
                    label = "CLASS",
                    value = travelClass,
                    onValueChange = { travelClass = it },
                    modifier = Modifier.weight(1f)
                )
                Box(modifier = Modifier.weight(1f)) // spacer to keep the row balanced
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Coral)
                    .clickable {
                        viewModel.search(departureIata, arrivalIata, outboundDate, adults, travelClass)
                        onSearchClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("Search Flights", color = White, style = MaterialTheme.typography.labelLarge)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val countLabel = when (val state = uiState) {
                is UiState.Success -> "${state.data.size} Flight Recommendations"
                is UiState.Loading -> "Searching..."
                is UiState.Error -> "Search failed"
                is UiState.Idle -> "Enter route and search"
            }
            Text(countLabel, style = MaterialTheme.typography.labelMedium, color = Navy)
        }

        when (val state = uiState) {
            is UiState.Idle -> {
                Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text(
                        "Pick departure, arrival, and a date above, then tap Search Flights.",
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
                        "Couldn't search flights: {state.message}" +
                                if (state.message.contains("401")) "\n\nThis usually means you're not logged in — the flight search endpoint requires a valid session." else "",
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
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.data) { flight ->
                            FlightCard(flight = flight, onSelect = { onSelectFlight(flight) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(text, style = MaterialTheme.typography.labelSmall, color = White.copy(alpha = 0.7f))
}

@Composable
private fun AirportField(label: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val filtered = remember(value) {
        if (value.isBlank()) AIRPORTS
        else AIRPORTS.filter { (code, city) -> code.contains(value, true) || city.contains(value, true) }
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        FieldLabel(label)
        Box {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(White)
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text("e.g. DEL", style = MaterialTheme.typography.bodySmall, color = Slate)
                }
                BasicTextField(
                    value = value,
                    onValueChange = {
                        onValueChange(it.uppercase())
                        expanded = true
                    },
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 13.sp, color = Navy),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true }
                )
            }
            DropdownMenu(expanded = expanded && filtered.isNotEmpty(), onDismissRequest = { expanded = false }) {
                filtered.take(8).forEach { (code, city) ->
                    DropdownMenuItem(
                        text = { Text("$code — $city") },
                        onClick = {
                            onValueChange(code)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DatePickerField(label: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        FieldLabel(label)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(White)
                .clickable {
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            onValueChange("%04d-%02d-%02d".format(year, month + 1, dayOfMonth))
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).apply {
                        datePicker.minDate = System.currentTimeMillis() - 1000 // no past dates
                    }.show()
                }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(Icons.Filled.CalendarToday, contentDescription = null, tint = Slate, modifier = Modifier.size(14.dp))
            Text(
                value.ifEmpty { "Select date" },
                style = MaterialTheme.typography.bodySmall,
                color = if (value.isEmpty()) Slate else Navy
            )
        }
    }
}

@Composable
private fun PassengerStepperField(label: String, count: Int, onCountChange: (Int) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        FieldLabel(label)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(White)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (count > 1) Border else Border.copy(alpha = 0.4f))
                    .clickable(enabled = count > 1) { onCountChange(count - 1) },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Remove, contentDescription = "Fewer passengers", tint = Navy, modifier = Modifier.size(14.dp))
            }
            Text("$count Adult${if (count == 1) "" else "s"}", style = MaterialTheme.typography.bodySmall, color = Navy)
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Border)
                    .clickable { onCountChange(count + 1) },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Add, contentDescription = "More passengers", tint = Navy, modifier = Modifier.size(14.dp))
            }
        }
    }
}

@Composable
private fun TravelClassField(label: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        FieldLabel(label)
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(White)
                    .clickable { expanded = true }
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(value.lowercase().replaceFirstChar(Char::uppercase), style = MaterialTheme.typography.bodySmall, color = Navy)
                Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Slate, modifier = Modifier.size(16.dp))
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                TRAVEL_CLASSES.forEach { cls ->
                    DropdownMenuItem(
                        text = { Text(cls.lowercase().replaceFirstChar(Char::uppercase)) },
                        onClick = {
                            onValueChange(cls)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FlightCard(flight: Flight, onSelect: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(White)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(Border))
                Text(flight.airline, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Navy)
            }
            Text("$${flight.price}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Navy)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(flight.departTime, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Navy)
                Text(flight.departAirport, style = MaterialTheme.typography.bodySmall, color = Slate)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(flight.duration, style = MaterialTheme.typography.labelMedium, color = Teal)
                Box(modifier = Modifier.width(60.dp).height(1.dp).background(Border))
                Text(flight.stopsLabel, style = MaterialTheme.typography.bodySmall, color = Slate)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(flight.arriveTime, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Navy)
                Text(flight.arriveAirport, style = MaterialTheme.typography.bodySmall, color = Slate)
            }
        }

        Column {
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Border))
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Round-trip / inclusive", style = MaterialTheme.typography.bodySmall, color = Slate)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Teal)
                        .clickable { onSelect() }
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text("Select", color = White, style = MaterialTheme.typography.labelMedium)
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