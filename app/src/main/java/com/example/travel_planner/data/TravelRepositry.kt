package com.example.travel_planner.data

import com.example.travel_planner.dat.ApiTrip
import com.example.travel_planner.dat.CreateTripRequest
import com.example.travel_planner.model.Activity
import com.example.travel_planner.model.Destination
import com.example.travel_planner.model.Hotel
import com.example.travel_planner.model.Restaurant
import com.google.gson.Gson
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

    suspend fun loadDestinationsUi(search: String? = null, destinationType: String? = null, country: String? = null): List<Destination> =
        unwrap(
            api.getDestinations(
                search = search?.takeIf { it.isNotBlank() },
                destinationType = destinationType?.takeIf { it.isNotBlank() },
                country = country?.takeIf { it.isNotBlank() } // <-- added
            )
        ).destinations.map { it.toUiModel() }

    suspend fun loadDestinationUi(id: String): Destination =
        unwrap(api.getDestinationById(id)).toUiModel()

    suspend fun loadHotelsUi(search: String? = null, destinationId: String? = null): List<Hotel> =
        unwrap(
            api.getHotels(
                search = search?.takeIf { it.isNotBlank() },
                destinationId = destinationId?.takeIf { it.isNotBlank() } // <-- added
            )
        ).hotels.map { it.toUiModel() }

    suspend fun loadHotelUi(id: String): Hotel =
        unwrap(api.getHotelById(id)).toUiModel()

    suspend fun loadRestaurantsUi(destinationId: String? = null): List<Restaurant> =
        unwrap(api.getRestaurants(destinationId = destinationId?.takeIf { it.isNotBlank() })).restaurants.map { it.toUiModel() } // <-- added

    suspend fun loadActivitiesUi(destinationId: String? = null): List<Activity> =
        unwrap(api.getActivities(destinationId = destinationId?.takeIf { it.isNotBlank() })).activities.map { it.toUiModel() } // <-- added

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


    suspend fun createTrip(
        tripName: String,
        destinationId: String,
        startDate: String,
        endDate: String
    ): ApiTrip = unwrap(
        api.createTrip(
            CreateTripRequest(
                tripName = tripName,
                destinationId = destinationId,
                startDate = startDate,
                endDate = endDate
            )
        )
    )

    suspend fun getMyTrips(): List<ApiTrip> {
        val json = unwrap(api.getTrips())
        val gson = Gson()
        return when {
            json.isJsonArray ->
                json.asJsonArray.map { gson.fromJson(it, ApiTrip::class.java) }
            json.isJsonObject && json.asJsonObject.has("trips") ->
                json.asJsonObject.getAsJsonArray("trips").map { gson.fromJson(it, ApiTrip::class.java) }
            else -> emptyList()
        }
    }

    suspend fun getTrip(id: String): ApiTrip =
        unwrap(api.getTripById(id))
}