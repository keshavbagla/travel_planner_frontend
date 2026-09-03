package com.example.travel_planner.data

import com.example.travel_planner.model.Activity as UiActivity
import com.example.travel_planner.model.Destination as UiDestination
import com.example.travel_planner.model.Flight as UiFlight
import com.example.travel_planner.model.Hotel as UiHotel
import com.example.travel_planner.model.Restaurant as UiRestaurant
import kotlin.math.roundToInt

private fun String.titleCaseWords(): String =
    replace("_", " ")
        .split(" ")
        .joinToString(" ") { word ->
            word.replaceFirstChar { it.uppercase() }
        }


fun Destination.toUiModel(): UiDestination {

    val seed = slug ?: id

    return UiDestination(
        id = id,
        name = name,
        country = country ?: "",
        rating = 4.5,
        priceTier = if (isFeatured == true) "$$$" else "$$",
        imageUrl = "https://picsum.photos/seed/$seed/800/600",
        description = listOfNotNull(
            city,
            state,
            country
        )
            .distinct()
            .joinToString(", ")
    )
}


fun Hotel.toUiModel(): UiHotel {

    val avg = averageRating ?: 0.0

    val label = when {
        avg >= 9.0 -> "Wonderful"
        avg >= 8.0 -> "Excellent"
        avg >= 7.0 -> "Very Good"
        avg >= 6.0 -> "Good"
        avg > 0.0 -> "Fair"
        else -> "Not yet rated"
    }

    return UiHotel(
        id = id,
        name = name,

        area = listOfNotNull(
            city,
            country
        ).joinToString(", "),

        rating = if (avg > 0.0) {
            "$avg $label"
        } else {
            label
        },

        stars = starRating ?: 0,

        price = (pricePerNight ?: 0.0).roundToInt(),

        currency = currency ?: "USD",

        amenities = amenities
            ?.takeIf { it.isNotEmpty() }
            ?.joinToString(" • ") { amenity ->
                amenity.titleCaseWords()
            }
            ?: "Amenities not listed"
    )
}


fun Restaurant.toUiModel(): UiRestaurant {

    val cost = averageCostForTwo ?: 0.0

    val tier = when {
        cost <= 0 -> "$"
        cost < 500 -> "$"
        cost < 1500 -> "$$"
        cost < 3000 -> "$$$"
        else -> "$$$$"
    }

    return UiRestaurant(
        id = id,
        name = name,

        cuisine = cuisine
            ?.takeIf { it.isNotEmpty() }
            ?.joinToString(" • ") { item ->
                item.titleCaseWords()
            }
            ?: (restaurantType ?: "Restaurant"),

        rating = averageRating ?: 0.0,

        priceTier = tier
    )
}


// =========================================================
// ACTIVITY
// API Activity -> UI Activity
// =========================================================

fun Activity.toUiModel(): UiActivity {

    val durationLabel =
        if (duration != null && durationUnit != null) {
            "$duration ${durationUnit.lowercase()}"
        } else {
            "Duration varies"
        }

    return UiActivity(
        id = id,
        name = name,
        duration = durationLabel,
        price = (price ?: 0.0).roundToInt()
    )
}

private fun extractHHmm(isoTimestamp: String?): String {

    if (isoTimestamp == null) {
        return "--:--"
    }

    val tIndex = isoTimestamp.indexOf('T')

    if (tIndex == -1 || isoTimestamp.length < tIndex + 6) {
        return "--:--"
    }

    return isoTimestamp.substring(
        tIndex + 1,
        tIndex + 6
    )
}

fun FlightOffer.toUiModel(): UiFlight {

    val airlineName =
        flights
            ?.firstOrNull()
            ?.airline
            ?: provider
            ?: "Unknown airline"

    val stopsLabel = when (stops) {
        null -> ""
        0 -> "Non-stop"
        1 -> "1 Stop"
        else -> "$stops Stops"
    }

    return UiFlight(
        id = id,
        airline = airlineName,
        price = (price ?: 0.0).roundToInt(),
        departTime = extractHHmm(departureTime),
        departAirport = departureAirport ?: "",
        arriveTime = extractHHmm(arrivalTime),
        arriveAirport = arrivalAirport ?: "",
        duration = durationText ?: "",
        stopsLabel = stopsLabel
    )
}