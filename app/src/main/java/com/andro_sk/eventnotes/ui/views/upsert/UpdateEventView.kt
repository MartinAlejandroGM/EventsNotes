package com.andro_sk.eventnotes.ui.views.upsert

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.andro_sk.eventnotes.ui.core.CustomAlertDialog
import com.andro_sk.eventnotes.ui.core.LoadingDialog
import com.andro_sk.eventnotes.ui.core.ManageEventContent
import com.andro_sk.eventnotes.ui.utils.AddUpdateDetailsEventViewActions
import com.andro_sk.eventnotes.ui.viewmodels.EventsViewModel

@Composable
fun UpdateEventView(
    eventId: String,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val eventTittle by remember { viewModel.eventName }
    val eventDate by remember { viewModel.eventDate }
    val photosUris = remember { viewModel.photosUris }
    val eventNotes = remember { viewModel.eventNotes }

    val eventSaved by viewModel.saveEvent.collectAsState()

    eventSaved.result?.let {
        viewModel.onBack()
    }

    eventSaved.error?.let { error ->
        CustomAlertDialog(
            confirmText = stringResource(error.confirmText),
            dismissText = error.dismissText?.let { stringResource(it) },
            title = error.titleResId?.let { stringResource(it) },
            text = error.messageResId?.let { stringResource(it) },
            onDismiss = { error.onDismiss.invoke() },
            onConfirm = { error.onConfirm.invoke() },
        )
    }

    if (eventSaved.isLoading) {
        LoadingDialog()
    }

    val event by viewModel.event.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.fetchEventById(eventId)
    }
    event.event?.let {
        LaunchedEffect(Unit) {
            viewModel.onEventTittleChanged(it.eventTittle)
            viewModel.onEventDateChanged(it.date)
            viewModel.onAddPhotos(it.eventPhotos.map { photo ->
                photo.uri
            })
            viewModel.setAllNotes(it.eventNotes)
            viewModel.setEventId(it.id)
        }
        ManageEventContent(
            eventTittle = eventTittle,
            eventId = it.id,
            eventDate = eventDate,
            photos = photosUris,
            notes = eventNotes,
            addUpdateDetailsEventViewActions = { action ->
                when(action) {
                    is AddUpdateDetailsEventViewActions.OnBack -> viewModel.onBack()
                    is AddUpdateDetailsEventViewActions.OnSave -> viewModel.onUpdateEvent()
                    is AddUpdateDetailsEventViewActions.OnWriteName ->  viewModel.onEventTittleChanged(action.name)
                    is AddUpdateDetailsEventViewActions.OnRemovePhoto -> viewModel.onRemovePhoto(action.photo)
                    is AddUpdateDetailsEventViewActions.OnSelectDate ->  viewModel.onEventDateChanged(action.selectedDate)
                    is AddUpdateDetailsEventViewActions.OnSelectCoverPhoto -> viewModel.onAddPhotos(action.uris)
                    is AddUpdateDetailsEventViewActions.OnUpdateNotes -> viewModel.onUpdateEventNote(action.note)
                    is AddUpdateDetailsEventViewActions.OnAddNote -> viewModel.onAddEventNote(action.note)
                    is AddUpdateDetailsEventViewActions.OnRemoveNote -> viewModel.onRemoveEventNote(action.id)
                }
            })
    }

    event.error?.let { error ->
        CustomAlertDialog(
            confirmText = stringResource(error.confirmText),
            dismissText = error.dismissText?.let { stringResource(it) },
            title = error.titleResId?.let { stringResource(it) },
            text = error.messageResId?.let { stringResource(it) },
            onDismiss = { error.onDismiss.invoke() },
            onConfirm = { error.onConfirm.invoke() },
        )
    }

    if (event.isLoading) {
        LoadingDialog()
    }
}