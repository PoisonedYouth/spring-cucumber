package com.poisonedyouth.springcucumber.application.user.usecases

import com.poisonedyouth.springcucumber.application.user.ports.input.NewUserDto
import com.poisonedyouth.springcucumber.application.user.ports.input.UserDto
import com.poisonedyouth.springcucumber.domain.common.vo.Identity
import java.util.*

interface WriteUserUseCase {
    fun add(user: NewUserDto): Identity

    fun update(user: UserDto)

    fun delete(userId: UUID)
}
