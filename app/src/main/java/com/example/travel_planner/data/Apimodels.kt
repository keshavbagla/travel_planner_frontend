package com.example.travel_planner.data

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName


data class ApiResponse<T>(
    @SerializedName("statusCode")
    val statusCode: Int,

    @SerializedName("data")
    val data: T?,

    @SerializedName("message")
    val message: String,

    @SerializedName("success")
    val success: Boolean
)

data class Pagination(
    @SerializedName("page")
    val page: Int,

    @SerializedName("limit")
    val limit: Int,

    @SerializedName("total")
    val total: Int,

    @SerializedName("totalPages")
    val totalPages: Int
)

data class GeoPoint(
    @SerializedName("type")
    val type: String,

    @SerializedName("coordinates")
    val coordinates: List<Double>
) {
    val longitude: Double?
        get() = coordinates.getOrNull(0)

    val latitude: Double?
        get() = coordinates.getOrNull(1)
}

data class RecommendedDuration(
    @SerializedName("minDays")
    val minDays: Int? = null,

    @SerializedName("maxDays")
    val maxDays: Int? = null
)

data class NearbyAirport(
    @SerializedName("airportName")
    val airportName: String? = null,

    @SerializedName("airportCode")
    val airportCode: String? = null,

    @SerializedName("city")
    val city: String? = null,

    @SerializedName("distance")
    val distanceKm: Double? = null
)

data class CoverImage(
    @SerializedName("url")
    val url: String? = null,

    @SerializedName("caption")
    val caption: String? = null
)

data class ApiDestination(
    @SerializedName("id")
    val id: String = "",

    // Geoapify ID
    @SerializedName("geoapifyPlaceId")
    val geoapifyPlaceId: String? = null,

    @SerializedName("name")
    val name: String = "",

    @SerializedName("city")
    val city: String? = null,

    @SerializedName("state")
    val state: String? = null,

    @SerializedName("country")
    val country: String? = null,

    @SerializedName("slug")
    val slug: String? = null,

    @SerializedName("destinationType")
    val destinationType: List<String>? = null,

    @SerializedName("isFeatured")
    val isFeatured: Boolean? = null,

    @SerializedName("recommendedDuration")
    val recommendedDuration: RecommendedDuration? = null,

    @SerializedName("countryCode")
    val countryCode: String? = null,

    @SerializedName("placeType")
    val placeType: String? = null,

    @SerializedName("location")
    val location: GeoPoint? = null,

    @SerializedName("primaryAirportIata")
    val primaryAirportIata: String? = null,

    @SerializedName("nearbyAirports")
    val nearbyAirports: List<NearbyAirport>? = null,

    @SerializedName("currency")
    val currency: String? = null,

    @SerializedName("visaRequired")
    val visaRequired: Boolean? = null,

    @SerializedName("averageRating")
    val averageRating: Double? = null,

    @SerializedName("popularityScore")
    val popularityScore: Double? = null,

    @SerializedName("coverImage")
    val coverImage: CoverImage? = null,

    @SerializedName("famousFor")
    val famousFor: List<String>? = null,

    @SerializedName("isActive")
    val isActive: Boolean? = null
) {
}

data class DestinationsData(
    @SerializedName("destinations")
    val destinations: List<ApiDestination> = emptyList(),

    @SerializedName("pagination")
    val pagination: Pagination? = null
)

data class ActivitySchedule(
    @SerializedName("day")
    val day: String? = null,

    @SerializedName("startTime")
    val startTime: String? = null,

    @SerializedName("endTime")
    val endTime: String? = null
)

