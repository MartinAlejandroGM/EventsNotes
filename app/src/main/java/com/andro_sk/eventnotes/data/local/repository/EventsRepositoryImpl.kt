package com.andro_sk.eventnotes.data.local.repository

import com.andro_sk.eventnotes.domain.contracts.EventsRepository
import com.andro_sk.eventnotes.domain.models.EventModel
import com.andro_sk.eventnotes.domain.models.Response
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(

) : EventsRepository {
    private val eventsList: ArrayList<EventModel> =
        arrayListOf(
            EventModel(
                id = "1",
                eventTittle = "Mario's Party",
                description = "Its Mario's Party",
                imageUrl = "https://static.wikia.nocookie.net/mario/images/6/6d/Plano_PCP.png/revision/latest?cb=20110928233126&path-prefix=es",
                date = "01/09/2025"
            ),
            EventModel(
                id = "2",
                eventTittle = "Luigi's Party",
                description = "Its Luigi's Party",
                imageUrl = "https://static.wikia.nocookie.net/mario/images/6/6d/Plano_PCP.png/revision/latest?cb=20110928233126&path-prefix=es",
                date = "01/10/2025"
            ),
            EventModel(
                id = "3",
                eventTittle = "Peach's Party",
                description = "Its Peach's Party",
                imageUrl = "https://static.wikia.nocookie.net/mario/images/6/6d/Plano_PCP.png/revision/latest?cb=20110928233126&path-prefix=es",
                date = "05/09/2025"
            ),
            EventModel(
                id = "4",
                eventTittle = "Daisy's Party",
                description = "Its Daisy's Party",
                imageUrl = "https://static.wikia.nocookie.net/mario/images/6/6d/Plano_PCP.png/revision/latest?cb=20110928233126&path-prefix=es",
                date = "07/09/2025"
            ),
            EventModel(
                id = "5",
                eventTittle = "Rosalina's Party",
                description = "Its Rosalina's Party",
                imageUrl = "https://static.wikia.nocookie.net/mario/images/6/6d/Plano_PCP.png/revision/latest?cb=20110928233126&path-prefix=es",
                date = "01/10/2026"
            )
        )
    override suspend fun fetchEventById(eventId: String): Response<EventModel> {
        return Response.Success(eventsList.firstOrNull { it.id == eventId } ?: EventModel())
    }

    override suspend fun updateEventById(eventId: String): Response<EventModel> {
        return Response.Success(eventsList.firstOrNull {it.id == eventId} ?: EventModel())
    }

    override suspend fun deleteEventById(eventId: String): Response<Unit> {
        eventsList.removeIf { it.id == eventId }
        return Response.Success(Unit)
    }

    override suspend fun fetchEvents(): Response<List<EventModel>> {
        return Response.Success( eventsList)
    }

    override suspend fun addEvent(event: EventModel): Response<Unit> {
        eventsList.add(event)
        return Response.Success(Unit)
    }
}