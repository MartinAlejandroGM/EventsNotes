package com.andro_sk.eventnotes.ui.views

import android.net.Uri
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andro_sk.eventnotes.R
import com.andro_sk.eventnotes.domain.models.EventModel
import com.andro_sk.eventnotes.ui.core.CustomAlertDialog
import com.andro_sk.eventnotes.ui.core.EventCard
import com.andro_sk.eventnotes.ui.core.LoadingDialog
import com.andro_sk.eventnotes.ui.navigation.AppRoutes
import com.andro_sk.eventnotes.ui.viewmodels.EventsViewModel

@Composable
fun HomeView(
    viewModel: EventsViewModel = hiltViewModel()
) {
    val events by viewModel.events.collectAsState()
    var filteredBy by remember { mutableStateOf("date") }

    LaunchedEffect(filteredBy) {
        viewModel.fetchEvents(filteredBy)
    }

    events.events?.let {
        HomeViewContent(
            events = it,
            homeViewActions = { action ->
                when (action) {
                    is HomeViewActions.OnNavigateToEventDetails -> {
                        viewModel.navigateTo(
                            route = AppRoutes.EVENT_DETAILS,
                            eventId = action.eventId,
                            filterBy = filteredBy
                        )
                    }
                    is HomeViewActions.RemoveEventItem -> {
                        viewModel.deleteEventById(action.eventId, filteredBy)
                    }
                    is HomeViewActions.OnNavigateToAddEvent -> {
                        viewModel.navigateTo(
                            route = AppRoutes.ADD_EVENT,
                            eventId = action.eventId,
                            filterBy = filteredBy
                        )
                    }
                    is HomeViewActions.FilterEvents -> {
                        if (filteredBy == "date")
                            filteredBy = "name"
                        else filteredBy = "date"
                    }
                }
            }
        )
    }

    events.error?.let { error ->
        CustomAlertDialog(
            confirmText = stringResource(error.confirmText),
            dismissText = error.dismissText?.let { stringResource(it) },
            title = error.titleResId?.let { stringResource(it) },
            text = error.messageResId?.let { stringResource(it) },
            onDismiss = { error.onDismiss.invoke() },
            onConfirm = { error.onConfirm.invoke() },
        )
    }

    if (events.isLoading) {
        LoadingDialog()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeViewContent(events: List<EventModel>, homeViewActions: (HomeViewActions) -> Unit) {
    Scaffold(
        floatingActionButton = {
            GetFloatingButton(homeViewActions)
        },
        topBar = {
            GetAppBar(homeViewActions)
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
                    onEditEvent =
                        {
                            homeViewActions.invoke(HomeViewActions.OnNavigateToEventDetails(event.id))
                        },
                    onRemoveEvent = {
                        homeViewActions.invoke(HomeViewActions.RemoveEventItem(event.id))
                    },
                    onNavigateToEventDetails = {
                        homeViewActions.invoke(HomeViewActions.OnNavigateToEventDetails(event.id))
                    })
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
    homeViewActions: (HomeViewActions) -> Unit
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

@Preview
@Composable
fun HomePreview() {
    HomeViewContent(
        events = listOf(
            EventModel(
                id = "1",
                eventTittle = "Mario's Party",
                description = "Its Mario's Party",
                imageUrl = Uri.EMPTY
            ),
            EventModel(
                id = "2",
                eventTittle = "Luigi's Party",
                description = "Its Luigi's Party",
                imageUrl = Uri.EMPTY
            ),
            EventModel(
                id = "3",
                eventTittle = "Peach's Party",
                description = "Its Peach's Party",
                imageUrl = Uri.EMPTY
            ),
            EventModel(
                id = "4",
                eventTittle = "Daisy's Party",
                description = "Its Daisy's Party",
                imageUrl = Uri.EMPTY
            ),
            EventModel(
                id = "5",
                eventTittle = "Rosalina's Party",
                description = "Its Rosalina's Party",
                imageUrl = Uri.EMPTY
            )
        )
    ) {
        when (it) {
            is HomeViewActions.OnNavigateToEventDetails -> {}
            is HomeViewActions.RemoveEventItem -> {}
            is HomeViewActions.OnNavigateToAddEvent -> {}
            is HomeViewActions.FilterEvents -> {}
        }
    }
}
