package com.example.travel_planner.ui.screens.flights

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.travel_planner.ui.viewmodel.FlightsViewModel
import com.example.travel_planner.ui.viewmodel.UiState
import androidx.compose.ui.tooling.preview.Preview
import com.example.travel_planner.data.ApiFlightOffer
import com.example.travel_planner.ui.theme.Theme

@Composable
fun FlightDetailsScreen(
    flightOfferId: String,
    viewModel: FlightsViewModel,
    onBack: () -> Unit = {}
) {

    val context = LocalContext.current

    val flight by
    viewModel.selectedFlight.collectAsState()

    val bookingState by
    viewModel.bookingState.collectAsState()


    // ---------------------------------------------------------
    // OPEN BOOKING URL WHEN API SUCCEEDS
    // ---------------------------------------------------------

    LaunchedEffect(bookingState) {

        if (bookingState is UiState.Success) {

            val url =
                (bookingState as UiState.Success<String>).data

            val intent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
                )

            context.startActivity(intent)

            viewModel.clearBookingState()
        }
    }

    FlightDetailsContent(
        flightOfferId = flightOfferId,
        flight = flight,
        bookingState = bookingState,
        onBookClick = {
            viewModel.getBookingUrl(
                flightOfferId
            )
        },
        onBack = onBack
    )
}

@Composable
fun FlightDetailsContent(
    flightOfferId: String,
    flight: ApiFlightOffer?,
    bookingState: UiState<String>,
    onBookClick: () -> Unit,
    onBack: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Flight Details",
            style =
                MaterialTheme.typography.headlineSmall
        )


        if (flight == null) {

            CircularProgressIndicator()

        } else {

            Text(
                text =
                    "Provider: ${flight.provider ?: "-"}"
            )

            Text(
                text =
                    "Departure: ${flight.departureAirport ?: "-"}"
            )

            Text(
                text =
                    "Arrival: ${flight.arrivalAirport ?: "-"}"
            )

            Text(
                text =
                    "Flight ID: $flightOfferId"
            )


            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )


            Button(
                onClick = onBookClick,
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                if (bookingState is UiState.Loading) {

                    CircularProgressIndicator()

                } else {

                    Text("Book Flight")
                }
            }


            if (bookingState is UiState.Error) {

                Text(
                    text =
                        (bookingState
                                as UiState.Error).message,
                    color =
                        MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlightDetailsScreenPreview() {
    Theme {
        FlightDetailsContent(
            flightOfferId = "offer_123",
            flight = ApiFlightOffer(
                id = "offer_123",
                provider = "Sample Airline",
                departureAirport = "JFK",
                arrivalAirport = "LAX"
            ),
            bookingState = UiState.Idle,
            onBookClick = {}
        )
    }
}