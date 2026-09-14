package com.example.travel_planner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travel_planner.data.FlightOfferSearchRequest
import com.example.travel_planner.data.TravelRepository
import com.example.travel_planner.data.toUiModel
import com.example.travel_planner.model.Flight
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FlightsViewModel : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<List<Flight>>>(UiState.Idle)

    val uiState: StateFlow<UiState<List<Flight>>> =
        _uiState.asStateFlow()

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
        val departure = departureIata.trim().uppercase()
        val arrival = arrivalIata.trim().uppercase()
        val date = outboundDate.trim()

        if (departure.isBlank()) {
            _uiState.value =
                UiState.Error("Please select a departure airport")
            return
        }

        if (arrival.isBlank()) {
            _uiState.value =
                UiState.Error("Please select an arrival airport")
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
                UiState.Error("Please select a departure date")
            return
        }

        if (adults < 1) {
            _uiState.value =
                UiState.Error("At least one adult is required")
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Loading

            _uiState.value = try {

                val result =
                    TravelRepository.searchFlights(
                        FlightOfferSearchRequest(
                            departureIata = departure,
                            arrivalIata = arrival,
                            outboundDate = date,
                            adults = adults,
                            travelClass = travelClass.uppercase(),
                            currency = "INR"
                        )
                    )

                UiState.Success(
                    result.offers.map { it.toUiModel() }
                )

            } catch (e: Exception) {

                UiState.Error(
                    e.message ?: "Flight search failed"
                )
            }
        }
    }

    fun selectFlight(flight: Flight) {

        if (flight.id.isBlank()) {
            _bookingState.value =
                UiState.Error("Flight ID is missing")
            return
        }

        viewModelScope.launch {

            _bookingState.value = UiState.Loading

            _bookingState.value = try {

                TravelRepository.selectFlight(
                    flightOfferId = flight.id
                )

                UiState.Success(
                    "Flight selected successfully"
                )

            } catch (e: Exception) {

                UiState.Error(
                    e.message ?: "Unable to select flight"
                )
            }
        }
    }

    fun clearBookingState() {
        _bookingState.value = UiState.Idle
    }
}