package com.example.travel_planner.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.travel_planner.ui.components.BottomNavBar
import com.example.travel_planner.ui.screens.ActivitiesScreen
import com.example.travel_planner.ui.screens.DestinationDetailsScreen
import com.example.travel_planner.ui.screens.DestinationsScreen
import com.example.travel_planner.ui.screens.HomeScreen
import com.example.travel_planner.ui.screens.HotelsScreen
import com.example.travel_planner.ui.screens.LoginScreen
import com.example.travel_planner.ui.screens.OtpVerificationScreen
import com.example.travel_planner.ui.screens.RestaurantsScreen
import com.example.travel_planner.ui.screens.SignUpScreen
import com.example.travel_planner.ui.screens.flights.FlightDetailsScreen
import com.example.travel_planner.ui.screens.flights.FlightsScreen
import com.example.travel_planner.ui.viewmodel.FlightsViewModel
import com.voyago.app.ui.screens.aiplanner.AiPlannerScreen
import java.net.URLDecoder
import java.net.URLEncoder

sealed class Destination(val route: String) {

    data object Login : Destination("login")

    data object Home : Destination("home")

    data object SignUp : Destination("sign_up")

    data object Otp : Destination("otp/{email}") {

        fun createRoute(email: String) =
            "otp/${URLEncoder.encode(email, "UTF-8")}"
    }

    data object AiPlanner : Destination("ai_planner")

    data object Destinations : Destination("destinations")

    data object DestinationsSearch :
        Destination("destinations_search/{query}/{type}") {

        fun createRoute(
            query: String = "",
            type: String = ""
        ) =
            "destinations_search/" +
                    "${URLEncoder.encode(query.ifBlank { " " }, "UTF-8")}/" +
                    "${URLEncoder.encode(type.ifBlank { " " }, "UTF-8")}"
    }

    data object DestinationDetails :
        Destination("destination_details/{destinationId}") {

        fun createRoute(destinationId: String) =
            "destination_details/$destinationId"
    }
    data object Flights :
        Destination("flights")
    data object FlightDetails :
        Destination("flight_details/{flightOfferId}") {

        fun createRoute(flightOfferId: String) =
            "flight_details/$flightOfferId"
    }

    data object Hotels :
        Destination("hotels")

    data object Restaurants :
        Destination("restaurants")

    data object Activities :
        Destination("activities")
}


private val bottomNavRoutes = setOf(
    Destination.Home.route,
    Destination.Destinations.route,
    Destination.Flights.route,
    Destination.AiPlanner.route
)


