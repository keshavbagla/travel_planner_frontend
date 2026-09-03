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

    private val _uiState = MutableStateFlow<UiState<List<Flight>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<Flight>>> = _uiState.asStateFlow()


    fun search(departureIata: String, arrivalIata: String, outboundDate: String, adults: Int = 1) {
        if (departureIata.isBlank() || arrivalIata.isBlank() || outboundDate.isBlank()) {
            _uiState.value = UiState.Error("Enter departure, arrival, and date first")
            return
        }
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = try {
                val result = TravelRepository.searchFlights(
                    FlightOfferSearchRequest(
                        departureIata = departureIata.trim().uppercase(),
                        arrivalIata = arrivalIata.trim().uppercase(),
                        outboundDate = outboundDate.trim(),
                        adults = adults
                    )
                )
                UiState.Success(result.offers.map { it.toUiModel() })
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Flight search failed")
            }
        }
    }
}