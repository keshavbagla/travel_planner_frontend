package com.example.travel_planner.data

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("data") val data: T?,
    @SerializedName("message") val message: String,
    @SerializedName("success") val success: Boolean
)

data class Pagination(
    @SerializedName("page") val page: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("totalPages") val totalPages: Int
)
data class GeoPoint(
    @SerializedName("type") val type: String,
    @SerializedName("coordinates") val coordinates: List<Double>
) {
    val longitude: Double? get() = coordinates.getOrNull(0)
    val latitude: Double? get() = coordinates.getOrNull(1)
}


data class RecommendedDuration(
    @SerializedName("minDays") val minDays: Int? = null,
    @SerializedName("maxDays") val maxDays: Int? = null
)

data class Destination(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("city") val city: String? = null,
    @SerializedName("state") val state: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("slug") val slug: String? = null,
    @SerializedName("destinationType") val destinationType: List<String>? = null,
    @SerializedName("isFeatured") val isFeatured: Boolean? = null,
    @SerializedName("recommendedDuration") val recommendedDuration: RecommendedDuration? = null,
    @SerializedName("countryCode") val countryCode: String? = null,
    @SerializedName("placeType") val placeType: String? = null,
    @SerializedName("location") val location: GeoPoint? = null,
    @SerializedName("primaryAirportIata") val primaryAirportIata: String? = null, // only on details endpoint
    @SerializedName("currency") val currency: String? = null,
    @SerializedName("visaRequired") val visaRequired: Boolean? = null,
    @SerializedName("isActive") val isActive: Boolean? = null
)

data class DestinationsData(
    @SerializedName("destinations") val destinations: List<Destination>,
    @SerializedName("pagination") val pagination: Pagination
)

data class ActivitySchedule(
    @SerializedName("day") val day: String? = null,
    @SerializedName("startTime") val startTime: String? = null,
    @SerializedName("endTime") val endTime: String? = null
)

data class Activity(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String? = null,
    @SerializedName("destination") val destinationId: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("duration") val duration: Int? = null,
    @SerializedName("durationUnit") val durationUnit: String? = null, // e.g. "Hours"
    @SerializedName("price") val price: Double? = null,
    @SerializedName("currency") val currency: String? = null,
    @SerializedName("difficulty") val difficulty: String? = null,
    @SerializedName("minimumAge") val minimumAge: Int? = null,
    @SerializedName("maximumAge") val maximumAge: Int? = null,
    @SerializedName("minimumParticipants") val minimumParticipants: Int? = null,
    @SerializedName("maximumParticipants") val maximumParticipants: Int? = null,
    @SerializedName("meetingPoint") val meetingPoint: String? = null,
    @SerializedName("schedule") val schedule: List<ActivitySchedule>? = null, // details endpoint only
    @SerializedName("included") val included: List<String>? = null,          // details endpoint only
    @SerializedName("excluded") val excluded: List<String>? = null,          // details endpoint only
    @SerializedName("bookingUrl") val bookingUrl: String? = null,            // details endpoint only, often null
    @SerializedName("externalProvider") val externalProvider: String? = null,
    @SerializedName("isActive") val isActive: Boolean? = null
)

data class ActivitiesData(
    @SerializedName("activities") val activities: List<Activity>,
    @SerializedName("pagination") val pagination: Pagination
)

data class ExternalActivity(
    @SerializedName("name") val name: String,
    @SerializedName("category") val category: String? = null,
    @SerializedName("location") val location: GeoPoint? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("price") val price: Double? = null,
    @SerializedName("currency") val currency: String? = null,
    @SerializedName("source") val source: String? = null // e.g. "Geoapify"
)

data class ExternalActivitiesData(
    @SerializedName("activities") val activities: List<ExternalActivity>,
    @SerializedName("meta") val meta: JsonElement? = null // shape unspecified in doc ("{}" in example)
)


data class Restaurant(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("destination") val destinationId: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("cuisine") val cuisine: List<String>? = null,
    @SerializedName("restaurantType") val restaurantType: String? = null,
    @SerializedName("averageCostForTwo") val averageCostForTwo: Double? = null,
    @SerializedName("currency") val currency: String? = null,
    @SerializedName("averageRating") val averageRating: Double? = null,
    @SerializedName("reviewCount") val reviewCount: Int? = null,
    @SerializedName("tableReservation") val tableReservation: Boolean? = null,
    @SerializedName("takeawayAvailable") val takeawayAvailable: Boolean? = null,
    @SerializedName("deliveryAvailable") val deliveryAvailable: Boolean? = null,
    @SerializedName("isActive") val isActive: Boolean? = null
)

data class RestaurantsData(
    @SerializedName("restaurants") val restaurants: List<Restaurant>,
    @SerializedName("pagination") val pagination: Pagination
)

data class Hotel(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("destination") val destinationId: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("hotelType") val hotelType: String? = null,
    @SerializedName("externalProvider") val externalProvider: String? = null, // e.g. "booking"
    @SerializedName("externalHotelId") val externalHotelId: String? = null,
    @SerializedName("bookingUrl") val bookingUrl: String? = null,
    @SerializedName("starRating") val starRating: Int? = null,
    @SerializedName("averageRating") val averageRating: Double? = null,
    @SerializedName("reviewCount") val reviewCount: Int? = null,
    @SerializedName("pricePerNight") val pricePerNight: Double? = null,
    @SerializedName("currency") val currency: String? = null,
    @SerializedName("amenities") val amenities: List<String>? = null,
    @SerializedName("roomTypes") val roomTypes: List<JsonElement>? = null, // empty in every example seen so far
    @SerializedName("isActive") val isActive: Boolean? = null
)

