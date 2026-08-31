package com.example.travel_planner.data

import com.example.travel_planner.model.Attraction
import com.example.travel_planner.model.ChatMessage
import com.example.travel_planner.model.Destination
import com.example.travel_planner.model.Flight
import com.example.travel_planner.model.ItineraryDay
import com.example.travel_planner.model.TripPreference

object SampleData {

    val popularDestinations = listOf(
        Destination(
            id = "kyoto",
            name = "Kyoto",
            country = "Japan",
            rating = 4.9,
            priceTier = "$$$",
            imageUrl = "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e",
            description = "A beautiful city known for traditional temples, gardens and Japanese culture."
        ),
        Destination(
            id = "santorini",
            name = "Santorini",
            country = "Greece",
            rating = 4.8,
            priceTier = "$$$",
            imageUrl = "https://images.unsplash.com/photo-1570077188670-e3a8d69ac5ff",
            description = "A stunning Greek island famous for white villages, blue domes and beautiful sunsets."
        ),
        Destination(
            id = "amalfi",
            name = "Amalfi Coast",
            country = "Italy",
            rating = 4.9,
            priceTier = "$$$",
            imageUrl = "https://images.unsplash.com/photo-1533105079780-92b9be482077",
            description = "A spectacular Italian coastline with colorful villages, beaches and Mediterranean views."
        ),
        Destination(
            id = "cape-town",
            name = "Cape Town",
            country = "South Africa",
            rating = 4.8,
            priceTier = "$$",
            imageUrl = "https://images.unsplash.com/photo-1580060839134-75a5edca2e99",
            description = "A vibrant destination combining mountains, beaches, wildlife and city experiences."
        ),
        Destination(
            id = "reykjavik",
            name = "Reykjavik",
            country = "Iceland",
            rating = 4.7,
            priceTier = "$$$",
            imageUrl = "https://images.unsplash.com/photo-1504829857797-ddff29c27927",
            description = "The gateway to Iceland's glaciers, waterfalls, hot springs and Northern Lights."
        )
    )

    val exploreDestinations = listOf(
        Destination(
            id = "kyoto",
            name = "Kyoto",
            country = "Japan",
            rating = 4.9,
            priceTier = "$$$",
            imageUrl = "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e",
            description = "Explore ancient temples, peaceful gardens and traditional Japanese streets."
        ),
        Destination(
            id = "cape-town",
            name = "Cape Town",
            country = "South Africa",
            rating = 4.8,
            priceTier = "$$",
            imageUrl = "https://images.unsplash.com/photo-1580060839134-75a5edca2e99",
            description = "Enjoy Table Mountain, beautiful beaches and unforgettable African landscapes."
        ),
        Destination(
            id = "reykjavik",
            name = "Reykjavik",
            country = "Iceland",
            rating = 4.7,
            priceTier = "$$$",
            imageUrl = "https://images.unsplash.com/photo-1504829857797-ddff29c27927",
            description = "Discover Icelandic nature, waterfalls, glaciers and the Northern Lights."
        ),
        Destination(
            id = "santorini",
            name = "Santorini",
            country = "Greece",
            rating = 4.8,
            priceTier = "$$$",
            imageUrl = "https://images.unsplash.com/photo-1570077188670-e3a8d69ac5ff",
            description = "Relax beside the Aegean Sea and experience iconic Greek island views."
        )
    )

    val searchChips = listOf(
        "Beach",
        "Mountains",
        "City",
        "Adventure",
        "Cultural"
    )

    val kyotoAttractions = listOf(
        Attraction(
            id = "fushimi-inari",
            name = "Fushimi Inari Taisha",
            rating = 4.9,
            imageUrl = "https://images.unsplash.com/photo-1478436127897-769e1b3f0f36"
        ),
        Attraction(
            id = "kinkakuji",
            name = "Kinkaku-ji Temple",
            rating = 4.8,
            imageUrl = "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e"
        ),
        Attraction(
            id = "arashiyama",
            name = "Arashiyama Bamboo Grove",
            rating = 4.8,
            imageUrl = "https://images.unsplash.com/photo-1528360983277-13d401cdc186"
        ),
        Attraction(
            id = "kiyomizu",
            name = "Kiyomizu-dera Temple",
            rating = 4.9,
            imageUrl = "https://images.unsplash.com/photo-1545569341-9eb8b30979d9"
        )
    )

    val kyotoItinerary = listOf(
        ItineraryDay(
            label = "Day 1",
            summary = "Explore Fushimi Inari Shrine and traditional Kyoto neighborhoods."
        ),
        ItineraryDay(
            label = "Day 2",
            summary = "Visit Kinkaku-ji, Arashiyama Bamboo Grove and nearby temples."
        ),
        ItineraryDay(
            label = "Day 3",
            summary = "Discover Kiyomizu-dera and enjoy a relaxed evening in Gion."
        )
    )

    val flights = listOf(
        Flight(
            id = "flight-1",
            airline = "Japan Airlines",
            price = 52000,
            departTime = "10:30 AM",
            departAirport = "DEL",
            arriveTime = "08:00 AM",
            arriveAirport = "KIX",
            duration = "10h 30m",
            stopsLabel = "1 Stop"
        ),
        Flight(
            id = "flight-2",
            airline = "ANA",
            price = 56000,
            departTime = "02:15 PM",
            departAirport = "DEL",
            arriveTime = "12:30 PM",
            arriveAirport = "KIX",
            duration = "10h 45m",
            stopsLabel = "1 Stop"
        ),
        Flight(
            id = "flight-3",
            airline = "Air India",
            price = 61000,
            departTime = "11:45 PM",
            departAirport = "DEL",
            arriveTime = "08:30 AM",
            arriveAirport = "KIX",
            duration = "8h 45m",
            stopsLabel = "Non-stop"
        )
    )

    val tripPreferences = listOf(
        TripPreference("Beach"),
        TripPreference("Mountains"),
        TripPreference("Adventure"),
        TripPreference("Culture"),
        TripPreference("Food"),
        TripPreference("Shopping"),
        TripPreference("Nightlife"),
        TripPreference("Relaxation")
    )

    val chatMessages = listOf(
        ChatMessage(
            id = "1",
            text = "Hi! Where would you like to travel?",
            fromUser = false
        ),
        ChatMessage(
            id = "2",
            text = "I want to plan a trip to Kyoto.",
            fromUser = true
        ),
        ChatMessage(
            id = "3",
            text = "Great choice! Kyoto is perfect for culture, food and beautiful temples.",
            fromUser = false
        )
    )
}