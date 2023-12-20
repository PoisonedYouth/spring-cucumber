package com.poisonedyouth.springcucumber.application.user.usecases

import com.poisonedyouth.springcucumber.application.user.ports.input.UserDto
import java.util.*

interface ReadUserUseCase {
    fun find(userId: UUID): UserDto?

    fun all(): List<UserDto>
}