data class HotelsData(
    @SerializedName("hotels") val hotels: List<Hotel>,
    @SerializedName("pagination") val pagination: Pagination
)

data class FlightOfferSearchRequest(
    @SerializedName("departureIata") val departureIata: String,
    @SerializedName("arrivalIata") val arrivalIata: String,
    @SerializedName("outboundDate") val outboundDate: String, // "yyyy-MM-dd"
    @SerializedName("adults") val adults: Int,
    @SerializedName("travelClass") val travelClass: String = "ECONOMY",
    @SerializedName("currency") val currency: String = "INR"
)

data class FlightLeg(
    @SerializedName("flightNumber") val flightNumber: String? = null,
    @SerializedName("airline") val airline: String? = null
)

data class FlightOffer(
    @SerializedName("_id") val id: String,
    @SerializedName("departureAirport") val departureAirport: String? = null,
    @SerializedName("arrivalAirport") val arrivalAirport: String? = null,
    @SerializedName("departureTime") val departureTime: String? = null, // ISO 8601
    @SerializedName("arrivalTime") val arrivalTime: String? = null,     // ISO 8601
    @SerializedName("durationMinutes") val durationMinutes: Int? = null,
    @SerializedName("durationText") val durationText: String? = null,
    @SerializedName("stops") val stops: Int? = null,
    @SerializedName("price") val price: Double? = null,
    @SerializedName("currency") val currency: String? = null,
    @SerializedName("flights") val flights: List<FlightLeg>? = null,
    @SerializedName("expiresAt") val expiresAt: String? = null, // check before using — offers are time-sensitive
    @SerializedName("isSelected") val isSelected: Boolean? = null,
    @SerializedName("isActive") val isActive: Boolean? = null,
    @SerializedName("provider") val provider: String? = null,   // present on the /select response
    @SerializedName("searchId") val searchId: String? = null    // present on the /select response
)

data class FlightOfferSearchData(
    @SerializedName("searchId") val searchId: String,
    @SerializedName("provider") val provider: String? = null,
    @SerializedName("offers") val offers: List<FlightOffer>,
    @SerializedName("totalOffers") val totalOffers: Int
)

data class FlightOfferSelectRequest(
    @SerializedName("flightOfferId") val flightOfferId: String
)

data class FlightBookingUrlData(
    @SerializedName("provider") val provider: String? = null,
    @SerializedName("timestamp") val timestamp: String? = null,
    @SerializedName("bookingUrl") val bookingUrl: String? = null
)

data class GuestDetails(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String? = ""
)

data class Travelers(
    @SerializedName("adults") val adults: Int,
    @SerializedName("children") val children: Int = 0,
    @SerializedName("infants") val infants: Int = 0
)

data class CreateBookingRequest(
    @SerializedName("trip") val tripId: String,
    @SerializedName("type") val type: String,
    @SerializedName("item") val itemId: String,
    @SerializedName("itemModel") val itemModel: String,
    @SerializedName("provider") val provider: String? = null,
    @SerializedName("externalItemId") val externalItemId: String? = null,
    @SerializedName("bookingUrl") val bookingUrl: String? = null,
    @SerializedName("bookingMode") val bookingMode: String = "ExternalRedirect",
    @SerializedName("amount") val amount: Double,
    @SerializedName("currency") val currency: String,
    @SerializedName("guestDetails") val guestDetails: GuestDetails,
    @SerializedName("travelers") val travelers: Travelers,
    @SerializedName("startDate") val startDate: String // "yyyy-MM-dd"
)
data class Booking(
    @SerializedName("_id") val id: String,
    @SerializedName("bookingReference") val bookingReference: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("itemModel") val itemModel: String? = null,
    @SerializedName("provider") val provider: String? = null,
    @SerializedName("externalItemId") val externalItemId: String? = null,
    @SerializedName("bookingUrl") val bookingUrl: String? = null,
    @SerializedName("bookingMode") val bookingMode: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("amount") val amount: Double? = null,
    @SerializedName("currency") val currency: String? = null,
    @SerializedName("guestDetails") val guestDetails: GuestDetails? = null,
    @SerializedName("travelers") val travelers: Travelers? = null,
    @SerializedName("totalTravelers") val totalTravelers: Int? = null,
    @SerializedName("redirectedAt") val redirectedAt: String? = null,
    @SerializedName("confirmedAt") val confirmedAt: String? = null,
    @SerializedName("cancelledAt") val cancelledAt: String? = null
)

data class BookingsListData(
    @SerializedName("bookings") val bookings: List<Booking>,
    @SerializedName("pagination") val pagination: Pagination
)

data class BookingRedirectData(
    @SerializedName("bookingId") val bookingId: String,
    @SerializedName("provider") val provider: String? = null,
    @SerializedName("bookingMode") val bookingMode: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("bookingUrl") val bookingUrl: String? = null,
    @SerializedName("redirectedAt") val redirectedAt: String? = null
)
