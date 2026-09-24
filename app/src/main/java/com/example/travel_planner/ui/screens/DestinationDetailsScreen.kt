package com.example.travel_planner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.compose.AsyncImage
import com.example.travel_planner.data.TravelRepository
import com.example.travel_planner.model.Activity
import com.example.travel_planner.model.Destination
import com.example.travel_planner.ui.theme.Background
import com.example.travel_planner.ui.theme.LightBlue
import com.example.travel_planner.ui.theme.Navy
import com.example.travel_planner.ui.theme.PrimaryBlue
import com.example.travel_planner.ui.theme.Slate
import com.example.travel_planner.ui.theme.White
import com.example.travel_planner.ui.viewmodel.ResourceViewModel
import com.example.travel_planner.ui.viewmodel.UiState
import java.util.Locale


@Composable
fun DestinationDetailsScreen(
    destinationId: String,
    destinationName: String = "",
    onBackClick: () -> Unit = {},
    onPlanTripClick: () -> Unit = {},
    onViewHotelsClick: () -> Unit = {},
    onViewRestaurantsClick: () -> Unit = {},
    onViewActivitiesClick: () -> Unit = {},

    detailsViewModel: ResourceViewModel<Destination> =
        viewModel<ResourceViewModel<Destination>>(
            key = "destinationDetails-$destinationId-$destinationName",
            factory = viewModelFactory {

                initializer<ResourceViewModel<Destination>> {

                    ResourceViewModel<Destination> {

                        TravelRepository.loadDestinationUi(
                            id = destinationId,
                            name = destinationName
                        )
                    }
                }
            }
        ),

    activitiesViewModel: ResourceViewModel<List<Activity>> =
        viewModel<ResourceViewModel<List<Activity>>>(
            key = "destinationActivities-$destinationId-$destinationName",
            factory = viewModelFactory {

                initializer<ResourceViewModel<List<Activity>>> {

                    ResourceViewModel<List<Activity>> {

                        if (destinationId.isBlank()) {
                            emptyList()
                        } else {
                            TravelRepository.loadActivitiesUi(
                                destinationId = destinationId
                            )
                        }
                    }
                }
            }
        )
) {

    val detailsState by detailsViewModel.uiState.collectAsState()
    val activitiesState by activitiesViewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        when (val state = detailsState) {

            is UiState.Idle -> {
                // Nothing to show
            }

            is UiState.Loading -> {
                LoadingDestination()
            }

            is UiState.Error -> {
                DestinationError(
                    message = state.message,
                    onBackClick = onBackClick,
                    onRetry = {
                        detailsViewModel.refresh()
                    }
                )
            }

            is UiState.Success -> {

                val destination = state.data

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        bottom = 110.dp
                    )
                ) {

                    /*
                     * HERO
                     */
                    item {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                        ) {

                            AsyncImage(
                                model = destination.imageUrl,
                                contentDescription = destination.name,
                                modifier = Modifier.fillMaxSize()
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .align(Alignment.BottomCenter)
                                    .background(
                                        Color.Black.copy(alpha = 0.30f)
                                    )
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement =
                                    Arrangement.SpaceBetween
                            ) {

                                CircleIconButton(
                                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                                    onClick = onBackClick
                                )

                                CircleIconButton(
                                    icon = Icons.Filled.FavoriteBorder,
                                    onClick = {}
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(
                                        start = 20.dp,
                                        end = 20.dp,
                                        bottom = 18.dp
                                    )
                            ) {

                                Text(
                                    text = destination.name,
                                    color = White,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                val location = listOfNotNull(
                                    destination.city,
                                    destination.state,
                                    destination.country
                                )
                                    .distinct()
                                    .joinToString(", ")

                                if (location.isNotBlank()) {

                                    Text(
                                        text = location,
                                        color = White.copy(alpha = 0.9f),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }

                    item {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 16.dp
                                ),
                            verticalArrangement =
                                Arrangement.spacedBy(14.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement =
                                    Arrangement.SpaceBetween,
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    destination.country
                                        ?.takeIf { it.isNotBlank() }
                                        ?.let {

                                            Text(
                                                text = it.uppercase(
                                                    Locale.getDefault()
                                                ),
                                                color = PrimaryBlue,
                                                fontSize = 12.sp,
                                                fontWeight =
                                                    FontWeight.Bold
                                            )
                                        }

                                    Text(
                                        text = destination.name,
                                        color = Navy,
                                        fontSize = 24.sp,
                                        fontWeight =
                                            FontWeight.Bold
                                    )
                                }

                                RatingBadge(
                                    rating = destination.rating
                                )
                            }

                            Text(
                                text = destination.description
                                    ?.takeIf { it.isNotBlank() }
                                    ?: "Discover the highlights, activities and places to visit in ${destination.name}.",
                                color = Slate,
                                style =
                                    MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    item {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement =
                                Arrangement.spacedBy(10.dp)
                        ) {

                            destination.recommendedDurationText?.let {

                                InfoChip(
                                    title = "Duration",
                                    value = it,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            destination.primaryAirportIata?.let {

                                InfoChip(
                                    title = "Airport",
                                    value = it,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    if (destination.destinationType.isNotEmpty()) {

                        item {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp,
                                        vertical = 16.dp
                                    ),
                                verticalArrangement =
                                    Arrangement.spacedBy(8.dp)
                            ) {

                                Text(
                                    text = "Travel Type",
                                    color = Navy,
                                    fontSize = 17.sp,
                                    fontWeight =
                                        FontWeight.Bold
                                )

                                LazyRow(
                                    horizontalArrangement =
                                        Arrangement.spacedBy(8.dp)
                                ) {

                                    items(
                                        destination.destinationType
                                    ) { type ->

                                        TagChip(
                                            text = type
                                                .replace("_", " ")
                                                .replaceFirstChar {
                                                    it.uppercase()
                                                }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (destination.famousFor.isNotEmpty()) {

                        item {

                            InformationSection(
                                title = "Famous For",
                                items = destination.famousFor
                            )
                        }
                    }

                    if (destination.placesToVisit.isNotEmpty()) {

                        item {

                            InformationSection(
                                title = "Places to Visit",
                                items = destination.placesToVisit
                            )
                        }
                    }

                    if (destination.popularActivities.isNotEmpty()) {

                        item {

                            InformationSection(
                                title = "Popular Activities",
                                items = destination.popularActivities
                            )
                        }
                    }

                    item {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                ),
                            verticalArrangement =
                                Arrangement.spacedBy(10.dp)
                        ) {

                            Text(
                                text = "Explore",
                                color = Navy,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement =
                                    Arrangement.spacedBy(10.dp)
                            ) {

                                QuickLinkButton(
                                    label = "Hotels",
                                    onClick = onViewHotelsClick,
                                    modifier = Modifier.weight(1f)
                                )

                                QuickLinkButton(
                                    label = "Restaurants",
                                    onClick = onViewRestaurantsClick,
                                    modifier = Modifier.weight(1f)
                                )

                                QuickLinkButton(
                                    label = "Activities",
                                    onClick = onViewActivitiesClick,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    item {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            verticalArrangement =
                                Arrangement.spacedBy(10.dp)
                        ) {

                            Text(
                                text = "Things to Do",
                                color = Navy,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(
                                    horizontal = 16.dp
                                )
                            )

                            when (val actState = activitiesState) {

                                is UiState.Idle -> {
                                }

                                is UiState.Loading -> {

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp),
                                        contentAlignment =
                                            Alignment.Center
                                    ) {

                                        CircularProgressIndicator(
                                            color = PrimaryBlue,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                }

                                is UiState.Error -> {

                                    Text(
                                        text = "Couldn't load activities.",
                                        color = Slate,
                                        fontSize = 13.sp,
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp
                                        )
                                    )
                                }

                                is UiState.Success -> {

                                    if (actState.data.isEmpty()) {

                                        Text(
                                            text = "No activities listed for this destination yet.",
                                            color = Slate,
                                            fontSize = 13.sp,
                                            modifier = Modifier.padding(
                                                horizontal = 16.dp
                                            )
                                        )

                                    } else {

                                        LazyRow(
                                            contentPadding =
                                                PaddingValues(
                                                    horizontal = 16.dp
                                                ),
                                            horizontalArrangement =
                                                Arrangement.spacedBy(12.dp)
                                        ) {

                                            items(actState.data) { activity ->

                                                ActivityPreviewCard(
                                                    activity = activity
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                /*
                 * BOTTOM CTA
                 */
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(White)
                        .padding(
                            horizontal = 20.dp,
                            vertical = 14.dp
                        ),
                    horizontalArrangement =
                        Arrangement.SpaceBetween,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Column {

                        Text(
                            text = "Plan your trip",
                            color = Slate,
                            fontSize = 12.sp
                        )

                        Text(
                            text = "AI Travel Planner",
                            color = Navy,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(22.dp))
                            .background(PrimaryBlue)
                            .clickable {
                                onPlanTripClick()
                            }
                            .padding(
                                horizontal = 24.dp,
                                vertical = 12.dp
                            )
                    ) {

                        Text(
                            text = "Plan Trip",
                            color = White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun LoadingDestination() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            CircularProgressIndicator(
                color = PrimaryBlue
            )

            Text(
                text = "Loading destination...",
                color = Slate,
                fontSize = 14.sp
            )
        }
    }
}


@Composable
private fun DestinationError(
    message: String,
    onBackClick: () -> Unit,
    onRetry: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Couldn't load destination",
            color = Navy,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = message,
            color = Slate,
            fontSize = 13.sp,
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 20.dp
            )
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(LightBlue)
                    .clickable {
                        onBackClick()
                    }
                    .padding(
                        horizontal = 20.dp,
                        vertical = 11.dp
                    )
            ) {

                Text(
                    text = "Back",
                    color = Navy,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(PrimaryBlue)
                    .clickable {
                        onRetry()
                    }
                    .padding(
                        horizontal = 20.dp,
                        vertical = 11.dp
                    )
            ) {

                Text(
                    text = "Retry",
                    color = White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


@Composable
private fun RatingBadge(
    rating: Double
) {

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(White)
            .padding(
                horizontal = 9.dp,
                vertical = 6.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        if (rating > 0.0) {

            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(14.dp)
            )

            Text(
                text = String.format(
                    Locale.getDefault(),
                    "%.1f",
                    rating
                ),
                color = Navy,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

        } else {

            Text(
                text = "New",
                color = Navy,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
private fun InfoChip(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(LightBlue)
            .padding(12.dp)
    ) {

        Text(
            text = title,
            color = Slate,
            fontSize = 11.sp
        )

        Text(
            text = value,
            color = Navy,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
private fun TagChip(
    text: String
) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(LightBlue)
            .padding(
                horizontal = 12.dp,
                vertical = 7.dp
            )
    ) {

        Text(
            text = text,
            color = PrimaryBlue,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@Composable
private fun InformationSection(
    title: String,
    items: List<String>
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 10.dp
            ),
        verticalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = title,
            color = Navy,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        items.take(6).forEach { item ->

            Row(
                verticalAlignment =
                    Alignment.CenterVertically,
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlue)
                )

                Text(
                    text = item,
                    color = Slate,
                    fontSize = 13.sp
                )
            }
        }
    }
}


@Composable
private fun QuickLinkButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(White)
            .clickable {
                onClick()
            }
            .padding(vertical = 13.dp),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = label,
            color = PrimaryBlue,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
private fun CircleIconButton(
    icon: ImageVector,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(
                Color.Black.copy(alpha = 0.30f)
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = White,
            modifier = Modifier.size(19.dp)
        )
    }
}


@Composable
private fun ActivityPreviewCard(
    activity: Activity
) {

    Column(
        modifier = Modifier
            .width(220.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(White)
            .padding(14.dp),
        verticalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(LightBlue)
        )

        Text(
            text = activity.name,
            color = Navy,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )

        Text(
            text = activity.duration,
            color = Slate,
            fontSize = 12.sp
        )

        if (activity.price > 0) {

            Text(
                text = "₹${activity.price}",
                color = PrimaryBlue,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}