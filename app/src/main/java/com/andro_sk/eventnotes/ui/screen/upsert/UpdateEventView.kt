package com.andro_sk.eventnotes.ui.screen.upsert

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andro_sk.eventnotes.domain.models.EventModel
import com.andro_sk.eventnotes.domain.models.EventNote
import com.andro_sk.eventnotes.domain.models.EventPhoto
import com.andro_sk.eventnotes.ui.core.LoadingDialog
import com.andro_sk.eventnotes.ui.core.ManageEventContent
import com.andro_sk.eventnotes.ui.state.EventDetailsState
import com.andro_sk.eventnotes.ui.extension.toEventPhotosList
import com.andro_sk.eventnotes.ui.viewmodels.UpsertViewModel

@Composable
fun UpdateEventView(
    eventId: String,
    viewModel: UpsertViewModel = hiltViewModel()
) {

    var tittle by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    val photos = remember { mutableStateListOf<EventPhoto>() }
    val notes = remember { mutableStateListOf<EventNote>() }

    val contentState by viewModel.contentState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.fetchEventById(eventId)
    }
    when(val content = contentState) {
        is EventDetailsState.Loading -> {
            LoadingDialog()
        }
        is EventDetailsState.Content -> {
            LaunchedEffect(Unit) {
                tittle = content.event.eventTittle
                date = content.event.date
                photos.clear()
                photos.addAll(content.event.eventPhotos)
                notes.clear()
                notes.addAll(content.event.eventNotes)
            }
            ManageEventContent(
                eventTittle = tittle,
                eventId = content.event.id,
                eventDate = date,
                photos = photos,
                notes = notes,
                addUpdateDetailsEventViewActions = { action ->
                    when(action) {
                        is AddUpdateDetailsEventViewActions.OnBack -> viewModel.onBack()
                        is AddUpdateDetailsEventViewActions.OnSave -> viewModel.onUpsertEvent(
                            EventModel(content.event.id, eventTittle = tittle, imageUrl = photos.firstOrNull()?.uri?: Uri.EMPTY,
                                date = date, eventPhotos = photos, eventNotes = notes)
                        )
                        is AddUpdateDetailsEventViewActions.OnWriteName ->  tittle = action.name
                        is AddUpdateDetailsEventViewActions.OnRemovePhoto -> photos.remove(action.photo)
                        is AddUpdateDetailsEventViewActions.OnSelectDate ->  date = action.selectedDate
                        is AddUpdateDetailsEventViewActions.OnSelectCoverPhoto -> {
                            val photosToAdd = action.uris.toEventPhotosList(photos.map { it.uri })
                            photos.addAll(photosToAdd)
                        }
                        is AddUpdateDetailsEventViewActions.OnUpdateNotes -> notes[notes.indexOfFirst { it.id == action.note.id }] = action.note
                        is AddUpdateDetailsEventViewActions.OnAddNote -> notes.add(action.note)
                        is AddUpdateDetailsEventViewActions.OnRemoveNote -> notes.removeAt(notes.indexOfFirst { it.id == action.id })
                    }
                })
        }
        is EventDetailsState.UpsertEvent -> {
            viewModel.onBack()
        }
        is EventDetailsState.Error -> {

        }
    }
}