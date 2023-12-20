package com.poisonedyouth.springcucumber.application.user.ports.input

import com.poisonedyouth.springcucumber.application.user.ports.output.UserOutputPort
import com.poisonedyouth.springcucumber.application.user.usecases.ReadUserUseCase
import com.poisonedyouth.springcucumber.domain.common.vo.Identity
import java.util.*

class ReadUserInputPort(private val userOutputPort: UserOutputPort) : ReadUserUseCase {
    override fun find(userId: UUID): UserDto? {
        return userOutputPort.findBy(Identity.UUIDIdentity(userId))?.toUserDto()
    }

    override fun all(): List<UserDto> {
        return userOutputPort.all().map { it.toUserDto() }
    }
}
