package com.poisonedyouth.springcucumber.application.event.usecases

import com.poisonedyouth.springcucumber.application.event.ports.input.EventDto

interface EventUseCase {
    fun storeEvent(event: EventDto)

    fun getEvents(): List<EventDto>
}
