package com.example.travel_planner.data

import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("destinations")
    suspend fun getDestinations(
        @Query("search") search: String? = null
    ): Response<ApiResponse<DestinationsData>>

    @GET("destinations/{destinationId}")
    suspend fun getDestinationById(
        @Path("destinationId") destinationId: String
    ): Response<ApiResponse<ApiDestination>>

    @GET("activities")
    suspend fun getActivities(): Response<ApiResponse<ActivitiesData>>

    @GET("activities/{activityId}")
    suspend fun getActivityById(
        @Path("activityId") activityId: String
    ): Response<ApiResponse<ApiActivity>>

    @GET("activities/search-external")
    suspend fun searchExternalActivities(
        @Query("destinationId") destinationId: String
    ): Response<ApiResponse<ExternalActivitiesData>>

    @GET("restaurants")
    suspend fun getRestaurants(): Response<ApiResponse<RestaurantsData>>

    @GET("hotels")
    suspend fun getHotels(
        @Query("search") search: String? = null
    ): Response<ApiResponse<HotelsData>>

    @GET("hotels/{hotelId}")
    suspend fun getHotelById(
        @Path("hotelId") hotelId: String
    ): Response<ApiResponse<ApiHotel>>

    @POST("flight-offers/search")
    suspend fun searchFlightOffers(
        @Body request: FlightOfferSearchRequest
    ): Response<ApiResponse<FlightOfferSearchData>>

    @POST("flight-offers/select")
    suspend fun selectFlightOffer(
        @Body request: FlightOfferSelectRequest
    ): Response<ApiResponse<ApiFlightOffer>>

    @GET("flight-offers/{flightOfferId}/booking-url")
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
}