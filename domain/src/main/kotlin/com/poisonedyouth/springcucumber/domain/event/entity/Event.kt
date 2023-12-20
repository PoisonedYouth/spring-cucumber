package com.poisonedyouth.springcucumber.domain.event.entity

import com.poisonedyouth.springcucumber.domain.common.vo.Identity
import com.poisonedyouth.springcucumber.domain.event.vo.EventType
import java.time.Instant

data class Event(
    val identity: Identity,
    val payload: String,
    val eventType: EventType,
    val created: Instant
)
