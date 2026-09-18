package com.example.travel_planner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travel_planner.data.ApiFlightOffer
import com.example.travel_planner.data.FlightOfferSearchRequest
import com.example.travel_planner.data.TravelRepository
import com.example.travel_planner.data.toUiModel
import com.example.travel_planner.model.Flight
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class FlightsViewModel : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<List<Flight>>>(UiState.Idle)

    val uiState: StateFlow<UiState<List<Flight>>> =
        _uiState.asStateFlow()

    private val _selectedFlight =
        MutableStateFlow<ApiFlightOffer?>(null)

    val selectedFlight: StateFlow<ApiFlightOffer?> =
        _selectedFlight.asStateFlow()

    private val _selectionState =
        MutableStateFlow<UiState<String>>(UiState.Idle)

    val selectionState: StateFlow<UiState<String>> =
        _selectionState.asStateFlow()
    private val _bookingState =
        MutableStateFlow<UiState<String>>(UiState.Idle)

    val bookingState: StateFlow<UiState<String>> =
        _bookingState.asStateFlow()

    fun search(
        departureIata: String,
        arrivalIata: String,
        outboundDate: String,
        adults: Int = 1,
        travelClass: String = "ECONOMY"
    ) {

        val departure =
            departureIata.trim().uppercase()

        val arrival =
            arrivalIata.trim().uppercase()

        val date =
            outboundDate.trim()

        if (departure.isBlank()) {
            _uiState.value =
                UiState.Error(
                    "Please select a departure airport"
                )
            return
        }

        if (arrival.isBlank()) {
            _uiState.value =
                UiState.Error(
                    "Please select an arrival airport"
                )
            return
        }

        if (departure == arrival) {
            _uiState.value =
                UiState.Error(
                    "Departure and arrival airports must be different"
                )
            return
        }

        if (date.isBlank()) {
            _uiState.value =
                UiState.Error(
                    "Please select a departure date"
                )
            return
        }

        if (adults < 1) {
            _uiState.value =
                UiState.Error(
                    "At least one adult is required"
                )
            return
        }

        viewModelScope.launch {

            _uiState.value =
                UiState.Loading

            _uiState.value =
                try {

                    val result =
                        TravelRepository.searchFlights(
                            FlightOfferSearchRequest(
                                departureIata = departure,
                                arrivalIata = arrival,
                                outboundDate = date,
                                adults = adults,
                                travelClass =
                                    travelClass.uppercase(),
                                currency = "INR"
                            )
                        )

                    UiState.Success(
                        result.offers.map {
                            it.toUiModel()
                        }
                    )

                } catch (e: Exception) {

                    UiState.Error(
                        e.message
                            ?: "Flight search failed"
                    )
                }
        }
    }

    fun selectFlight(flight: Flight) {

        if (flight.id.isBlank()) {

            _selectionState.value =
                UiState.Error(
                    "Flight ID is missing"
                )

            return
        }

        viewModelScope.launch {

            _selectionState.value =
                UiState.Loading

            try {

                TravelRepository.selectFlight(
                    flightOfferId = flight.id
                )


                val details =
                    TravelRepository.getFlightBookingDetails(
                        flightOfferId = flight.id
                    )

                _selectedFlight.value =
                    details

                _selectionState.value =
                    UiState.Success(
                        flight.id
                    )

            } catch (e: Exception) {

                _selectionState.value =
                    UiState.Error(
                        e.message
                            ?: "Unable to select flight"
                    )
            }
        }
    }
    fun getBookingUrl(
        flightOfferId: String
    ) {

        if (flightOfferId.isBlank()) {

            _bookingState.value =
                UiState.Error(
                    "Flight ID is missing"
                )

            return
        }

        viewModelScope.launch {

            _bookingState.value =
                UiState.Loading

            _bookingState.value =
                try {

                    val result =
                        TravelRepository.getFlightBookingUrl(
                            flightOfferId = flightOfferId
                        )

                    val url =
                        result.bookingUrl
                            ?: throw IOException(
                                "Booking URL is missing"
                            )

                    UiState.Success(url)

                } catch (e: Exception) {

                    UiState.Error(
                        e.message
                            ?: "Unable to get booking URL"
                    )
                }
        }
    }

    fun clearSelectionState() {
        _selectionState.value =
            UiState.Idle
    }

    fun clearBookingState() {
        _bookingState.value =
            UiState.Idle
    }
}