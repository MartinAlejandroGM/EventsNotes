package com.andro_sk.eventnotes.ui.core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andro_sk.eventnotes.R
import com.andro_sk.eventnotes.domain.models.EventNote
import com.andro_sk.eventnotes.domain.models.EventPhoto
import com.andro_sk.eventnotes.ui.utils.AddUpdateDetailsEventViewActions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageEventContent(
    eventTittle: String,
    eventDate: String,
    photos: List<EventPhoto>,
    eventId: String = "",
    notes: List<EventNote>,
    addUpdateDetailsEventViewActions: (AddUpdateDetailsEventViewActions) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier,
                title = { Text(stringResource(if (eventId.isBlank()) R.string.create_event else R.string.update_event)) },
                navigationIcon = {
                    IconButton(
                        onClick = { addUpdateDetailsEventViewActions.invoke(
                            AddUpdateDetailsEventViewActions.OnBack) }
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
                addUpdateDetailsEventViewActions.invoke(
                    AddUpdateDetailsEventViewActions.OnWriteName(
                        value
                    )
                )
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
            PickMedia(photos, onAddPhotos = {
                addUpdateDetailsEventViewActions.invoke(AddUpdateDetailsEventViewActions.OnSelectCoverPhoto(it))
            },  onRemovePhoto = {
                addUpdateDetailsEventViewActions.invoke(AddUpdateDetailsEventViewActions.OnRemovePhoto(it))
            })
            Spacer(Modifier.height(8.dp))
            NoteOutlinedCard(notes = notes, onAddNote = {
                addUpdateDetailsEventViewActions.invoke(AddUpdateDetailsEventViewActions.OnAddNote(it))
            }, onRemoveNote = {
                addUpdateDetailsEventViewActions.invoke(
                    AddUpdateDetailsEventViewActions.OnRemoveNote(it))
            }, onChangeNoteText = {
                addUpdateDetailsEventViewActions.invoke(
                    AddUpdateDetailsEventViewActions.OnUpdateNotes(
                        EventNote(id = it.id, description = it.description)
                    )
                )
            })
            Spacer(Modifier.height(12.dp))
            CancelSaveButtons(onSaveClick = {
                addUpdateDetailsEventViewActions.invoke(AddUpdateDetailsEventViewActions.OnSave)
            }, onCancelClick = {
                addUpdateDetailsEventViewActions.invoke(
                    AddUpdateDetailsEventViewActions.OnBack)
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddEventViewPreview() {
    ManageEventContent(
        eventTittle = "",
        eventDate = "",
        photos = SnapshotStateList(),
        notes = SnapshotStateList(),
        addUpdateDetailsEventViewActions = { actions ->
            when(actions) {
                is AddUpdateDetailsEventViewActions.OnBack -> {}
                is AddUpdateDetailsEventViewActions.OnSave -> {}
                is AddUpdateDetailsEventViewActions.OnWriteName -> {}
                is AddUpdateDetailsEventViewActions.OnRemovePhoto -> {}
                is AddUpdateDetailsEventViewActions.OnSelectDate -> {}
                is AddUpdateDetailsEventViewActions.OnSelectCoverPhoto -> {}
                is AddUpdateDetailsEventViewActions.OnUpdateNotes -> {}
                is AddUpdateDetailsEventViewActions.OnAddNote -> {}
                is AddUpdateDetailsEventViewActions.OnRemoveNote -> {}
            }
        })
}