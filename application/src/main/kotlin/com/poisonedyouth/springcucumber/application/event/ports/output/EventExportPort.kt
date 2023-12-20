package com.poisonedyouth.springcucumber.application.event.ports.output

import com.poisonedyouth.springcucumber.domain.event.entity.Event
import java.util.stream.Stream

interface EventExportPort {
    fun storeEvents(fileExportPath: String)

    fun streamEvents(): Stream<Event>
}
