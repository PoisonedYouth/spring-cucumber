package com.poisonedyouth.springcucumber.framework.adapters.output.rabbitmq

import com.poisonedyouth.springcucumber.application.event.ports.output.NotifyEventOutputPort
import com.poisonedyouth.springcucumber.application.user.ports.input.UserDto
import com.poisonedyouth.springcucumber.application.user.ports.input.toUserDto
import com.poisonedyouth.springcucumber.domain.event.vo.EventType
import com.poisonedyouth.springcucumber.domain.user.entity.User
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate

class RabbitMqUserSender(private val rabbitTemplate: RabbitTemplate, private val queue: Queue) :
    NotifyEventOutputPort {
    override fun sendEvent(eventType: EventType, event: User) {
        rabbitTemplate.convertAndSend(
            queue.name,
            EventSaveDto(type = eventType, userDto = event.toUserDto())
        )
    }
}

data class EventSaveDto(val type: EventType, val userDto: UserDto)
