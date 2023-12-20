package com.poisonedyouth.springcucumber.application.feature

import com.poisonedyouth.springcucumber.application.event.ports.output.NotifyEventOutputPort
import com.poisonedyouth.springcucumber.application.user.ports.output.UserOutputPort
import com.poisonedyouth.springcucumber.domain.common.vo.Identity
import com.poisonedyouth.springcucumber.domain.event.vo.EventType
import com.poisonedyouth.springcucumber.domain.user.entity.User

class TestUserOutputPort : UserOutputPort {
    private val userList = mutableListOf<User>()

    override fun store(user: User): Identity {
        val newUser = user.copy(identity = Identity.UUIDIdentity.NEW)
        userList.add(newUser)
        return newUser.identity
    }

    override fun findBy(identity: Identity): User? {
        return userList.firstOrNull { it.identity == identity }
    }

    override fun delete(identity: Identity) {
        userList.removeIf { it.identity == identity }
    }

    override fun all(): List<User> {
        return userList.toList()
    }
}

class TestNotifyEventOutputPort : NotifyEventOutputPort {
    override fun sendEvent(eventType: EventType, event: User) {
        // Nothing to do
    }
}
