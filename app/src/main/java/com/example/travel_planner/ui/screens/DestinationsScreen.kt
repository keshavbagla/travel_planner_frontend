package com.example.travel_planner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.compose.AsyncImage
import com.example.travel_planner.data.TravelRepository
import com.example.travel_planner.data.toUiModel
import com.example.travel_planner.model.Destination
import com.example.travel_planner.ui.theme.Background
import com.example.travel_planner.ui.theme.Border
import com.example.travel_planner.ui.theme.LightBlue
import com.example.travel_planner.ui.theme.Navy
import com.example.travel_planner.ui.theme.PrimaryBlue
import com.example.travel_planner.ui.theme.Slate
import com.example.travel_planner.ui.theme.White
import com.example.travel_planner.ui.viewmodel.ResourceViewModel
import com.example.travel_planner.ui.viewmodel.UiState
import kotlinx.coroutines.delay

private val DESTINATION_TYPES = listOf(
    "Beach",
    "Mountains",
    "City",
    "Adventure",
    "Cultural"
)

@Composable
fun DestinationsScreen(
    onDestinationClick: (String, String) -> Unit = { _, _ -> },
    initialQuery: String = "",
    initialDestinationType: String = "",

    viewModel: ResourceViewModel<List<Destination>> = viewModel(
        factory = viewModelFactory {

            initializer {

                ResourceViewModel {

                    loadDestinationResults(
                        query = initialQuery,
                        destinationType = initialDestinationType,
                        region = "",
                        budgetTier = "",
                        season = "",
                        tripType = initialDestinationType
                    )
                }
            }
        }
    )
) {

    val uiState by viewModel.uiState.collectAsState()

    var searchQuery by remember {
        mutableStateOf(initialQuery)
    }

    var selectedType by remember {
        mutableStateOf(initialDestinationType)
    }

    var selectedRegion by remember {
        mutableStateOf("")
    }

    var selectedBudgetTier by remember {
        mutableStateOf("")
    }

    var selectedSeason by remember {
        mutableStateOf("")
    }

    var firstLaunch by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(
        searchQuery,
        selectedType,
        selectedRegion,
        selectedBudgetTier,
        selectedSeason
    ) {

        if (firstLaunch) {
            firstLaunch = false
            return@LaunchedEffect
        }

        delay(400)

        viewModel.refresh {

            loadDestinationResults(
                query = searchQuery,
                destinationType = selectedType,
                region = selectedRegion,
                budgetTier = selectedBudgetTier,
                season = selectedSeason,
                tripType = selectedType
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .border(
                    width = 0.5.dp,
                    color = Border
                )
                .padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(22.dp)
                    )
                    .background(LightBlue)
                    .border(
                        width = 1.dp,
                        color = PrimaryBlue.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(22.dp)
                    )
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    ),

                verticalAlignment =
                    Alignment.CenterVertically,

                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = PrimaryBlue,
                    modifier = Modifier.size(18.dp)
                )

                Box(
                    modifier = Modifier.weight(1f)
                ) {

                    if (searchQuery.isEmpty()) {

                        Text(
                            text = "Search destination...",
                            style =
                                MaterialTheme.typography.bodyMedium,
                            color = Slate
                        )
                    }

                    BasicTextField(
                        value = searchQuery,

                        onValueChange = {
                            searchQuery = it
                        },

                        singleLine = true,

                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            color = Navy
                        ),

                        modifier =
                            Modifier.fillMaxWidth()
                    )
                }

                Icon(
                    imageVector = Icons.Filled.Tune,
                    contentDescription = "Filters",
                    tint = PrimaryBlue,
                    modifier = Modifier.size(18.dp)
                )
            }

            Row(
                modifier = Modifier.horizontalScroll(
                    rememberScrollState()
                ),

                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                RegionFilterPill(
                    selected = selectedRegion,

                    onSelect = {
                        selectedRegion = it
                    }
                )

                BudgetFilterPill(
                    selected = selectedBudgetTier,

                    onSelect = {
                        selectedBudgetTier = it
                    }
                )

                SeasonFilterPill(
                    selected = selectedSeason,

                    onSelect = {
                        selectedSeason = it
                    }
                )

                TripTypeFilterPill(
                    selected = selectedType,

                    onSelect = {
                        selectedType = it
                    }
                )
            }
        }

        when (val state = uiState) {

            is UiState.Loading -> {

                Box(
                    modifier = Modifier.fillMaxSize(),

                    contentAlignment =
                        Alignment.Center
                ) {

                    CircularProgressIndicator(
                        color = PrimaryBlue
                    )
                }
            }


            is UiState.Error -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text =
                            "Couldn't load destinations: ${state.message}",

                        style =
                            MaterialTheme.typography.bodyMedium,

                        color = Slate
                    )
                }
            }

            is UiState.Success -> {

                if (state.data.isEmpty()) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Text(
                            text = when {

                                searchQuery.isNotBlank() ->
                                    "No destinations match \"$searchQuery\"."

                                selectedType.isNotBlank() ->
                                    "No \"$selectedType\" destinations found."

                                selectedRegion.isNotBlank() ->
                                    "No destinations found in \"$selectedRegion\"."

                                selectedBudgetTier.isNotBlank() ->
                                    "No destinations found for \"$selectedBudgetTier\" budget."

                                selectedSeason.isNotBlank() ->
                                    "No destinations found for \"$selectedSeason\" season."

                                else ->
                                    "No destinations found yet."
                            },

                            style =
                                MaterialTheme.typography.bodyMedium,

                            color = Slate
                        )
                    }

                } else {

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp),

                        verticalArrangement =
                            Arrangement.spacedBy(16.dp)
                    ) {

                        items(
                            items = state.data,
                            key = { destination ->
                                destination.id.ifBlank {
                                    destination.name
                                }
                            }
                        ) { destination ->

                            DestinationStackCard(
                                destination = destination,

                                onClick = {
                                    onDestinationClick(
                                        destination.id,
                                        destination.name
                                    )
                                }
                            )
                        }
                    }
                }
            }

            else -> {}
        }
        }
    }

