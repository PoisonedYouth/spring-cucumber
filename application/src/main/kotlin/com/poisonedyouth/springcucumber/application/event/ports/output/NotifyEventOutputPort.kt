package com.poisonedyouth.springcucumber.application.event.ports.output

import com.poisonedyouth.springcucumber.domain.event.vo.EventType
import com.poisonedyouth.springcucumber.domain.user.entity.User

interface NotifyEventOutputPort {
    fun sendEvent(eventType: EventType, event: User)
}
