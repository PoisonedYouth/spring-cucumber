package com.poisonedyouth.springcucumber.framework.adapters.input.rest

import com.poisonedyouth.springcucumber.application.event.ports.output.EventExportPort
import com.poisonedyouth.springcucumber.application.event.usecases.EventUseCase
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody

@RestController
@RequestMapping("/events")
class EventRestAdapter(
    private val eventUseCase: EventUseCase,
    private val eventExportPort: EventExportPort
) {

    @GetMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun getEvents(): ResponseEntity<*> {
        return ResponseEntity.ok(eventUseCase.getEvents())
    }

    @GetMapping("/export")
    fun exportEvents(@RequestParam filename: String): ResponseEntity<*> {
        eventExportPort.storeEvents(filename)
        return ResponseEntity.accepted().build<Unit>()
    }

    @GetMapping(
        "/export/stream",
    )
    fun exportEventsAsStream(): ResponseEntity<StreamingResponseBody> {
        val stream = eventExportPort.streamEvents()

        val responseBody = StreamingResponseBody {
            it.bufferedWriter().use { writer ->
                stream.forEach { event ->
                    writer.write(event.toString())
                    writer.write("\n")
                }
            }
        }
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export.txt")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(responseBody)
    }
}
