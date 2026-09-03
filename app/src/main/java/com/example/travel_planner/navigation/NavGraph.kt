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
import com.example.travel_planner.ui.screens.RestaurantsScreen
import com.example.travel_planner.ui.screens.SignUpScreen
import com.example.travel_planner.ui.screens.flights.FlightsScreen
import com.voyago.app.ui.screens.aiplanner.AiPlannerScreen

sealed class Destination(val route: String) {
    data object Login : Destination("login")
    data object Home : Destination("home")
    data object SignUp : Destination("sign_up")
    data object AiPlanner : Destination("ai_planner")
    data object Destinations : Destination("destinations")
    data object DestinationDetails : Destination("destination_details/{destinationId}") {
        fun createRoute(destinationId: String) = "destination_details/$destinationId"
    }
    data object Flights : Destination("flights")
    data object Hotels : Destination("hotels")
    data object Restaurants : Destination("restaurants")
    data object Activities : Destination("activities")
}

// Login and SignUp are standalone auth screens - no bottom nav on them
private val bottomNavRoutes = setOf(
    Destination.Home.route,
    Destination.Destinations.route,
    Destination.Flights.route,
    Destination.AiPlanner.route
)

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: Destination.Login.route

    fun navigate(destination: Destination) {
        navController.navigate(destination.route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            NavHost(navController = navController, startDestination = Destination.Login.route) {
                composable(Destination.Login.route) {
                    LoginScreen(
                        onLoginSuccess = { navigate(Destination.Home) },
                        onSignUpClick = { navController.navigate(Destination.SignUp.route) }
                    )
                }
                composable(Destination.SignUp.route) {
                    SignUpScreen(
                        onCreateAccount = { _, _, _ -> navigate(Destination.Home) },
                        onSignInClick = { navController.popBackStack() }
                    )
                }
                composable(Destination.Home.route) {
                    HomeScreen(
                        onSearchDestination = { navigate(Destination.Destinations) },
                        onDestinationClick = { destinationId ->
                            navController.navigate(Destination.DestinationDetails.createRoute(destinationId))
                        }
                    )
                }
                composable(Destination.AiPlanner.route) {
                    AiPlannerScreen()
                }
                composable(Destination.Destinations.route) {
                    DestinationsScreen(
                        onDestinationClick = { destinationId ->
                            navController.navigate(Destination.DestinationDetails.createRoute(destinationId))
                        }
                    )
                }
                composable(Destination.DestinationDetails.route) { backStackEntry ->
                    val destinationId = backStackEntry.arguments?.getString("destinationId").orEmpty()
                    DestinationDetailsScreen(
                        destinationId = destinationId,
                        onBackClick = { navController.popBackStack() },
                        onPlanTripClick = { navigate(Destination.Flights) },
                        onViewHotelsClick = { navController.navigate(Destination.Hotels.route) },
                        onViewRestaurantsClick = { navController.navigate(Destination.Restaurants.route) },
                        onViewActivitiesClick = { navController.navigate(Destination.Activities.route) }
                    )
                }
                composable(Destination.Flights.route) {
                    FlightsScreen()
                }
                composable(Destination.Hotels.route) {
                    HotelsScreen(
                        onSelectHotel = { /* TODO: navigate to a hotel detail screen once one exists */ }
                    )
                }
                composable(Destination.Restaurants.route) {
                    RestaurantsScreen(
                        onSelectRestaurant = { /* TODO: navigate to a restaurant detail screen once one exists */ }
                    )
                }
                composable(Destination.Activities.route) {
                    ActivitiesScreen(
                        onSelectActivity = { /* TODO: navigate to an activity detail screen once one exists */ }
                    )
                }
            }
        }

        if (currentRoute in bottomNavRoutes) {
            val current = when (currentRoute) {
                Destination.Destinations.route -> Destination.Destinations
                Destination.Flights.route -> Destination.Flights
                Destination.AiPlanner.route -> Destination.AiPlanner
                else -> Destination.Home
            }
            BottomNavBar(current = current, onNavigate = ::navigate)
        }
    }
}