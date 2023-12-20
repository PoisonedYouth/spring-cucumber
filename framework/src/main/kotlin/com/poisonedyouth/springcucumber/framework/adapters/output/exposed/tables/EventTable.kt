package com.poisonedyouth.springcucumber.framework.adapters.output.exposed.tables

import com.poisonedyouth.springcucumber.domain.event.vo.EventType
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.timestamp

private const val EVENT_TYPE_MAX_LENGTH = 10

object EventTable : UUIDTable("application_event") {
    val payload = text("payload")
    val type = enumerationByName("event_type", EVENT_TYPE_MAX_LENGTH, EventType::class)
    val created = timestamp("created")
}
