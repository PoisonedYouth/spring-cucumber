package com.poisonedyouth.springcucumber.framework.adapters.input.rest

// Kotlin imports
import com.fasterxml.jackson.databind.ObjectMapper
import com.poisonedyouth.springcucumber.application.event.ports.input.EventDto
import com.poisonedyouth.springcucumber.application.event.ports.output.EventExportPort
import com.poisonedyouth.springcucumber.application.event.usecases.EventUseCase
import com.poisonedyouth.springcucumber.domain.common.vo.Identity
import com.poisonedyouth.springcucumber.domain.event.entity.Event
import com.poisonedyouth.springcucumber.domain.event.vo.EventType
import com.poisonedyouth.springcucumber.framework.adapters.configuration.ApplicationConfiguration
import com.poisonedyouth.springcucumber.framework.adapters.configuration.RabbitMqConfiguration
import java.time.Instant
import java.util.*
import java.util.stream.Stream
import org.junit.jupiter.api.Test
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(EventRestAdapter::class)
@Import(ApplicationConfiguration::class, RabbitMqConfiguration::class)
class EventRestAdapterTest {
    @Autowired private lateinit var mockMvc: MockMvc

    @MockBean private lateinit var eventUseCase: EventUseCase

    @MockBean private lateinit var eventExportPort: EventExportPort

    @Autowired private lateinit var objectMapper: ObjectMapper

    @Test
    fun `given existing events, when getEvents is called, then return list of events`() {
        // given
        val expectedEventList =
            listOf(
                EventDto(UUID.randomUUID(), "payload1", EventType.CREATED, Instant.now()),
                EventDto(UUID.randomUUID(), "payload2", EventType.UPDATED, Instant.now())
            )
        whenever(eventUseCase.getEvents()).thenReturn(expectedEventList)

        // when & then
        mockMvc
            .perform(get("/events").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedEventList)))

        verify(eventUseCase, times(1)).getEvents()
    }

    @Test
    fun `given filename, when exportEvents is called, then return accepted status`() {
        // given
        val filename = "file1"

        // when & then
        mockMvc
            .perform(
                get("/events/export")
                    .param("filename", filename)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isAccepted)

        verify(eventExportPort, times(1)).storeEvents(filename)
    }

    @Test
    fun `when exportEventsAsStream is called, then return content with appropriate headers`() {
        // given
        val eventStream: Stream<Event> =
            Stream.of(
                Event(
                    Identity.UUIDIdentity(UUID.randomUUID()),
                    "payload1",
                    EventType.CREATED,
                    Instant.now()
                ),
                Event(
                    Identity.UUIDIdentity(UUID.randomUUID()),
                    "payload2",
                    EventType.UPDATED,
                    Instant.now()
                )
            )

        whenever(eventExportPort.streamEvents()).thenReturn(eventStream)

        // when & then
        mockMvc
            .perform(get("/events/export/stream").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(
                header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export.txt")
            )
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/octet-stream"))

        verify(eventExportPort, times(1)).streamEvents()
    }
}
