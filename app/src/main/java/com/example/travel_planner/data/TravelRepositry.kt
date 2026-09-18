package com.example.travel_planner.data

import com.example.travel_planner.model.Activity
import com.example.travel_planner.model.Destination
import com.example.travel_planner.model.Hotel
import com.example.travel_planner.model.Restaurant
import com.google.gson.Gson
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
            throw IOException(
                body.message.ifBlank {
                    "Request failed"
                }
            )
        }

        return body.data
            ?: throw IOException("No data in response")
    }

    suspend fun loadDestinationsUi(
        search: String? = null,
        destinationType: String? = null,
        country: String? = null
    ): List<Destination> {

        val trimmedSearch = search?.trim()

        val results: List<ApiDestination> =
            if (!trimmedSearch.isNullOrBlank()) {

                unwrap(
                    api.searchDestinations(
                        trimmedSearch
                    )
                ).results

            } else {

                unwrap(
                    api.getDestinations(
                        destinationType =
                            destinationType?.takeIf {
                                it.isNotBlank()
                            },
                        country =
                            country?.takeIf {
                                it.isNotBlank()
                            }
                    )
                ).destinations
            }

        val filtered =
            if (
                !trimmedSearch.isNullOrBlank() &&
                !destinationType.isNullOrBlank()
            ) {

                results.filter { destination ->

                    destination.destinationType?.any {
                        it.equals(
                            destinationType,
                            ignoreCase = true
                        )
                    } == true
                }

            } else {
                results
            }

        return filtered.map {
            it.toUiModel()
        }
    }

    suspend fun loadDestinationUi(
        id: String
    ): Destination =
        unwrap(
            api.getDestinationById(id)
        ).toUiModel()


    suspend fun loadHotelsUi(
        search: String? = null,
        destinationId: String? = null
    ): List<Hotel> {

        return unwrap(
            api.getHotels(
                search = search?.takeIf {
                    it.isNotBlank()
                },
                destinationId =
                    destinationId?.takeIf {
                        it.isNotBlank()
                    }
            )
        ).hotels.map {
            it.toUiModel()
        }
    }

    suspend fun loadHotelUi(
        id: String
    ): Hotel =
        unwrap(
            api.getHotelById(id)
        ).toUiModel()

    suspend fun loadRestaurantsUi(
        destinationId: String? = null
    ): List<Restaurant> {

        return unwrap(
            api.getRestaurants(
                destinationId =
                    destinationId?.takeIf {
                        it.isNotBlank()
                    }
            )
        ).restaurants.map {
            it.toUiModel()
        }
    }

    suspend fun loadActivitiesUi(
        destinationId: String? = null
    ): List<Activity> {

        return unwrap(
            api.getActivities(
                destinationId =
                    destinationId?.takeIf {
                        it.isNotBlank()
                    }
            )
        ).activities.map {
            it.toUiModel()
        }
    }

    suspend fun loadActivityUi(
        id: String
    ): Activity =
        unwrap(
            api.getActivityById(id)
        ).toUiModel()

    suspend fun searchExternalActivitiesUi(
        destinationId: String
    ): List<ApiExternalActivity> {

        if (destinationId.isBlank()) {
            throw IOException(
                "Destination ID is required"
            )
        }

        return unwrap(
            api.searchExternalActivities(
                destinationId
            )
        ).activities
    }

    suspend fun searchFlights(
        request: FlightOfferSearchRequest
    ): FlightOfferSearchData {

        return unwrap(
            api.searchFlightOffers(
                request
            )
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
                flightOfferId
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
                flightOfferId
            )
        )
    }

    suspend fun createBooking(
        request: CreateBookingRequest
    ): ApiBooking =
        unwrap(
            api.createBooking(request)
        )

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
                bookingId
            )
        )
    }

    suspend fun getMyBookings(): BookingsListData =
        unwrap(
            api.getMyBookings()
        )

    suspend fun getBooking(
        id: String
    ): ApiBooking =
        unwrap(
            api.getBookingById(id)
        )

    suspend fun searchBookings(
        keyword: String
    ): List<ApiBooking> {

        if (keyword.isBlank()) {
            return emptyList()
        }

        return unwrap(
            api.searchBookings(
                keyword.trim()
            )
        )
    }

    suspend fun filterBookings(
        status: String
    ): List<ApiBooking> {

        if (status.isBlank()) {
            return emptyList()
        }

        return unwrap(
            api.filterBookings(
                mapOf(
                    "status" to status
                )
            )
        )
    }

    suspend fun createTrip(
        tripName: String,
        destinationId: String,
        startDate: String,
        endDate: String
    ): ApiTrip {

        return unwrap(
            api.createTrip(
                CreateTripRequest(
                    tripName = tripName,
                    destinationId = destinationId,
                    startDate = startDate,
                    endDate = endDate
                )
            )
        )
    }

    suspend fun getMyTrips(): List<ApiTrip> {

        val json =
            unwrap(
                api.getTrips()
            )

        val gson = Gson()

        return when {

            json.isJsonArray -> {

                json.asJsonArray.map {
                    gson.fromJson(
                        it,
                        ApiTrip::class.java
                    )
                }
            }

            json.isJsonObject &&
                    json.asJsonObject.has("trips") -> {

                json.asJsonObject
                    .getAsJsonArray("trips")
                    .map {
                        gson.fromJson(
                            it,
                            ApiTrip::class.java
                        )
                    }
            }

            else -> {
                emptyList()
            }
        }
    }

    suspend fun getTrip(
        id: String
    ): ApiTrip =
        unwrap(
            api.getTripById(id)
        )
}