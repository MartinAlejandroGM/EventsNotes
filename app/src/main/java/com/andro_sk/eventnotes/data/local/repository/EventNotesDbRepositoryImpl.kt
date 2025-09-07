package com.andro_sk.eventnotes.data.local.repository

import com.andro_sk.eventnotes.data.local.database.daos.EventsDao
import com.andro_sk.eventnotes.data.local.database.daos.EventsPhotosDao
import com.andro_sk.eventnotes.data.local.database.daos.NotesDao
import com.andro_sk.eventnotes.domain.contracts.EventsRepository
import com.andro_sk.eventnotes.domain.models.EventModel
import com.andro_sk.eventnotes.domain.models.Response
import com.andro_sk.eventnotes.ui.utils.getEventModelByDaos
import com.andro_sk.eventnotes.ui.utils.getEventsModelByDaos
import com.andro_sk.eventnotes.ui.utils.toEventEntity
import com.andro_sk.eventnotes.ui.utils.toNotesEntity
import com.andro_sk.eventnotes.ui.utils.toPhotosEntity
import javax.inject.Inject

class EventNotesDbRepositoryImpl @Inject constructor(
    private val eventsDao: EventsDao,
    private val notesDao: NotesDao,
    private val photosDao: EventsPhotosDao
) : EventsRepository {
    override suspend fun fetchEventById(eventId: String): Response<EventModel> =
        try {
            val eventResponse = eventsDao.fetchEventById(eventId)
            eventResponse.firstOrNull()?.let {
                Response.Success(it.getEventModelByDaos())
            }?: run {
                Response.Error(Throwable(message = "Error Getting the Event!!"))
            }
        } catch(exception: Exception) {
            Response.Error(exception)
        }

    override suspend fun upsertEvent(event: EventModel): Response<Long> =
         try {
            val eventEntity = event.toEventEntity()
            val result = eventsDao.upsertEvent(eventEntity)

            if (result == -1L) {
                notesDao.upsertNotes(event.toNotesEntity())
                photosDao.upsertPhoto(event.toPhotosEntity())

                notesDao.deleteNotes(event.eventNotes.map { it.id }, event.id)
                photosDao.deletePhotoByEventId(event.eventPhotos.map { it.id }, event.id)

            } else {
                notesDao.upsertNotes(event.toNotesEntity())
                photosDao.upsertPhoto(event.toPhotosEntity())
            }
            Response.Success(1L)
        } catch (exception: Exception) {
            Response.Error(exception)
        }

    override suspend fun deleteEventById(eventId: String): Response<Unit>  =
        try {
            eventsDao.deleteEventById(eventId)
            Response.Success(Unit)
        } catch (exception: Exception) {
            Response.Error(exception)
        }


    override suspend fun fetchEvents(): Response<List<EventModel>> =
        try {
            val eventsResponse = eventsDao.fetchEvents()
            Response.Success(eventsResponse.getEventsModelByDaos())
        } catch (exception: Exception) {
            Response.Error(exception)
        }
}