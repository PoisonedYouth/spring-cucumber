package com.poisonedyouth.springcucumber.application.event.ports.input

import com.poisonedyouth.springcucumber.domain.common.vo.Identity
import com.poisonedyouth.springcucumber.domain.event.entity.Event
import com.poisonedyouth.springcucumber.domain.event.vo.EventType
import java.time.Instant
import java.util.*

data class EventDto(
    val identity: UUID,
    val payload: String,
    val eventType: EventType,
    val created: Instant
)

fun EventDto.toEvent() =
    Event(
        identity = Identity.UUIDIdentity(this.identity),
        payload = this.payload,
        eventType = this.eventType,
        created = created
    )

fun Event.toEventDto() =
    EventDto(
        identity = this.identity.getId(),
        payload = this.payload,
        eventType = this.eventType,
        created = this.created
    )
