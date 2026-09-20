package com.example.travel_planner.ui.screens.flights

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travel_planner.ui.theme.*
import com.example.travel_planner.ui.viewmodel.FlightsViewModel
import com.example.travel_planner.ui.viewmodel.UiState
import com.example.travel_planner.data.ApiFlightOffer
import androidx.compose.ui.tooling.preview.Preview
import java.text.NumberFormat
import java.util.Locale

@Composable
fun FlightDetailsScreen(
    flightOfferId: String,
    viewModel: FlightsViewModel,
    onBack: () -> Unit = {}
) {
    val flight by viewModel.selectedFlight.collectAsState()
    val bookingState by viewModel.bookingState.collectAsState()

    FlightDetailsContent(
        flightOfferId = flightOfferId,
        flight = flight,
        bookingState = bookingState,
        onBookFlightClick = { viewModel.getBookingUrl(flightOfferId) },
        onClearBookingState = { viewModel.clearBookingState() },
        onBack = onBack
    )
}

@Composable
fun FlightDetailsContent(
    flightOfferId: String,
    flight: ApiFlightOffer?,
    bookingState: UiState<String>,
    onBookFlightClick: () -> Unit,
    onClearBookingState: () -> Unit,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current

    LaunchedEffect(bookingState) {
        if (bookingState is UiState.Success) {
            val url =
                (bookingState as UiState.Success<String>).data

            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
                )
                context.startActivity(intent)
            } catch (_: Exception) {
            }

            onClearBookingState()
        }
    }
    if (flight == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PrimaryBlue)
        }

        return
    }

    val selectedFlight = flight!!

    val provider =
        selectedFlight.provider?.takeIf { it.isNotBlank() }
            ?: "Airline"

    val departure =
        selectedFlight.departureAirport?.takeIf { it.isNotBlank() }
            ?: "DEP"

    val arrival =
        selectedFlight.arrivalAirport?.takeIf { it.isNotBlank() }
            ?: "ARR"

    val priceInr = getFlightPrice(selectedFlight)

    val formattedPrice = NumberFormat
        .getNumberInstance(Locale("en", "IN"))
        .format(priceInr)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 12.dp,
                    bottom = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            RoundIconButton(
                icon = Icons.Default.ArrowBack,
                contentDescription = "Back",
                onClick = onBack
            )

            Text(
                text = "Booking Details",
                color = Navy,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            RoundIconButton(
                icon = Icons.Default.Share,
                contentDescription = "Share",
                onClick = {
                    val shareText =
                        "Flight $departure → $arrival\n" +
                                "Provider: $provider\n" +
                                "Fare: ₹$formattedPrice"

                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }

                    context.startActivity(
                        Intent.createChooser(
                            sendIntent,
                            "Share Flight"
                        )
                    )
                }
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 34.dp)
        ) {

            Spacer(modifier = Modifier.height(18.dp))


            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(24.dp))
                    .background(LightBlue)
                    .padding(
                        horizontal = 22.dp,
                        vertical = 11.dp
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = "VIA GOOGLE FLIGHTS",
                        color = PrimaryBlue,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(26.dp))


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(38.dp))
                    .background(Color.White)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 34.dp,
                            end = 34.dp,
                            top = 30.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFF2EC))
                            .border(
                                width = 3.dp,
                                color = Color(0xFFFFE1D4),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = provider
                                .take(2)
                                .uppercase(),
                            color = Color(0xFFFF5A1F),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 20.dp)
                    ) {

                        Text(
                            text = provider,
                            color = Navy,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Flight • Boeing 737",
                            color = Slate,
                            fontSize = 17.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(13.dp))
                            .background(Color(0xFFF0F2F5))
                            .padding(
                                horizontal = 15.dp,
                                vertical = 10.dp
                            )
                    ) {
                        Text(
                            text = "FZ",
                            color = Navy,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(34.dp))

                /*
                 * Route
                 */

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 34.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = departure,
                            color = Navy,
                            fontSize = 47.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = airportName(departure),
                            color = Slate,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "14:30",
                            color = Color(0xFF9AAACA),
                            fontSize = 19.sp
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {

                        Text(
                            text = "3h 45m",
                            color = Slate,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(13.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(3.dp)
                                    .background(Color(0xFFDDE4EF))
                            )

                            Icon(
                                imageVector = Icons.Default.FlightTakeoff,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .size(32.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(3.dp)
                                    .background(Color(0xFFDDE4EF))
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Direct",
                            color = Color(0xFF19BDB2),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {

                        Text(
                            text = arrival,
                            color = Navy,
                            fontSize = 47.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = airportName(arrival),
                            color = Slate,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "19:45",
                            color = Color(0xFF9AAACA),
                            fontSize = 19.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(Color(0xFFE1E7F0))
                )

                Spacer(modifier = Modifier.height(34.dp))

                /*
                 * Booking type + passengers
                 */

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 34.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    InfoBox(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.ConfirmationNumber,
                        title = "Booking Type",
                        value = "Airline Booking"
                    )

                    InfoBox(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Groups,
                        title = "Passengers",
                        value = "Group Booking"
                    )
                }

                Spacer(modifier = Modifier.height(26.dp))

                /*
                 * Schedule
                 */

                InfoBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 34.dp),
                    icon = Icons.Default.CalendarMonth,
                    title = "SCHEDULE",
                    value = "Thursday, 18 Sep 2026"
                )

                Spacer(modifier = Modifier.height(34.dp))
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFFFFF8F9))
                    .border(
                        width = 2.dp,
                        color = Color(0xFFFFC6CC),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFE93645),
                        modifier = Modifier.size(28.dp)
                    )
                }

                Column(
                    modifier = Modifier.padding(start = 18.dp)
                ) {

                    Text(
                        text = "BAGGAGE INFO",
                        color = Color(0xFFE93645),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "No baggage info available",
                        color = Navy,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(26.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White)
                    .border(
                        width = 2.dp,
                        color = Color(0xFFDDE4EF),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .clickable {
                        try {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://www.flydubai.com")
                                )
                            )
                        } catch (_: Exception) {
                        }
                    }
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF0F6FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(29.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 18.dp)
                ) {

                    Text(
                        text = "OFFICIAL WEBSITE",
                        color = Slate,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "www.flydubai.com",
                        color = PrimaryBlue,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Icon(
                    imageVector = Icons.Default.OpenInNew,
                    contentDescription = "Open website",
                    tint = Slate,
                    modifier = Modifier.size(25.dp)
                )
            }

            Spacer(modifier = Modifier.height(35.dp))
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(
                    start = 34.dp,
                    end = 34.dp,
                    top = 24.dp,
                    bottom = 14.dp
                )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "TOTAL FARE",
                        color = Slate,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Includes all taxes & fees",
                        color = Color(0xFF9AAACA),
                        fontSize = 16.sp
                    )
                }

                Text(
                    text = "₹$formattedPrice",
                    color = Navy,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = onBookFlightClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue
                ),
                enabled = bookingState !is UiState.Loading
            ) {

                if (bookingState is UiState.Loading) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(25.dp),
                        color = Color.White,
                        strokeWidth = 3.dp
                    )

                } else {

                    Text(
                        text = "Book Flight",
                        color = Color.White,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (bookingState is UiState.Error) {

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = (bookingState as UiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp
                )
            }
        }
    }
}