@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {

    val backStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        backStackEntry?.destination?.route
            ?: Destination.Login.route


    fun navigate(destination: Destination) {

        navController.navigate(destination.route) {

            popUpTo(
                navController.graph.findStartDestination().id
            ) {
                saveState = true
            }

            launchSingleTop = true

            restoreState = true
        }
    }
    val flightsViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel<FlightsViewModel>()


    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier.weight(1f)
        ) {

            NavHost(
                navController = navController,
                startDestination = Destination.Login.route
            ) {

                composable(
                    Destination.Login.route
                ) {

                    LoginScreen(

                        onLoginSuccess = {
                            navigate(Destination.Home)
                        },

                        onSignUpClick = {
                            navController.navigate(
                                Destination.SignUp.route
                            )
                        },

                        onNeedsVerification = { email ->

                            navController.navigate(
                                Destination.Otp.createRoute(email)
                            )
                        }
                    )
                }

                composable(
                    Destination.SignUp.route
                ) {

                    SignUpScreen(

                        onAccountCreated = { email ->

                            navController.navigate(
                                Destination.Otp.createRoute(email)
                            )
                        },

                        onSignInClick = {
                            navController.popBackStack()
                        }
                    )
                }

                composable(
                    Destination.Otp.route
                ) { backStackEntry ->

                    val email =
                        backStackEntry
                            .arguments
                            ?.getString("email")
                            ?.let {
                                URLDecoder.decode(
                                    it,
                                    "UTF-8"
                                )
                            }
                            .orEmpty()

                    OtpVerificationScreen(

                        email = email,

                        onVerified = {

                            navController.navigate(
                                Destination.Login.route
                            ) {

                                popUpTo(
                                    Destination.Login.route
                                ) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }

                composable(
                    Destination.Home.route
                ) {

                    HomeScreen(

                        onSearchDestination = { query ->

                            if (query.isBlank()) {

                                navigate(
                                    Destination.Destinations
                                )

                            } else {

                                navController.navigate(
                                    Destination.DestinationsSearch
                                        .createRoute(
                                            query = query
                                        )
                                )
                            }
                        },

                        onCategoryClick = { type ->

                            navController.navigate(
                                Destination.DestinationsSearch
                                    .createRoute(
                                        type = type
                                    )
                            )
                        },

                        onDestinationClick = { destinationId ->

                            navController.navigate(
                                Destination.DestinationDetails
                                    .createRoute(
                                        destinationId
                                    )
                            )
                        }
                    )
                }

                composable(
                    Destination.AiPlanner.route
                ) {

                    AiPlannerScreen()
                }

                composable(
                    Destination.Destinations.route
                ) {

                    DestinationsScreen(

                        onDestinationClick = {
                                destinationId ->

                            navController.navigate(
                                Destination.DestinationDetails
                                    .createRoute(
                                        destinationId
                                    )
                            )
                        }
                    )
                }

                composable(
                    Destination.DestinationsSearch.route
                ) { backStackEntry ->

                    val query =
                        backStackEntry
                            .arguments
                            ?.getString("query")
                            ?.let {
                                URLDecoder.decode(
                                    it,
                                    "UTF-8"
                                )
                            }
                            .orEmpty()
                            .trim()

                    val type =
                        backStackEntry
                            .arguments
                            ?.getString("type")
                            ?.let {
                                URLDecoder.decode(
                                    it,
                                    "UTF-8"
                                )
                            }
                            .orEmpty()
                            .trim()

                    DestinationsScreen(

                        initialQuery = query,

                        initialDestinationType = type,

                        onDestinationClick = {
                                destinationId ->

                            navController.navigate(
                                Destination.DestinationDetails
                                    .createRoute(
                                        destinationId
                                    )
                            )
                        }
                    )
                }

                composable(
                    Destination.DestinationDetails.route
                ) { backStackEntry ->

                    val destinationId =
                        backStackEntry
                            .arguments
                            ?.getString(
                                "destinationId"
                            )
                            .orEmpty()

                    DestinationDetailsScreen(

                        destinationId = destinationId,

                        onBackClick = {
                            navController.popBackStack()
                        },

                        onPlanTripClick = {
                            navigate(Destination.Flights)
                        },

                        onViewHotelsClick = {
                            navController.navigate(
                                Destination.Hotels.route
                            )
                        },

                        onViewRestaurantsClick = {
                            navController.navigate(
                                Destination.Restaurants.route
                            )
                        },

                        onViewActivitiesClick = {
                            navController.navigate(
                                Destination.Activities.route
                            )
                        }
                    )
                }

                composable(
                    Destination.Flights.route
                ) {

                    FlightsScreen(

                        viewModel = flightsViewModel,

                        onSearchClick = {
                            // Search stays on this screen.
                        },

                        onFlightDetails = { flightOfferId ->

                            navController.navigate(
                                Destination.FlightDetails
                                    .createRoute(
                                        flightOfferId
                                    )
                            )
                        }
                    )
                }

                composable(
                    Destination.FlightDetails.route
                ) { backStackEntry ->

                    val flightOfferId =
                        backStackEntry
                            .arguments
                            ?.getString(
                                "flightOfferId"
                            )
                            .orEmpty()

                    FlightDetailsScreen(

                        flightOfferId = flightOfferId,

                        viewModel = flightsViewModel,

                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }

                composable(
                    Destination.Hotels.route
                ) {

                    HotelsScreen(

                        onSelectHotel = {
                        }
                    )
                }

                composable(
                    Destination.Restaurants.route
                ) {

                    RestaurantsScreen(

                        onSelectRestaurant = {
                        }
                    )
                }

                composable(
                    Destination.Activities.route
                ) {

                    ActivitiesScreen(

                        onSelectActivity = {
                        }
                    )
                }
            }
        }

        if (currentRoute in bottomNavRoutes) {

            val current =
                when (currentRoute) {

                    Destination.Destinations.route ->
                        Destination.Destinations

                    Destination.Flights.route ->
                        Destination.Flights

                    Destination.AiPlanner.route ->
                        Destination.AiPlanner

                    else ->
                        Destination.Home
                }

            BottomNavBar(
                current = current,
                onNavigate = ::navigate
            )
        }
    }
}