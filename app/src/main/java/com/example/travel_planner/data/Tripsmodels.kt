package com.example.travel_planner.dat

import com.example.travel_planner.data.Travelers
import com.google.gson.annotations.SerializedName

data class CreateTripRequest(
    @SerializedName("tripName") val tripName: String,
    @SerializedName("destination") val destinationId: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("travelers") val travelers: Travelers? = null,
    @SerializedName("budget") val budget: Map<String, String>? = null,
    @SerializedName("preferences") val preferences: Map<String, String>? = null,
    @SerializedName("restaurants") val restaurants: List<String> = emptyList(),
    @SerializedName("activities") val activities: List<String> = emptyList(),
    @SerializedName("itinerary") val itinerary: List<String> = emptyList()
)

data class ApiTrip(
    @SerializedName("_id") val id: String,
    @SerializedName("tripName") val tripName: String? = null,
    @SerializedName("destination") val destinationId: String? = null,
    @SerializedName("hotel") val hotelId: String? = null,
    @SerializedName("startDate") val startDate: String? = null,
    @SerializedName("endDate") val endDate: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("travelers") val travelers: Travelers? = null,
    @SerializedName("isAIGenerated") val isAIGenerated: Boolean? = null,
    @SerializedName("isPublic") val isPublic: Boolean? = null
)

data class TripsListData(
    @SerializedName("trips") val trips: List<ApiTrip>? = null
)