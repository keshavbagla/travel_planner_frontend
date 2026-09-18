package com.example.travel_planner.data

import com.google.gson.JsonElement
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("destinations")
    suspend fun getDestinations(
        @Query("destinationType") destinationType: String? = null,
        @Query("country") country: String? = null
    ): Response<ApiResponse<DestinationsData>>

    @GET("destinations/search")
    suspend fun searchDestinations(
        @Query("keyword") keyword: String
    ): Response<ApiResponse<DestinationSearchData>>

    @GET("destinations/{destinationId}")
    suspend fun getDestinationById(
        @Path("destinationId") destinationId: String
    ): Response<ApiResponse<ApiDestination>>

    @GET("activities")
    suspend fun getActivities(
        @Query("destination") destinationId: String? = null
    ): Response<ApiResponse<ActivitiesData>>

    @GET("activities/{activityId}")
    suspend fun getActivityById(
        @Path("activityId") activityId: String
    ): Response<ApiResponse<ApiActivity>>

    @GET("activities/search-external")
    suspend fun searchExternalActivities(
        @Query("destinationId") destinationId: String
    ): Response<ApiResponse<ExternalActivitiesData>>

    @GET("restaurants")
    suspend fun getRestaurants(
        @Query("destination") destinationId: String? = null
    ): Response<ApiResponse<RestaurantsData>>

    @GET("hotels")
    suspend fun getHotels(
        @Query("search") search: String? = null,
        @Query("destination") destinationId: String? = null
    ): Response<ApiResponse<HotelsData>>

    @GET("hotels/{hotelId}")
    suspend fun getHotelById(
        @Path("hotelId") hotelId: String
    ): Response<ApiResponse<ApiHotel>>

    @POST("flight/search")
    suspend fun searchFlightOffers(
        @Body request: FlightOfferSearchRequest
    ): Response<ApiResponse<FlightOfferSearchData>>

    @POST("flight/select")
    suspend fun selectFlightOffer(
        @Body request: FlightOfferSelectRequest
    ): Response<ApiResponse<ApiFlightOffer>>


    @GET("flight/{flightOfferId}/booking-details")
    suspend fun getFlight(
        @Path("flightOfferId") flightOfferId: String
    ): Response<ApiResponse<ApiFlightOffer>>


    @GET("flight/{flightOfferId}/booking-url")
    suspend fun getFlightBookingUrl(
        @Path("flightOfferId") flightOfferId: String
    ): Response<ApiResponse<FlightBookingUrlData>>

    @POST("bookings")
    suspend fun createBooking(
        @Body request: CreateBookingRequest
    ): Response<ApiResponse<ApiBooking>>

    @POST("bookings/{bookingId}/redirect")
    suspend fun redirectBooking(
        @Path("bookingId") bookingId: String
    ): Response<ApiResponse<BookingRedirectData>>

    @GET("bookings")
    suspend fun getMyBookings(): Response<ApiResponse<BookingsListData>>

    @GET("bookings/{bookingId}")
    suspend fun getBookingById(
        @Path("bookingId") bookingId: String
    ): Response<ApiResponse<ApiBooking>>

    @GET("bookings/search")
    suspend fun searchBookings(
        @Query("keyword") keyword: String
    ): Response<ApiResponse<List<ApiBooking>>>

    @GET("bookings/filter")
    suspend fun filterBookings(
        @QueryMap filters: Map<String, String>
    ): Response<ApiResponse<List<ApiBooking>>>


    @POST("trips")
    suspend fun createTrip(
        @Body request: CreateTripRequest
    ): Response<ApiResponse<ApiTrip>>

    @GET("trips")
    suspend fun getTrips(): Response<ApiResponse<JsonElement>>

    @GET("trips/{tripId}")
    suspend fun getTripById(
        @Path("tripId") tripId: String
    ): Response<ApiResponse<ApiTrip>>

    @GET("hotels/search-external")
    suspend fun searchExternalHotels(
        @Query("destinationId") destinationId: String,
        @Query("checkIn") checkIn: String,
        @Query("checkOut") checkOut: String,
        @Query("adults") adults: Int = 2,
        @Query("limit") limit: Int = 10
    ): Response<ApiResponse<ExternalHotelsData>>

    @GET("restaurants/search-external")
    suspend fun searchExternalRestaurants(
        @Query("destinationId") destinationId: String,
        @Query("limit") limit: Int = 20
    ): Response<ApiResponse<ExternalRestaurantsData>>
}


