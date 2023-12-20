package com.poisonedyouth.springcucumber.application.user.ports.output

import com.poisonedyouth.springcucumber.domain.common.vo.Identity
import com.poisonedyouth.springcucumber.domain.user.entity.User

interface UserOutputPort {

    fun store(user: User): Identity

    fun findBy(identity: Identity): User?

    fun delete(identity: Identity)

    fun all(): List<User>
}
