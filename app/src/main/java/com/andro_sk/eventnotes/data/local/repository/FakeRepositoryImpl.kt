package com.andro_sk.eventnotes.data.local.repository

import com.andro_sk.eventnotes.domain.contracts.EventsRepository
import com.andro_sk.eventnotes.domain.models.EventModel
import com.andro_sk.eventnotes.domain.models.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeRepositoryImpl @Inject constructor() : EventsRepository {
    private val eventsList =
        mutableListOf(
            EventModel(
                id = "1",
                eventTittle = "Mario's Party",
                description = "Its Mario's Party",
                imageUrl = "https://static.wikia.nocookie.net/mario/images/6/6d/Plano_PCP.png/revision/latest?cb=20110928233126&path-prefix=es",
                date = "09/02/2025"
            ),
            EventModel(
                id = "2",
                eventTittle = "Luigi's Party",
                description = "Its Luigi's Party",
                imageUrl = "https://static.wikia.nocookie.net/mario/images/6/6d/Plano_PCP.png/revision/latest?cb=20110928233126&path-prefix=es",
                date = "10/12/2025"
            ),
            EventModel(
                id = "3",
                eventTittle = "Peach's Party",
                description = "Its Peach's Party",
                imageUrl = "https://static.wikia.nocookie.net/mario/images/6/6d/Plano_PCP.png/revision/latest?cb=20110928233126&path-prefix=es",
                date = "09/05/2025"
            ),
            EventModel(
                id = "4",
                eventTittle = "Daisy's Party",
                description = "Its Daisy's Party",
                imageUrl = "https://static.wikia.nocookie.net/mario/images/6/6d/Plano_PCP.png/revision/latest?cb=20110928233126&path-prefix=es",
                date = "09/07/2025"
            ),
            EventModel(
                id = "5",
                eventTittle = "Rosalina's Party",
                description = "Its Rosalina's Party",
                imageUrl = "https://static.wikia.nocookie.net/mario/images/6/6d/Plano_PCP.png/revision/latest?cb=20110928233126&path-prefix=es",
                date = "10/01/2026"
            )
        )
    private var index = 5
    override suspend fun fetchEventById(eventId: String): Response<EventModel> {
        return Response.Success(eventsList.firstOrNull { it.id == eventId } ?: EventModel())
    }

    override suspend fun updateEventById(event: EventModel): Response<Long> {
        val indexToChange = eventsList.indexOfFirst { it.id == event.id }
        eventsList[indexToChange] = event
        return Response.Success(1)
    }

    override suspend fun deleteEventById(eventId: String): Response<Unit> {
        eventsList.removeIf { it.id == eventId }
        return Response.Success(Unit)
    }

    override suspend fun fetchEvents(): Response<List<EventModel>> {
        return Response.Success( eventsList)
    }

    override suspend fun addEvent(event: EventModel): Response<Long> {
        eventsList.add(event.copy(id = (++index).toString()))
        return Response.Success(1)
    }
}