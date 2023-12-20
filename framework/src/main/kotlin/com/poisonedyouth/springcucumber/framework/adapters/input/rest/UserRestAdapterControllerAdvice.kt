package com.poisonedyouth.springcucumber.framework.adapters.input.rest

import com.poisonedyouth.springcucumber.domain.common.exception.NotFoundException
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class UserRestAdapterControllerAdvice {

    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentException(
        response: HttpServletResponse,
        exception: IllegalArgumentException
    ) {
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.message)
    }

    @ExceptionHandler(NotFoundException::class)
    fun notFoundException(response: HttpServletResponse, exception: NotFoundException) {
        response.sendError(HttpStatus.NOT_FOUND.value(), exception.message)
    }
}
