package com.poisonedyouth.springcucumber.framework.adapters.output.exposed

import com.poisonedyouth.springcucumber.application.event.ports.output.EventOutputPort
import com.poisonedyouth.springcucumber.domain.common.vo.Identity
import com.poisonedyouth.springcucumber.domain.event.entity.Event
import com.poisonedyouth.springcucumber.framework.adapters.output.exposed.tables.EventTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class ExposedEventRepository : EventOutputPort {
    override fun storeEvent(event: Event) = transaction {
        EventTable.insert {
            it[id] = event.identity.getId()
            it[payload] = event.payload
            it[type] = event.eventType
            it[created] = event.created
        }
        Unit
    }

    override fun all(): List<Event> = transaction {
        EventTable.selectAll().map {
            Event(
                identity = Identity.UUIDIdentity(it[EventTable.id].value),
                payload = it[EventTable.payload],
                eventType = it[EventTable.type],
                created = it[EventTable.created]
            )
        }
    }
}
