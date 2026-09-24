package com.example.travel_planner.data

import com.example.travel_planner.model.Activity
import com.example.travel_planner.model.Destination
import com.example.travel_planner.model.Flight
import com.example.travel_planner.model.Hotel
import com.example.travel_planner.model.Restaurant
import kotlin.math.roundToInt

private fun String.titleCaseWords(): String =
    replace("_", " ")
        .split(" ")
        .filter { it.isNotBlank() }
        .joinToString(" ") {
            it.replaceFirstChar { char ->
                char.uppercase()
            }
        }

fun ApiDestination.toUiModel(): Destination {
    val destinationId =
        id
            .ifBlank {
                geoapifyPlaceId.orEmpty()
            }
            .ifBlank {
                openTripMapXid.orEmpty()
            }
    val seed =
        slug
            ?.takeIf { it.isNotBlank() }
            ?: destinationId
                .ifBlank { name }
                .replace(" ", "-")
    val finalDescription =
        description
            ?.takeIf {
                it.isNotBlank()
            }
            ?: buildString {

                if (name.isNotBlank()) {
                    append(name)
                }

                val locationParts =
                    listOfNotNull(
                        city?.takeIf {
                            it.isNotBlank()
                        },
                        state?.takeIf {
                            it.isNotBlank()
                        },
                        country?.takeIf {
                            it.isNotBlank()
                        }
                    )
                        .distinct()


                if (locationParts.isNotEmpty()) {

                    if (isNotEmpty()) {
                        append(" is located in ")
                    }

                    append(
                        locationParts.joinToString(", ")
                    )

                    append(".")
                }
                if (famousFor.isNotEmpty()) {

                    append(" It is known for ")

                    append(
                        famousFor
                            .take(5)
                            .joinToString(", ")
                    )

                    append(".")
                }


                recommendedDuration?.let { duration ->

                    val minDays =
                        duration.minDays

                    val maxDays =
                        duration.maxDays


                    if (
                        minDays != null &&
                        maxDays != null
                    ) {

                        append(
                            " A recommended stay is " +
                                    "$minDays-$maxDays days."
                        )

                    } else if (
                        minDays != null
                    ) {

                        append(
                            " A recommended stay is " +
                                    "$minDays days."
                        )

                    } else if (
                        maxDays != null
                    ) {

                        append(
                            " A recommended stay is " +
                                    "up to $maxDays days."
                        )
                    }
                }
            }
                .ifBlank {
                    "Explore $name and discover its attractions, activities and local experiences."
                }


    return Destination(
        id = destinationId,

        name = name,

        slug = slug,
        city = city,

        state = state,

        country = country,

        region = state,
        description = finalDescription,
        destinationType =
            destinationType,
        travelStyles =
            emptyList(),

        suitableFor =
            emptyList(),
        budgetTier =
            when {

                isFeatured == true ->
                    "$$$"

                else ->
                    "$$"
            },
        seasons =
            seasons,
        placesToVisit =
            placesToVisit,
        activities =
            activities,
        popularActivities =
            popularActivities,
        beaches =
            beaches,
        shopping =
            shopping,
        nightlife =
            nightlife,
        hotels =
            hotels,
        restaurants =
            restaurants,
        famousFor =
            famousFor,
        recommendedDurationText =
            recommendedDuration?.let { duration ->

                when {

                    duration.minDays != null &&
                            duration.maxDays != null -> {

                        "${duration.minDays}-${duration.maxDays} days"
                    }

                    duration.minDays != null -> {

                        "${duration.minDays}+ days"
                    }

                    duration.maxDays != null -> {

                        "Up to ${duration.maxDays} days"
                    }

                    else -> null
                }
            },
        primaryAirportIata =
            primaryAirportIata,
        rating =
            averageRating ?: 0.0,
        imageUrl =
            coverImage
                ?.url
                ?.takeIf {
                    it.isNotBlank()
                }
                ?: "https://picsum.photos/seed/$seed/800/600",
        priceTier =
            when {

                popularityScore != null &&
                        popularityScore >= 80 ->
                    "$$$"

                isFeatured == true ->
                    "$$$"

                else ->
                    "$$"
            }
    )
}
fun ApiHotel.toUiModel(): Hotel {

    val avg =
        averageRating ?: 0.0


    val label =
        when {

            avg >= 9.0 ->
                "Wonderful"

            avg >= 8.0 ->
                "Excellent"

            avg >= 7.0 ->
                "Very Good"

            avg >= 6.0 ->
                "Good"

            avg > 0.0 ->
                "Fair"

            else ->
                "Not yet rated"
        }


    return Hotel(

        id = id,

        name = name,

        area =
            listOfNotNull(
                city,
                country
            )
                .joinToString(", "),

        rating =
            if (avg > 0.0) {
                "$avg $label"
            } else {
                label
            },

        stars =
            starRating ?: 0,

        price =
            (pricePerNight ?: 0.0)
                .roundToInt(),

        currency =
            currency ?: "USD",

        amenities =
            amenities
                ?.takeIf {
                    it.isNotEmpty()
                }
                ?.joinToString(" • ") {
                    it.titleCaseWords()
                }
                ?: "Amenities not listed"
    )
}
fun ApiRestaurant.toUiModel(): Restaurant {

    val cost =
        averageCostForTwo ?: 0.0


    val tier =
        when {

            cost <= 0 ->
                "$"

            cost < 500 ->
                "$"

            cost < 1500 ->
                "$$"

            cost < 3000 ->
                "$$$"

            else ->
                "$$$$"
        }


    return Restaurant(

        id = id,

        name = name,

        cuisine =
            cuisine
                ?.takeIf {
                    it.isNotEmpty()
                }
                ?.joinToString(" • ") {
                    it.titleCaseWords()
                }
                ?: (
                        restaurantType
                            ?: "Restaurant"
                        ),

        rating =
            averageRating ?: 0.0,

        priceTier =
            tier
    )
}
fun ApiActivity.toUiModel(): Activity {

    val durationLabel =
        if (
            duration != null &&
            durationUnit != null
        ) {

            "$duration ${durationUnit.lowercase()}"

        } else {

            "Duration varies"
        }


    return Activity(

        id = id,

        name = name,

        duration =
            durationLabel,

        price =
            (price ?: 0.0)
                .roundToInt()
    )
}
private fun extractHHmm(
    isoTimestamp: String?
): String {

    if (isoTimestamp == null) {
        return "--:--"
    }


    val tIndex =
        isoTimestamp.indexOf('T')


    if (
        tIndex == -1 ||
        isoTimestamp.length < tIndex + 6
    ) {
        return "--:--"
    }


    return isoTimestamp.substring(
        tIndex + 1,
        tIndex + 6
    )
}
fun ApiFlightOffer.toUiModel(): Flight {

    val airlineName =
        flights
            ?.firstOrNull()
            ?.airline
            ?: provider
            ?: "Unknown airline"


    val stopsLabel =
        when (stops) {

            null ->
                ""

            0 ->
                "Non-stop"

            1 ->
                "1 Stop"

            else ->
                "$stops Stops"
        }


    return Flight(

        id = id,

        airline =
            airlineName,

        price =
            (price ?: 0.0)
                .roundToInt(),

        departTime =
            extractHHmm(
                departureTime
            ),

        departAirport =
            departureAirport ?: "",

        arriveTime =
            extractHHmm(
                arrivalTime
            ),

        arriveAirport =
            arrivalAirport ?: "",

        duration =
            durationText ?: "",

        stopsLabel =
            stopsLabel
    )
}