@Composable
private fun RoundIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(62.dp)
            .clip(CircleShape)
            .background(Color.White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Navy,
            modifier = Modifier.size(28.dp)
        )
    }
}


@Composable
private fun InfoBox(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .border(
                width = 2.dp,
                color = Color(0xFFDDE4EF),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryBlue,
            modifier = Modifier.size(27.dp)
        )

        Column(
            modifier = Modifier.padding(start = 14.dp)
        ) {

            Text(
                text = title,
                color = Slate,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = value,
                color = Navy,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun airportName(code: String): String {

    return when (code.uppercase()) {

        "DEL" -> "Delhi Indira"
        "DXB" -> "Dubai Int."
        "BOM" -> "Mumbai Int."
        "BLR" -> "Bengaluru Int."
        "MAA" -> "Chennai Int."
        "CCU" -> "Kolkata Int."
        "HYD" -> "Hyderabad Int."
        "GOI" -> "Goa Int."
        "LHR" -> "London Heathrow"
        "CDG" -> "Paris Charles"
        "JFK" -> "New York JFK"
        "SIN" -> "Singapore"
        "NRT" -> "Tokyo Narita"
        "SYD" -> "Sydney Int."
        "FRA" -> "Frankfurt Int."
        "AMS" -> "Amsterdam"
        "BKK" -> "Bangkok"

        else -> "$code Airport"
    }
}

private fun getFlightPrice(
    flight: Any
): Double {

    return 15100.0
}

@Preview(showBackground = true)
@Composable
fun FlightDetailsScreenPreview() {
    Theme {
        FlightDetailsContent(
            flightOfferId = "sample-id",
            flight = ApiFlightOffer(
                id = "sample-id",
                departureAirport = "DEL",
                arrivalAirport = "DXB",
                provider = "Flydubai"
            ),
            bookingState = UiState.Idle,
            onBookFlightClick = {},
            onClearBookingState = {}
        )
    }
}