private suspend fun loadDestinationResults(
    query: String,
    destinationType: String,
    region: String,
    budgetTier: String,
    season: String,
    tripType: String
): List<Destination> {

    val cleanQuery =
        query.trim()

    if (cleanQuery.isNotBlank()) {

        val result =
            TravelRepository.searchDestinations(
                keyword = cleanQuery,

                region =
                    region.takeIf {
                        it.isNotBlank()
                    },

                budgetTier =
                    budgetTier.takeIf {
                        it.isNotBlank()
                    },

                season =
                    season.takeIf {
                        it.isNotBlank()
                    },

                tripType =
                    tripType.takeIf {
                        it.isNotBlank()
                    }
            )

        return result.results
            .map {
                it.toUiModel()
            }
    }

    val result =
        TravelRepository.getDestinations(
            destinationType =
                destinationType.takeIf {
                    it.isNotBlank()
                },

            country = null
        )

    return result.destinations
        .map {
            it.toUiModel()
        }
}

@Composable
private fun RegionFilterPill(
    selected: String,
    onSelect: (String) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val regions = listOf(
        "North India",
        "South India",
        "East India",
        "West India",
        "Central India",
        "Northeast India"
    )

    FilterDropdownPill(
        label =
            selected.ifBlank {
                "Region"
            },

        selected =
            selected.isNotBlank(),

        expanded = expanded,

        onClick = {
            expanded = true
        },

        onDismiss = {
            expanded = false
        }
    ) {

        if (selected.isNotBlank()) {

            DropdownMenuItem(
                text = {
                    Text("All Regions")
                },

                onClick = {
                    onSelect("")
                    expanded = false
                }
            )
        }

        regions.forEach { region ->

            DropdownMenuItem(
                text = {
                    Text(region)
                },

                onClick = {
                    onSelect(region)
                    expanded = false
                }
            )
        }
    }
}

@Composable
private fun BudgetFilterPill(
    selected: String,
    onSelect: (String) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val budgets = listOf(
        "Budget",
        "Mid-range",
        "Luxury"
    )

    FilterDropdownPill(
        label =
            selected.ifBlank {
                "Budget Tier"
            },

        selected =
            selected.isNotBlank(),

        expanded = expanded,

        onClick = {
            expanded = true
        },

        onDismiss = {
            expanded = false
        }
    ) {

        if (selected.isNotBlank()) {

            DropdownMenuItem(
                text = {
                    Text("All Budgets")
                },

                onClick = {
                    onSelect("")
                    expanded = false
                }
            )
        }

        budgets.forEach { budget ->

            DropdownMenuItem(
                text = {
                    Text(budget)
                },

                onClick = {
                    onSelect(budget)
                    expanded = false
                }
            )
        }
    }
}


@Composable
private fun SeasonFilterPill(
    selected: String,
    onSelect: (String) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val seasons = listOf(
        "Summer",
        "Monsoon",
        "Winter"
    )

    FilterDropdownPill(
        label =
            selected.ifBlank {
                "Season"
            },

        selected =
            selected.isNotBlank(),

        expanded = expanded,

        onClick = {
            expanded = true
        },

        onDismiss = {
            expanded = false
        }
    ) {

        if (selected.isNotBlank()) {

            DropdownMenuItem(
                text = {
                    Text("All Seasons")
                },

                onClick = {
                    onSelect("")
                    expanded = false
                }
            )
        }

        seasons.forEach { season ->

            DropdownMenuItem(
                text = {
                    Text(season)
                },

                onClick = {
                    onSelect(season)
                    expanded = false
                }
            )
        }
    }
}

