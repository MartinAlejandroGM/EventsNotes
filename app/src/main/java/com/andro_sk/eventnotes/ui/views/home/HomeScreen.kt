package com.andro_sk.eventnotes.ui.views.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andro_sk.eventnotes.R
import com.andro_sk.eventnotes.domain.models.EventModel
import com.andro_sk.eventnotes.ui.core.EventCard
import com.andro_sk.eventnotes.ui.core.LoadingDialog
import com.andro_sk.eventnotes.ui.core.ThemeToggleButton
import com.andro_sk.eventnotes.ui.navigation.AppRoutes
import com.andro_sk.eventnotes.ui.state.EventState
import com.andro_sk.eventnotes.ui.viewmodels.EventsViewModel
import com.andro_sk.eventnotes.ui.viewmodels.UserSettingsViewModel

@Composable
fun HomeView(
    viewModel: EventsViewModel = hiltViewModel(),
    userSettingsViewModel: UserSettingsViewModel
) {
    val contentState by viewModel.contentState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchContent()
        userSettingsViewModel.getUserSettings()
    }
    when(val content = contentState) {
        EventState.Loading -> LoadingDialog()
        is EventState.Content -> {
                    HomeViewContent(
                        userSettingsViewModel = userSettingsViewModel,
                        events = content.events,
                        homeViewActions = { action ->
                            when (action) {
                                is HomeViewActions.OnNavigateToEventDetails -> {
                                    viewModel.navigateTo(
                                        route = AppRoutes.EVENT_DETAILS,
                                        eventId = action.eventId
                                    )
                                }

                                is HomeViewActions.RemoveEventItem -> {
                                    viewModel.deleteEventById(action.eventId)
                                }

                                is HomeViewActions.OnNavigateToAddEvent -> {
                                    viewModel.navigateTo(
                                        route = AppRoutes.ADD_EVENT,
                                        eventId = action.eventId
                                    )
                                }

                                is HomeViewActions.FilterEvents -> {
                                    viewModel.updateSortBy()
                                }
                            }
                        }
                    )

        }
        is EventState.Error -> {
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeViewContent(events: List<EventModel>, homeViewActions: (HomeViewActions) -> Unit, userSettingsViewModel: UserSettingsViewModel) {
    Scaffold(
        floatingActionButton = {
            GetFloatingButton(homeViewActions)
        },
        topBar = {
            GetAppBar(homeViewActions, userSettingsViewModel)
        }
    ) { innerPadding ->
        LazyColumn (modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(horizontal = 16.dp)) {
            items(items = events, key = {it.id}) { event ->
                EventCard(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .animateItem(),
                    event = event,
                    onEditEvent = {
                            homeViewActions.invoke(HomeViewActions.OnNavigateToEventDetails(event.id))
                                  },
                    onRemoveEvent = {
                        homeViewActions.invoke(HomeViewActions.RemoveEventItem(event.id))
                                    }
                )
            }
        }
    }
}

@Composable
private fun GetFloatingButton(homeViewActions: (HomeViewActions) -> Unit) {
    ExtendedFloatingActionButton(
        onClick = { homeViewActions.invoke(HomeViewActions.OnNavigateToAddEvent()) },
        icon = { Icon(Icons.Filled.AddCircle, contentDescription = null) },
        text = { Text(stringResource(R.string.add_event)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GetAppBar(
    homeViewActions: (HomeViewActions) -> Unit,
    userSettingsViewModel: UserSettingsViewModel
) {
    TopAppBar(
        title = { Text(stringResource(R.string.your_events)) },
        actions = {
            Row {
                IconButton(onClick = { homeViewActions.invoke(HomeViewActions.FilterEvents) }) {
                    Icon(
                        imageVector = Icons.Filled.FilterList,
                        contentDescription = "Filter"
                    )
                }
                IconButton(onClick = { homeViewActions.invoke(HomeViewActions.FilterEvents) }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search"
                    )
                }
                ThemeToggleButton(userSettingsViewModel)
            }
        }
    )
}

sealed class HomeViewActions {
    data class OnNavigateToAddEvent(val eventId: String = "") : HomeViewActions()
    data class OnNavigateToEventDetails(val eventId: String) : HomeViewActions()
    data class RemoveEventItem(val eventId: String) : HomeViewActions()
    object FilterEvents : HomeViewActions()
}
