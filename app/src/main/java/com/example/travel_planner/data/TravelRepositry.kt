package com.example.travel_planner.data

import com.example.travel_planner.model.Activity
import com.example.travel_planner.model.Destination
import com.example.travel_planner.model.Hotel
import com.example.travel_planner.model.Restaurant
import com.google.gson.JsonElement
import java.io.IOException
import retrofit2.Response
object TravelRepository {

    private val api = RetrofitClient.apiService

    private fun <T> unwrap(
        response: Response<ApiResponse<T>>
    ): T {

        if (!response.isSuccessful) {
            throw IOException(
                "HTTP ${response.code()}: ${response.message()}"
            )
        }

        val body = response.body()
            ?: throw IOException("Empty response body")

        if (!body.success) {
            throw IOException(
                body.message.ifBlank {
                    "Request failed"
                }
            )
        }

        return body.data
            ?: throw IOException("No data in response")
    }
    suspend fun getDestinations(
        destinationType: String? = null,
        country: String? = null
    ): DestinationsData {

        return unwrap(
            api.getDestinations(
                destinationType = destinationType,
                country = country
            )
        )
    }
    suspend fun searchDestinations(
        keyword: String,
        region: String? = null,
        budgetTier: String? = null,
        season: String? = null,
        tripType: String? = null
    ): DestinationSearchData {

        val cleanKeyword = keyword.trim()

        if (cleanKeyword.isBlank()) {
            throw IOException(
                "Please enter a destination"
            )
        }

        return unwrap(
            api.searchDestinations(
                keyword = cleanKeyword,
                region = region?.takeIf { it.isNotBlank() },
                budgetTier = budgetTier?.takeIf { it.isNotBlank() },
                season = season?.takeIf { it.isNotBlank() },
                tripType = tripType?.takeIf { it.isNotBlank() }
            )
        )
    }
    suspend fun getDestinationById(
        destinationId: String
    ): ApiDestination {

        if (destinationId.isBlank()) {
            throw IOException(
                "Destination ID is required"
            )
        }

        return unwrap(
            api.getDestinationById(
                destinationId = destinationId
            )
        )
    }

    suspend fun getActivities(
        destinationId: String? = null
    ): ActivitiesData {

        return unwrap(
            api.getActivities(
                destinationId = destinationId
            )
        )
    }


    suspend fun getActivityById(
        activityId: String
    ): ApiActivity {

        if (activityId.isBlank()) {
            throw IOException(
                "Activity ID is required"
            )
        }

        return unwrap(
            api.getActivityById(
                activityId = activityId
            )
        )
    }


    suspend fun searchExternalActivities(
        destinationId: String
    ): ExternalActivitiesData {

        if (destinationId.isBlank()) {
            throw IOException(
                "Destination ID is required"
            )
        }

        return unwrap(
            api.searchExternalActivities(
                destinationId = destinationId
            )
        )
    }

    suspend fun getRestaurants(
        destinationId: String? = null
    ): RestaurantsData {

        return unwrap(
            api.getRestaurants(
                destinationId = destinationId
            )
        )
    }

    suspend fun getHotels(
        search: String? = null,
        destinationId: String? = null
    ): HotelsData {

        return unwrap(
            api.getHotels(
                search = search,
                destinationId = destinationId
            )
        )
    }


    suspend fun getHotelById(
        hotelId: String
    ): ApiHotel {

        if (hotelId.isBlank()) {
            throw IOException(
                "Hotel ID is required"
            )
        }

        return unwrap(
            api.getHotelById(
                hotelId = hotelId
            )
        )
    }

    suspend fun searchFlights(
        request: FlightOfferSearchRequest
    ): FlightOfferSearchData {

        return unwrap(
            api.searchFlightOffers(request)
        )
    }
    suspend fun selectFlight(
        flightOfferId: String
    ): ApiFlightOffer {

        if (flightOfferId.isBlank()) {
            throw IOException(
                "Flight offer ID is required"
            )
        }

        return unwrap(
            api.selectFlightOffer(
                FlightOfferSelectRequest(
                    flightOfferId = flightOfferId
                )
            )
        )
    }


    suspend fun getFlightBookingDetails(
        flightOfferId: String
    ): ApiFlightOffer {

        if (flightOfferId.isBlank()) {
            throw IOException(
                "Flight offer ID is required"
            )
        }

        return unwrap(
            api.getFlight(
                flightOfferId = flightOfferId
            )
        )
    }


