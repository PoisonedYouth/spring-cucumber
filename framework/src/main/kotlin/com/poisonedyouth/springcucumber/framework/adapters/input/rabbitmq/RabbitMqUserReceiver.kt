package com.poisonedyouth.springcucumber.framework.adapters.input.rabbitmq

import com.fasterxml.jackson.databind.ObjectMapper
import com.poisonedyouth.springcucumber.application.event.ports.input.EventDto
import com.poisonedyouth.springcucumber.application.event.usecases.EventUseCase
import com.poisonedyouth.springcucumber.domain.common.vo.Identity
import com.poisonedyouth.springcucumber.framework.adapters.output.rabbitmq.EventSaveDto
import java.time.Instant
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class RabbitMqUserReceiver(
    private val eventInputPort: EventUseCase,
    private val objectMapper: ObjectMapper
) {
    @RabbitListener(queues = ["\${com.poisonedyouth.rabbitmq.queue}"])
    fun onUserChanged(eventSaveDto: EventSaveDto) {
        eventInputPort.storeEvent(
            EventDto(
                identity = Identity.UUIDIdentity.NEW.value,
                payload = objectMapper.writeValueAsString(eventSaveDto.userDto),
                eventType = eventSaveDto.type,
                created = Instant.now()
            )
        )
    }
}
