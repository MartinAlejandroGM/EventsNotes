package com.andro_sk.eventnotes.ui.utils

import android.net.Uri
import com.andro_sk.eventnotes.data.local.database.entities.EventsEntity
import com.andro_sk.eventnotes.data.local.database.entities.NotesEntity
import com.andro_sk.eventnotes.data.local.database.entities.PhotoEntity
import com.andro_sk.eventnotes.domain.models.EventModel
import com.andro_sk.eventnotes.domain.models.EventPhoto
import androidx.core.net.toUri
import com.andro_sk.eventnotes.data.local.database.pojo.EventWithDetails
import com.andro_sk.eventnotes.domain.models.EventNote

fun EventWithDetails.getEventModelByDaos(): EventModel =
    EventModel(
        id = this.event.id,
        eventTittle = this.event.eventTittle,
        date = this.event.eventDate,
        imageUrl = this.photos.firstOrNull()?.uri?.toUri() ?: Uri.EMPTY,
        eventNotes = this.notes.map {
            EventNote(
                id = it.id,
                description = it.notes
            )
        },
        eventPhotos = this.photos.map {
            EventPhoto(
                id = it.id,
                uri = it.uri.toUri()
            )
        }
    )

fun List<EventWithDetails>.getEventsModelByDaos(): List<EventModel> =
    this.map {
        it.getEventModelByDaos()
    }

fun EventModel.toEventEntity(): EventsEntity {
    return EventsEntity(
        id = this.id,
        eventTittle = this.eventTittle,
        eventDate = this.date
    )
}

fun EventModel.toNotesEntity(): List<NotesEntity> {
    return this.eventNotes.map {
        NotesEntity(
            eventId = this.id,
            id = it.id,
            notes = it.description
        )
    }
}

fun EventModel.toPhotosEntity(): List<PhotoEntity> {
    return this.eventPhotos.map {
        PhotoEntity(
            eventId = this.id,
            id = it.id,
            uri = it.uri.toString()
        )
    }
}