    suspend fun getFlightBookingUrl(
        flightOfferId: String
    ): FlightBookingUrlData {

        if (flightOfferId.isBlank()) {
            throw IOException(
                "Flight offer ID is required"
            )
        }

        return unwrap(
            api.getFlightBookingUrl(
                flightOfferId = flightOfferId
            )
        )
    }

    suspend fun createBooking(
        request: CreateBookingRequest
    ): ApiBooking {

        return unwrap(
            api.createBooking(request)
        )
    }


    suspend fun redirectBooking(
        bookingId: String
    ): BookingRedirectData {

        if (bookingId.isBlank()) {
            throw IOException(
                "Booking ID is required"
            )
        }

        return unwrap(
            api.redirectBooking(
                bookingId = bookingId
            )
        )
    }


    suspend fun getMyBookings(): BookingsListData {

        return unwrap(
            api.getMyBookings()
        )
    }


    suspend fun getBookingById(
        bookingId: String
    ): ApiBooking {

        if (bookingId.isBlank()) {
            throw IOException(
                "Booking ID is required"
            )
        }

        return unwrap(
            api.getBookingById(
                bookingId = bookingId
            )
        )
    }


    suspend fun searchBookings(
        keyword: String
    ): List<ApiBooking> {

        val cleanKeyword = keyword.trim()

        if (cleanKeyword.isBlank()) {
            throw IOException(
                "Search keyword is required"
            )
        }

        return unwrap(
            api.searchBookings(
                keyword = cleanKeyword
            )
        )
    }


    suspend fun filterBookings(
        filters: Map<String, String>
    ): List<ApiBooking> {

        return unwrap(
            api.filterBookings(
                filters = filters
            )
        )
    }

    suspend fun createTrip(
        request: CreateTripRequest
    ): ApiTrip {

        return unwrap(
            api.createTrip(request)
        )
    }


    suspend fun getTrips(): JsonElement {

        return unwrap(
            api.getTrips()
        )
    }


    suspend fun getTripById(
        tripId: String
    ): ApiTrip {

        if (tripId.isBlank()) {
            throw IOException(
                "Trip ID is required"
            )
        }

        return unwrap(
            api.getTripById(
                tripId = tripId
            )
        )
    }

    suspend fun searchExternalHotels(
        destinationId: String,
        checkIn: String,
        checkOut: String,
        adults: Int = 2,
        limit: Int = 10
    ): ExternalHotelsData {

        if (destinationId.isBlank()) {
            throw IOException(
                "Destination ID is required"
            )
        }

        if (checkIn.isBlank()) {
            throw IOException(
                "Check-in date is required"
            )
        }

        if (checkOut.isBlank()) {
            throw IOException(
                "Check-out date is required"
            )
        }

        return unwrap(
            api.searchExternalHotels(
                destinationId = destinationId,
                checkIn = checkIn,
                checkOut = checkOut,
                adults = adults,
                limit = limit
            )
        )
    }
    suspend fun loadDestinationUi(
        id: String
    ): Destination =
        unwrap(
            api.getDestinationById(id)
        ).toUiModel()

    suspend fun loadActivitiesUi(
        destinationId: String? = null
    ): List<Activity> {

        return unwrap(
            api.getActivities(
                destinationId = destinationId?.takeIf {
                    it.isNotBlank()
                }
            )
        ).activities.map {
            it.toUiModel()
        }
    }

    suspend fun loadHotelsUi(
        search: String? = null,
        destinationId: String? = null
    ): List<Hotel> {
        return getHotels(
            search = search,
            destinationId = destinationId
        ).hotels.map { it.toUiModel() }
    }

    suspend fun loadRestaurantsUi(
        destinationId: String? = null
    ): List<Restaurant> {
        return getRestaurants(
            destinationId = destinationId
        ).restaurants.map { it.toUiModel() }
    }

    suspend fun searchExternalRestaurants(
        destinationId: String,
        limit: Int = 20
    ): ExternalRestaurantsData {

        if (destinationId.isBlank()) {
            throw IOException(
                "Destination ID is required"
            )
        }

        return unwrap(
            api.searchExternalRestaurants(
                destinationId = destinationId,
                limit = limit
            )
        )
    }

}
