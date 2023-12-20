package com.poisonedyouth.springcucumber.application.user.ports.input

import com.poisonedyouth.springcucumber.application.event.ports.output.NotifyEventOutputPort
import com.poisonedyouth.springcucumber.application.user.ports.output.UserOutputPort
import com.poisonedyouth.springcucumber.application.user.usecases.WriteUserUseCase
import com.poisonedyouth.springcucumber.domain.common.exception.NotFoundException
import com.poisonedyouth.springcucumber.domain.common.vo.Identity
import com.poisonedyouth.springcucumber.domain.event.vo.EventType
import java.util.*

class WriteUserInputPort(
    private val userOutputPort: UserOutputPort,
    private val notifyEventOutputPort: NotifyEventOutputPort
) : WriteUserUseCase {
    override fun add(user: NewUserDto): Identity {
        val mappedUser = user.toUser()
        return userOutputPort.store(mappedUser).also {
            notifyEventOutputPort.sendEvent(EventType.CREATED, mappedUser.copy(identity = it))
        }
    }

    override fun update(user: UserDto) {
        val mappedUser = user.toUser()
        requireNotNull(userOutputPort.findBy(mappedUser.identity)) {
            "User with id '${user.identity} does not exist."
        }
        userOutputPort.store(mappedUser).also {
            notifyEventOutputPort.sendEvent(EventType.UPDATED, mappedUser.copy(identity = it))
        }
    }

    override fun delete(userId: UUID) {
        val userIdentity = Identity.UUIDIdentity(userId)
        val user =
            userOutputPort.findBy(userIdentity)
                ?: throw NotFoundException("User with id '${userIdentity} does not exist.")
        userOutputPort.delete(userIdentity).also {
            notifyEventOutputPort.sendEvent(EventType.DELETED, user)
        }
    }
}
