package com.poisonedyouth.springcucumber.application.event.ports.input

import com.poisonedyouth.springcucumber.application.event.ports.output.EventOutputPort
import com.poisonedyouth.springcucumber.application.event.usecases.EventUseCase

class UserEventInputPort(private val eventOutputPort: EventOutputPort) : EventUseCase {
    override fun storeEvent(event: EventDto) {
        eventOutputPort.storeEvent(event.toEvent())
    }

    override fun getEvents(): List<EventDto> {
        return eventOutputPort.all().map { it.toEventDto() }
    }
}