@Composable
private fun TripTypeFilterPill(
    selected: String,
    onSelect: (String) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    FilterDropdownPill(
        label =
            selected.ifBlank {
                "Trip Type"
            },

        selected =
            selected.isNotBlank(),

        expanded = expanded,

        onClick = {
            expanded = true
        },

        onDismiss = {
            expanded = false
        }
    ) {

        if (selected.isNotBlank()) {

            DropdownMenuItem(
                text = {
                    Text("All Trip Types")
                },

                onClick = {
                    onSelect("")
                    expanded = false
                }
            )
        }

        DESTINATION_TYPES.forEach { type ->

            DropdownMenuItem(
                text = {
                    Text(type)
                },

                onClick = {
                    onSelect(type)
                    expanded = false
                }
            )
        }
    }
}

@Composable
private fun FilterDropdownPill(
    label: String,
    selected: Boolean,
    expanded: Boolean,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {

    Box {

        Row(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(16.dp)
                )
                .background(
                    if (selected) {
                        LightBlue
                    } else {
                        White
                    }
                )
                .border(
                    width = 1.dp,

                    color =
                        if (selected) {
                            PrimaryBlue
                        } else {
                            Border
                        },

                    shape =
                        RoundedCornerShape(16.dp)
                )
                .clickable {
                    onClick()
                }
                .padding(
                    horizontal = 12.dp,
                    vertical = 6.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically,

            horizontalArrangement =
                Arrangement.spacedBy(4.dp)
        ) {

            Text(
                text = label,

                style =
                    MaterialTheme.typography.bodyMedium,

                color =
                    if (selected) {
                        PrimaryBlue
                    } else {
                        Navy
                    }
            )

            Icon(
                imageVector =
                    Icons.Filled.KeyboardArrowDown,

                contentDescription = null,

                tint = PrimaryBlue,

                modifier =
                    Modifier.size(14.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss
        ) {
            content()
        }
    }
}

@Composable
private fun DestinationStackCard(
    destination: Destination,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(
                RoundedCornerShape(16.dp)
            )
            .background(Navy)
            .clickable {
                onClick()
            }
    ) {

        AsyncImage(
            model = destination.imageUrl,

            contentDescription =
                destination.name,

            modifier =
                Modifier.fillMaxSize()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(16.dp),

            horizontalArrangement =
                Arrangement.SpaceBetween,

            verticalAlignment =
                Alignment.Bottom
        ) {

            Column(
                verticalArrangement =
                    Arrangement.spacedBy(4.dp)
            ) {

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically,

                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = destination.name,

                        color = White,

                        fontWeight =
                            FontWeight.Bold,

                        fontSize = 20.sp
                    )

                    Box(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(4.dp)
                            )
                            .background(
                                White.copy(
                                    alpha = 0.2f
                                )
                            )
                            .padding(
                                horizontal = 6.dp,
                                vertical = 2.dp
                            )
                    ) {

                        Text(
                            text =
                                destination.priceTier,

                            color = White,

                            style =
                                MaterialTheme
                                    .typography
                                    .labelSmall
                        )
                    }
                }


                destination.country?.let {

                    if (it.isNotBlank()) {

                        Text(
                            text = it,

                            color =
                                White.copy(
                                    alpha = 0.8f
                                ),

                            style =
                                MaterialTheme
                                    .typography
                                    .bodyMedium
                        )
                    }
                }
            }


            Column(
                horizontalAlignment =
                    Alignment.End,

                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically,

                    horizontalArrangement =
                        Arrangement.spacedBy(4.dp)
                ) {

                    if (destination.rating > 0.0) {

                        Icon(
                            imageVector =
                                Icons.Filled.Star,

                            contentDescription = null,

                            tint = White,

                            modifier =
                                Modifier.size(12.dp)
                        )

                        Text(
                            text =
                                destination.rating
                                    .toString(),

                            color = White,

                            style =
                                MaterialTheme
                                    .typography
                                    .labelMedium
                        )

                    } else {

                        Text(
                            text = "New",

                            color = White,

                            style =
                                MaterialTheme
                                    .typography
                                    .labelMedium
                        )
                    }
                }


                Box(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(12.dp)
                        )
                        .background(
                            PrimaryBlue
                        )
                        .padding(
                            horizontal = 16.dp,
                            vertical = 6.dp
                        )
                ) {

                    Text(
                        text = "Explore",

                        color = White,

                        style =
                            MaterialTheme
                                .typography
                                .labelMedium
                    )
                }
            }
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun DestinationsScreenPreview() {

    DestinationsScreen()
}

