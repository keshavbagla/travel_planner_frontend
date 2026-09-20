package com.example.travel_planner.model

import com.google.firebase.inappmessaging.model.ImageData


data class Destination(
    val id: String = "",
    val name: String = "",
    val slug: String? = null,

    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val region: String? = null,

    val description: String? = null,

    val destinationType: List<String> = emptyList(),
    val travelStyles: List<String> = emptyList(),
    val suitableFor: List<String> = emptyList(),

    val budgetTier: String? = null,
    val seasons: List<String> = emptyList(),

    val placesToVisit: List<String> = emptyList(),
    val activities: List<String> = emptyList(),
    val popularActivities: List<String> = emptyList(),

    val beaches: List<String> = emptyList(),
    val shopping: List<String> = emptyList(),
    val nightlife: List<String> = emptyList(),

    val hotels: List<String> = emptyList(),
    val restaurants: List<String> = emptyList(),

    val famousFor: List<String> = emptyList(),

    val recommendedDurationText: String? = null,
    val primaryAirportIata: String? = null,

    val coverImage: ImageData? = null,
    val rating: Double,
    val priceTier: String,
    val imageUrl: String
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

data class Hotel(
    val id: String,
    val name: String,
    val area: String,
    val rating: String,
    val stars: Int,
    val price: Int,
    val currency: String,
    val amenities: String
)

data class Restaurant(
    val id: String,
    val name: String,
    val cuisine: String,
    val rating: Double,
    val priceTier: String
)

data class Activity(
    val id: String,
    val name: String,
    val duration: String,
    val price: Int
)