data class ApiActivity(
    @SerializedName("_id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("slug")
    val slug: String? = null,

    @SerializedName("destination")
    val destinationId: String? = null,

    @SerializedName("city")
    val city: String? = null,

    @SerializedName("country")
    val country: String? = null,

    @SerializedName("category")
    val category: String? = null,

    @SerializedName("duration")
    val duration: Int? = null,

    @SerializedName("durationUnit")
    val durationUnit: String? = null,

    @SerializedName("price")
    val price: Double? = null,

    @SerializedName("currency")
    val currency: String? = null,

    @SerializedName("difficulty")
    val difficulty: String? = null,

    @SerializedName("minimumAge")
    val minimumAge: Int? = null,

    @SerializedName("maximumAge")
    val maximumAge: Int? = null,

    @SerializedName("minimumParticipants")
    val minimumParticipants: Int? = null,

    @SerializedName("maximumParticipants")
    val maximumParticipants: Int? = null,

    @SerializedName("meetingPoint")
    val meetingPoint: String? = null,

    @SerializedName("schedule")
    val schedule: List<ActivitySchedule>? = null,

    @SerializedName("included")
    val included: List<String>? = null,

    @SerializedName("excluded")
    val excluded: List<String>? = null,

    @SerializedName("bookingUrl")
    val bookingUrl: String? = null,

    @SerializedName("externalProvider")
    val externalProvider: String? = null,

    @SerializedName("isActive")
    val isActive: Boolean? = null
)

data class ActivitiesData(
    @SerializedName("activities")
    val activities: List<ApiActivity>,

    @SerializedName("pagination")
    val pagination: Pagination
)

data class ApiExternalActivity(
    @SerializedName("name")
    val name: String,

    @SerializedName("category")
    val category: String? = null,

    @SerializedName("location")
    val location: GeoPoint? = null,

    @SerializedName("city")
    val city: String? = null,

    @SerializedName("country")
    val country: String? = null,

    @SerializedName("price")
    val price: Double? = null,

    @SerializedName("currency")
    val currency: String? = null,

    @SerializedName("source")
    val source: String? = null
)

data class ExternalActivitiesData(
    @SerializedName("activities")
    val activities: List<ApiExternalActivity>,

    @SerializedName("meta")
    val meta: JsonElement? = null
)

data class ApiRestaurant(
    @SerializedName("_id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("destination")
    val destinationId: String? = null,

    @SerializedName("city")
    val city: String? = null,

    @SerializedName("country")
    val country: String? = null,

    @SerializedName("cuisine")
    val cuisine: List<String>? = null,

    @SerializedName("restaurantType")
    val restaurantType: String? = null,

    @SerializedName("averageCostForTwo")
    val averageCostForTwo: Double? = null,

    @SerializedName("currency")
    val currency: String? = null,

    @SerializedName("averageRating")
    val averageRating: Double? = null,

    @SerializedName("reviewCount")
    val reviewCount: Int? = null,

    @SerializedName("tableReservation")
    val tableReservation: Boolean? = null,

    @SerializedName("takeawayAvailable")
    val takeawayAvailable: Boolean? = null,

    @SerializedName("deliveryAvailable")
    val deliveryAvailable: Boolean? = null,

    @SerializedName("isActive")
    val isActive: Boolean? = null
)

data class RestaurantsData(
    @SerializedName("restaurants")
    val restaurants: List<ApiRestaurant>,

    @SerializedName("pagination")
    val pagination: Pagination
)

data class ApiHotel(
    @SerializedName("_id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("destination")
    val destinationId: String? = null,

    @SerializedName("city")
    val city: String? = null,

    @SerializedName("country")
    val country: String? = null,

    @SerializedName("hotelType")
    val hotelType: String? = null,

    @SerializedName("externalProvider")
    val externalProvider: String? = null,

    @SerializedName("externalHotelId")
    val externalHotelId: String? = null,

    @SerializedName("bookingUrl")
    val bookingUrl: String? = null,

    @SerializedName("starRating")
    val starRating: Int? = null,

    @SerializedName("averageRating")
    val averageRating: Double? = null,

    @SerializedName("reviewCount")
    val reviewCount: Int? = null,

    @SerializedName("pricePerNight")
    val pricePerNight: Double? = null,

    @SerializedName("currency")
    val currency: String? = null,

    @SerializedName("amenities")
    val amenities: List<String>? = null,

    @SerializedName("roomTypes")
    val roomTypes: List<JsonElement>? = null,

    @SerializedName("isActive")
    val isActive: Boolean? = null
)

data class HotelsData(
    @SerializedName("hotels")
    val hotels: List<ApiHotel>,

    @SerializedName("pagination")
    val pagination: Pagination
)

data class FlightOfferSearchRequest(
    @SerializedName("departureIata")
    val departureIata: String,

    @SerializedName("arrivalIata")
    val arrivalIata: String,

    @SerializedName("outboundDate")
    val outboundDate: String,

    @SerializedName("adults")
    val adults: Int,

    @SerializedName("travelClass")
    val travelClass: String = "ECONOMY",

    @SerializedName("currency")
    val currency: String = "INR"
)


data class FlightLeg(
    @SerializedName("flightNumber")
    val flightNumber: String? = null,

    @SerializedName("airline")
    val airline: String? = null
)

data class ApiFlightOffer(
    @SerializedName("_id")
    val id: String,

    @SerializedName("departureAirport")
    val departureAirport: String? = null,

    @SerializedName("arrivalAirport")
    val arrivalAirport: String? = null,

    @SerializedName("departureTime")
    val departureTime: String? = null,

    @SerializedName("arrivalTime")
    val arrivalTime: String? = null,

    @SerializedName("durationMinutes")
    val durationMinutes: Int? = null,

    @SerializedName("durationText")
    val durationText: String? = null,

    @SerializedName("stops")
    val stops: Int? = null,

    @SerializedName("price")
    val price: Double? = null,

    @SerializedName("currency")
    val currency: String? = null,

    @SerializedName("flights")
    val flights: List<FlightLeg>? = null,

    @SerializedName("expiresAt")
    val expiresAt: String? = null,

    @SerializedName("isSelected")
    val isSelected: Boolean? = null,

    @SerializedName("isActive")
    val isActive: Boolean? = null,

    @SerializedName("provider")
    val provider: String? = null,

    @SerializedName("searchId")
    val searchId: String? = null
)

data class FlightOfferSearchData(
    @SerializedName("searchId")
    val searchId: String,

    @SerializedName("provider")
    val provider: String? = null,

    @SerializedName("offers")
    val offers: List<ApiFlightOffer>,

    @SerializedName("totalOffers")
    val totalOffers: Int
)

data class FlightOfferSelectRequest(
    @SerializedName("flightOfferId")
    val flightOfferId: String
)

data class FlightBookingUrlData(
    @SerializedName("provider")
    val provider: String? = null,

    @SerializedName("timestamp")
    val timestamp: String? = null,

    @SerializedName("bookingUrl")
    val bookingUrl: String? = null
)

data class GuestDetails(
    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("phone")
    val phone: String? = ""
)

data class Travelers(
    @SerializedName("adults")
    val adults: Int,

    @SerializedName("children")
    val children: Int = 0,

    @SerializedName("infants")
    val infants: Int = 0
)

data class CreateBookingRequest(
    @SerializedName("trip")
    val tripId: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("item")
    val itemId: String,

    @SerializedName("itemModel")
    val itemModel: String,

    @SerializedName("provider")
    val provider: String? = null,

    @SerializedName("externalItemId")
    val externalItemId: String? = null,

    @SerializedName("bookingUrl")
    val bookingUrl: String? = null,

    @SerializedName("bookingMode")
    val bookingMode: String = "ExternalRedirect",

    @SerializedName("amount")
    val amount: Double,

    @SerializedName("currency")
    val currency: String,

    @SerializedName("guestDetails")
    val guestDetails: GuestDetails,

    @SerializedName("travelers")
    val travelers: Travelers,

    @SerializedName("startDate")
    val startDate: String
)

data class ApiBooking(
    @SerializedName("_id")
    val id: String,

    @SerializedName("bookingReference")
    val bookingReference: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("itemModel")
    val itemModel: String? = null,

    @SerializedName("provider")
    val provider: String? = null,

    @SerializedName("externalItemId")
    val externalItemId: String? = null,

    @SerializedName("bookingUrl")
    val bookingUrl: String? = null,

    @SerializedName("bookingMode")
    val bookingMode: String? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("amount")
    val amount: Double? = null,

    @SerializedName("currency")
    val currency: String? = null,

    @SerializedName("guestDetails")
    val guestDetails: GuestDetails? = null,

    @SerializedName("travelers")
    val travelers: Travelers? = null,

    @SerializedName("totalTravelers")
    val totalTravelers: Int? = null,

    @SerializedName("redirectedAt")
    val redirectedAt: String? = null,

    @SerializedName("confirmedAt")
    val confirmedAt: String? = null,

    @SerializedName("cancelledAt")
    val cancelledAt: String? = null
)

data class BookingsListData(
    @SerializedName("bookings")
    val bookings: List<ApiBooking>,

    @SerializedName("pagination")
    val pagination: Pagination
)

data class BookingRedirectData(
    @SerializedName("bookingId")
    val bookingId: String,

    @SerializedName("provider")
    val provider: String? = null,

    @SerializedName("bookingMode")
    val bookingMode: String? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("bookingUrl")
    val bookingUrl: String? = null,

    @SerializedName("redirectedAt")
    val redirectedAt: String? = null
)

data class CreateTripRequest(
    @SerializedName("tripName")
    val tripName: String,

    @SerializedName("destination")
    val destinationId: String,

    @SerializedName("startDate")
    val startDate: String,

    @SerializedName("endDate")
    val endDate: String,

    @SerializedName("travelers")
    val travelers: Travelers? = null,

    @SerializedName("budget")
    val budget: Map<String, String>? = null,

    @SerializedName("preferences")
    val preferences: Map<String, String>? = null,

    @SerializedName("restaurants")
    val restaurants: List<String> = emptyList(),

    @SerializedName("activities")
    val activities: List<String> = emptyList(),

    @SerializedName("itinerary")
    val itinerary: List<String> = emptyList()
)
data class ApiTrip(
    @SerializedName("_id")
    val id: String,

    @SerializedName("tripName")
    val tripName: String? = null,

    @SerializedName("destination")
    val destinationId: String? = null,

    @SerializedName("hotel")
    val hotelId: String? = null,

    @SerializedName("startDate")
    val startDate: String? = null,

    @SerializedName("endDate")
    val endDate: String? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("travelers")
    val travelers: Travelers? = null,

    @SerializedName("isAIGenerated")
    val isAIGenerated: Boolean? = null,

    @SerializedName("isPublic")
    val isPublic: Boolean? = null
)

data class TripsListData(
    @SerializedName("trips")
    val trips: List<ApiTrip>? = null
)

data class DestinationSearchData(
    @SerializedName("source")
    val source: String? = null,

    @SerializedName("results")
    val results: List<ApiDestination> = emptyList()
)

data class ExternalHotelsData(
    @SerializedName("hotels")
    val hotels: List<ApiHotel> = emptyList(),

    @SerializedName("meta")
    val meta: JsonElement? = null
)

data class ExternalRestaurantsData(
    @SerializedName("restaurants")
    val restaurants: List<ApiRestaurant> = emptyList(),

    @SerializedName("meta")
    val meta: JsonElement? = null
)

