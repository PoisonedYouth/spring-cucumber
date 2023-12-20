package com.poisonedyouth.springcucumber.framework.adapters.output.file

import com.poisonedyouth.springcucumber.application.event.ports.output.EventExportPort
import com.poisonedyouth.springcucumber.application.event.ports.output.EventOutputPort
import com.poisonedyouth.springcucumber.domain.event.entity.Event
import java.nio.file.Paths
import java.util.*
import java.util.stream.Stream
import kotlin.io.path.exists
import kotlin.io.path.writeLines

class FileEventExport(private val eventOutputPort: EventOutputPort) : EventExportPort {
    override fun storeEvents(fileExportPath: String) {
        val exportPath = Paths.get(".").resolve(fileExportPath)
        if (exportPath.exists()) {
            error("Export path '$exportPath' already exist.")
        }
        val events = eventOutputPort.all().map { it.toString() }
        exportPath.writeLines(events)
    }

    override fun streamEvents(): Stream<Event> {
        return Arrays.stream(eventOutputPort.all().toTypedArray())
    }
}
