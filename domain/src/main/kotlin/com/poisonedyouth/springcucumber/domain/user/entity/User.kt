package com.poisonedyouth.springcucumber.domain.user.entity

import com.poisonedyouth.springcucumber.domain.common.vo.Identity
import com.poisonedyouth.springcucumber.domain.user.vo.Address
import com.poisonedyouth.springcucumber.domain.user.vo.Name

data class User(val identity: Identity, val name: Name, val address: Address)
