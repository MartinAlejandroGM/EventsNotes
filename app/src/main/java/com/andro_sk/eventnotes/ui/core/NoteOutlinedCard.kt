package com.andro_sk.eventnotes.ui.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DashboardCustomize
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andro_sk.eventnotes.R
import com.andro_sk.eventnotes.domain.models.EventNote
import com.andro_sk.eventnotes.ui.extension.generateRandomUUID

@Composable
fun NoteOutlinedCard(
    notes: List<EventNote>,
    onAddNote: (EventNote) -> Unit,
    onChangeNoteText: (EventNote) -> Unit,
    onRemoveNote: (noteId: String) -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
        ) {
            NotesTop{ noteAdded ->
                onAddNote.invoke(noteAdded)
            }
            notes.forEachIndexed { index, note ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CustomOutlinedTextField(
                        value = note.description,
                        label = "Nota $index",
                        singleLine = false,
                        modifier = Modifier.weight(1f),
                        onValueChange = {
                            onChangeNoteText.invoke(EventNote(id = note.id, description = it))
                        },
                    )
                    IconButton(
                        onClick = {
                            onRemoveNote.invoke(note.id)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Filter"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NotesTop(onAddNote: (EventNote) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.event_notes))
        IconButton(
            onClick = { onAddNote.invoke(EventNote(generateRandomUUID(), "")) },
        ) {
            Icon(
                imageVector = Icons.Filled.DashboardCustomize,
                contentDescription = "Filter",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteOutlinedCardPreview() {
    NoteOutlinedCard(
        notes = arrayListOf(),
        onAddNote = {},
        onRemoveNote = {},
        onChangeNoteText = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun NoteOutlinedCardOneNotePreview() {
    NoteOutlinedCard(
        notes = arrayListOf(
            EventNote(
                id = "",
                description = "Note Fake"
            )
        ),
        onAddNote = {},
        onRemoveNote = {},
        onChangeNoteText = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun NoteOutlinedCardTwoNotePreview() {
    NoteOutlinedCard(
        notes = arrayListOf(
            EventNote(
                id = "",
                description = "Note Fake"
            ),
            EventNote(
                id = "",
                description = "Note Fake 2"
            )
        ),
        onAddNote = {},
        onRemoveNote = {},
        onChangeNoteText = {}
    )
}