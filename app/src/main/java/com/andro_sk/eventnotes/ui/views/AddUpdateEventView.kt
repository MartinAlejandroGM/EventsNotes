package com.andro_sk.eventnotes.ui.views

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.andro_sk.eventnotes.R
import com.andro_sk.eventnotes.domain.models.EventModel
import com.andro_sk.eventnotes.ui.core.CustomAlertDialog
import com.andro_sk.eventnotes.ui.viewmodels.EventsViewModel
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.DashboardCustomize
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.andro_sk.eventnotes.ui.core.CustomOutlinedTextField
import com.andro_sk.eventnotes.ui.core.DatePickerField
import com.andro_sk.eventnotes.ui.core.EventPhotoRow
import com.andro_sk.eventnotes.ui.core.LoadingDialog

@Composable
fun AddUpdateEventView(
    eventId: String? = null,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val eventTittle by remember { viewModel.eventName }
    val eventDate by remember { viewModel.eventDate }
    val photosUris = remember { viewModel.photosUris }
    val eventNotes = remember { viewModel.eventNotes }

    if (!eventId.isNullOrBlank()) {
        val event by viewModel.event.collectAsState()
        LaunchedEffect(eventId) {
            viewModel.fetchEventById(eventId)
        }
        event.event?.let {
            LaunchedEffect(Unit) {
                viewModel.onEventTittleChanged(it.eventTittle)
                viewModel.onEventDateChanged(it.date)
            }
            AddUpdateDetailsEventContent(
                eventTittle = eventTittle,
                event = it, eventDate = eventDate,
                photoUris = photosUris,
                notes = eventNotes,
                addUpdateDetailsEventViewActions = { actions ->
                when(actions) {
                    is AddUpdateDetailsEventViewActions.OnBack -> {}
                    is AddUpdateDetailsEventViewActions.OnSave -> { viewModel }
                    is AddUpdateDetailsEventViewActions.OnWriteName -> { viewModel.onEventTittleChanged(actions.name) }
                    is AddUpdateDetailsEventViewActions.OnRemovePhoto -> {}
                    is AddUpdateDetailsEventViewActions.OnSelectDate -> { viewModel.onEventDateChanged(actions.selectedDate) }
                    is AddUpdateDetailsEventViewActions.OnSelectCoverPhoto -> {}
                    is AddUpdateDetailsEventViewActions.OnWriteNotes -> {}
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
    } else {
        AddUpdateDetailsEventContent(eventTittle = eventTittle,
            eventDate = eventDate,
            photoUris = photosUris,
            notes = eventNotes,
            addUpdateDetailsEventViewActions = { actions ->
            when(actions) {
                is AddUpdateDetailsEventViewActions.OnBack -> {}
                is AddUpdateDetailsEventViewActions.OnSave -> {}
                is AddUpdateDetailsEventViewActions.OnWriteName -> { viewModel.onEventTittleChanged(actions.name) }
                is AddUpdateDetailsEventViewActions.OnRemovePhoto -> {}
                is AddUpdateDetailsEventViewActions.OnSelectDate -> { viewModel.onEventDateChanged(actions.selectedDate) }
                is AddUpdateDetailsEventViewActions.OnSelectCoverPhoto -> {}
                is AddUpdateDetailsEventViewActions.OnWriteNotes -> {}
            }
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUpdateDetailsEventContent(
    eventTittle: String,
    eventDate: String,
    photoUris: Set<Uri?>,
    notes: List<String>,
    event: EventModel = EventModel(),
    addUpdateDetailsEventViewActions: (AddUpdateDetailsEventViewActions) -> Unit
) {
    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            if (uris.isNotEmpty()) {
                addUpdateDetailsEventViewActions.invoke(AddUpdateDetailsEventViewActions.OnSelectCoverPhoto(uris))
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier,
                title = { Text(stringResource(if (event.id == null) R.string.create_event else R.string.update_event)) },
                navigationIcon = {
                    IconButton(
                        onClick = {  }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            EventTittle(eventTittle) { value ->
                addUpdateDetailsEventViewActions.invoke(AddUpdateDetailsEventViewActions.OnWriteNotes(value))
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                DatePickerField(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.event_date),
                    value = eventDate,
                    onDateSelected = { value ->
                        addUpdateDetailsEventViewActions.invoke(AddUpdateDetailsEventViewActions.OnSelectDate(value))
                    }
                )
            }
            Spacer(Modifier.height(12.dp))
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
            ) {
                Column(
                    modifier = Modifier
                        .padding(12.dp),
                ) {
                    Button(
                        onClick = { pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                Icons.Filled.Photo,
                                contentDescription = null,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(R.string.event_photos))
                        }
                    }
                    event.eventPhotos.urls.forEach { uri ->
                        EventPhotoRow(
                            uri,
                            onRemovePhoto = {
                                addUpdateDetailsEventViewActions.invoke(
                                    AddUpdateDetailsEventViewActions.OnRemovePhoto(
                                        it
                                    )
                                )
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
            ) {
                Column(
                    modifier = Modifier
                        .padding(12.dp),
                ) {
                    NotesTop()
                    notes.forEachIndexed { index, note ->
                        CustomOutlinedTextField(
                            value = note,
                            label = "Nota $index",
                            singleLine = false,
                            onValueChange = {
                                addUpdateDetailsEventViewActions.invoke(
                                    AddUpdateDetailsEventViewActions.OnWriteNotes(
                                        it
                                    )
                                )
                            },
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilledTonalButton(
                        modifier = Modifier.weight(1f),
                        onClick = { addUpdateDetailsEventViewActions.invoke(
                            AddUpdateDetailsEventViewActions.OnBack) },
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            addUpdateDetailsEventViewActions.invoke(AddUpdateDetailsEventViewActions.OnSave)
                        },
                    ) {
                        Text(stringResource(R.string.save_event))
                    }
                }
            }
        }
    }
}

sealed class AddUpdateDetailsEventViewActions() {
    object OnBack : AddUpdateDetailsEventViewActions()
    data class OnWriteName(val name: String) : AddUpdateDetailsEventViewActions()
    data class OnSelectDate(val selectedDate: String) : AddUpdateDetailsEventViewActions()
    data class OnWriteNotes(val notes: String) : AddUpdateDetailsEventViewActions()
    data class OnSelectCoverPhoto(val uris: List<Uri>) : AddUpdateDetailsEventViewActions()
    data class OnRemovePhoto(val uri: Uri) : AddUpdateDetailsEventViewActions()
    object OnSave : AddUpdateDetailsEventViewActions()
}

@Composable
fun NotesTop() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.event_notes))
        IconButton(
            onClick = { /* Acción del icono */ },
        ) {
            Icon(
                imageVector = Icons.Filled.DashboardCustomize,
                contentDescription = "Filter",
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AddUpdateDetailsEventViewPreview() {
    AddUpdateDetailsEventContent(
        eventTittle = "",
        eventDate = "",
        photoUris = setOf(),
        notes = arrayListOf(),
        addUpdateDetailsEventViewActions = { actions ->
        when(actions) {
            is AddUpdateDetailsEventViewActions.OnBack -> {}
            is AddUpdateDetailsEventViewActions.OnSave -> {}
            is AddUpdateDetailsEventViewActions.OnWriteName -> {}
            is AddUpdateDetailsEventViewActions.OnRemovePhoto -> {}
            is AddUpdateDetailsEventViewActions.OnSelectDate -> {}
            is AddUpdateDetailsEventViewActions.OnSelectCoverPhoto -> {}
            is AddUpdateDetailsEventViewActions.OnWriteNotes -> {}
        }
    })
}