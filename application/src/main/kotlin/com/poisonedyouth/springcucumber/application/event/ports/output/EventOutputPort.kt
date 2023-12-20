package com.poisonedyouth.springcucumber.application.event.ports.output

import com.poisonedyouth.springcucumber.domain.event.entity.Event

interface EventOutputPort {
    fun storeEvent(event: Event)

    fun all(): List<Event>
}
