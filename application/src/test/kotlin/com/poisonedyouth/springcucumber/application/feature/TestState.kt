package com.poisonedyouth.springcucumber.application.feature

import arrow.core.Either

class TestState<T : Any, U : Any> {
    lateinit var input: T
    lateinit var output: Either<Throwable, U>
}
