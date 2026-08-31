package com.example.travel_planner.model

data class Destination(
    val id: String,
    val name: String,
    val country: String,
    val rating: Double,
    val priceTier: String,
    val imageUrl: String?,
    val description: String = ""
)

data class Attraction(
    val id: String,
    val name: String,
    val rating: Double,
    val imageUrl: String? = null
)

data class ItineraryDay(
    val label: String,
    val summary: String
)

data class Flight(
    val id: String,
    val airline: String,
    val price: Int,
    val departTime: String,
    val departAirport: String,
    val arriveTime: String,
    val arriveAirport: String,
    val duration: String,
    val stopsLabel: String
)

data class ChatMessage(
    val id: String,
    val text: String,
    val fromUser: Boolean
)

data class TripPreference(
    val label: String,
    val selected: Boolean = false
)