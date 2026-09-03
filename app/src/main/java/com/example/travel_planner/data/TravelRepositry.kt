package com.example.travel_planner.data

import com.example.travel_planner.model.Activity as UiActivity
import com.example.travel_planner.model.Destination as UiDestination
import com.example.travel_planner.model.Hotel as UiHotel
import com.example.travel_planner.model.Restaurant as UiRestaurant
import retrofit2.Response
import java.io.IOException

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
            throw IOException(body.message)
        }

        return body.data
            ?: throw IOException("No data in response")
    }

    suspend fun loadDestinationsUi(): List<UiDestination> {
        val data = this.unwrap(response = api.getDestinations())

        return data.destinations.map {
            it.toUiModel()
        }
    }


    suspend fun loadDestinationUi(
        id: String
    ): UiDestination {

        val data = unwrap(
            api.getDestinationById(id)
        )

        return data.toUiModel()
    }

    suspend fun loadHotelsUi(): List<UiHotel> {
        val data = unwrap(api.getHotels())

        return data.hotels.map {
            it.toUiModel()
        }
    }


    suspend fun loadHotelUi(
        id: String
    ): UiHotel {

        val data = unwrap(
            api.getHotelById(id)
        )

        return data.toUiModel()
    }

    suspend fun loadRestaurantsUi(): List<UiRestaurant> {
        val data = unwrap(api.getRestaurants())

        return data.restaurants.map {
            it.toUiModel()
        }
    }

    suspend fun loadActivitiesUi(): List<UiActivity> {
        val data = unwrap(api.getActivities())

        return data.activities.map {
            it.toUiModel()
        }
    }


    suspend fun loadActivityUi(
        id: String
    ): UiActivity {

        val data = unwrap(
            api.getActivityById(id)
        )

        return data.toUiModel()
    }

    suspend fun searchExternalActivitiesUi(
        destinationId: String
    ): List<ExternalActivity> {

        val data = unwrap(
            api.searchExternalActivities(destinationId)
        )

        return data.activities
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
    ): FlightOffer {

        return unwrap(
            api.selectFlightOffer(
                FlightOfferSelectRequest(
                    flightOfferId = flightOfferId
                )
            )
        )
    }


    suspend fun getFlightBookingUrl(
        flightOfferId: String
    ): FlightBookingUrlData {

        return unwrap(
            api.getFlightBookingUrl(flightOfferId)
        )
    }

    suspend fun createBooking(
        request: CreateBookingRequest
    ): Booking {

        return unwrap(
            api.createBooking(request)
        )
    }


    suspend fun redirectBooking(
        bookingId: String
    ): BookingRedirectData {

        return unwrap(
            api.redirectBooking(bookingId)
        )
    }


    suspend fun getMyBookings(): BookingsListData {

        return unwrap(
            api.getMyBookings()
        )
    }


    suspend fun getBooking(
        id: String
    ): Booking {

        return unwrap(
            api.getBookingById(id)
        )
    }


    suspend fun searchBookings(
        keyword: String
    ): List<Booking> {

        return unwrap(
            api.searchBookings(keyword)
        )
    }


    suspend fun filterBookings(
        status: String
    ): List<Booking> {

        return unwrap(
            api.filterBookings(
                mapOf("status" to status)
            )
        )
    }
}