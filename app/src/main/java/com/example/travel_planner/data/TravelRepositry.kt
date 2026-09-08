package com.example.travel_planner.data

import com.example.travel_planner.model.Activity
import com.example.travel_planner.model.Destination
import com.example.travel_planner.model.Hotel
import com.example.travel_planner.model.Restaurant
import retrofit2.Response
import java.io.IOException

object TravelRepository {

    private val api = RetrofitClient.apiService

    private fun <T> unwrap(response: Response<ApiResponse<T>>): T {
        if (!response.isSuccessful) throw IOException("HTTP ${response.code()}: ${response.message()}")
        val body = response.body() ?: throw IOException("Empty response body")
        if (!body.success) throw IOException(body.message)
        return body.data ?: throw IOException("No data in response")
    }

    suspend fun loadDestinationsUi(search: String? = null): List<Destination> =
        unwrap(api.getDestinations(search = search?.takeIf { it.isNotBlank() })).destinations.map { it.toUiModel() }

    suspend fun loadDestinationUi(id: String): Destination =
        unwrap(api.getDestinationById(id)).toUiModel()

    suspend fun loadHotelsUi(search: String? = null): List<Hotel> =
        unwrap(api.getHotels(search = search?.takeIf { it.isNotBlank() })).hotels.map { it.toUiModel() }

    suspend fun loadHotelUi(id: String): Hotel =
        unwrap(api.getHotelById(id)).toUiModel()

    suspend fun loadRestaurantsUi(): List<Restaurant> =
        unwrap(api.getRestaurants()).restaurants.map { it.toUiModel() }

    suspend fun loadActivitiesUi(): List<Activity> =
        unwrap(api.getActivities()).activities.map { it.toUiModel() }

    suspend fun loadActivityUi(id: String): Activity =
        unwrap(api.getActivityById(id)).toUiModel()

    suspend fun searchExternalActivitiesUi(destinationId: String): List<ApiExternalActivity> =
        unwrap(api.searchExternalActivities(destinationId)).activities

    suspend fun searchFlights(request: FlightOfferSearchRequest): FlightOfferSearchData =
        unwrap(api.searchFlightOffers(request))

    suspend fun selectFlight(flightOfferId: String): ApiFlightOffer =
        unwrap(api.selectFlightOffer(FlightOfferSelectRequest(flightOfferId)))

    suspend fun getFlightBookingUrl(flightOfferId: String): FlightBookingUrlData =
        unwrap(api.getFlightBookingUrl(flightOfferId))

    suspend fun createBooking(request: CreateBookingRequest): ApiBooking =
        unwrap(api.createBooking(request))

    suspend fun redirectBooking(bookingId: String): BookingRedirectData =
        unwrap(api.redirectBooking(bookingId))

    suspend fun getMyBookings(): BookingsListData =
        unwrap(api.getMyBookings())

    suspend fun getBooking(id: String): ApiBooking =
        unwrap(api.getBookingById(id))

    suspend fun searchBookings(keyword: String): List<ApiBooking> =
        unwrap(api.searchBookings(keyword))

    suspend fun filterBookings(status: String): List<ApiBooking> =
        unwrap(api.filterBookings(mapOf("status" to status)))
}