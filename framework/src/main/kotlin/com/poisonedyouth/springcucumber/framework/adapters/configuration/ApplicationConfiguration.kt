package com.poisonedyouth.springcucumber.framework.adapters.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.poisonedyouth.springcucumber.application.event.ports.input.UserEventInputPort
import com.poisonedyouth.springcucumber.application.event.ports.output.EventExportPort
import com.poisonedyouth.springcucumber.application.event.ports.output.EventOutputPort
import com.poisonedyouth.springcucumber.application.event.ports.output.NotifyEventOutputPort
import com.poisonedyouth.springcucumber.application.event.usecases.EventUseCase
import com.poisonedyouth.springcucumber.application.user.ports.input.ReadUserInputPort
import com.poisonedyouth.springcucumber.application.user.ports.input.WriteUserInputPort
import com.poisonedyouth.springcucumber.application.user.ports.output.UserOutputPort
import com.poisonedyouth.springcucumber.application.user.usecases.ReadUserUseCase
import com.poisonedyouth.springcucumber.application.user.usecases.WriteUserUseCase
import com.poisonedyouth.springcucumber.framework.adapters.output.exposed.ExposedEventRepository
import com.poisonedyouth.springcucumber.framework.adapters.output.exposed.ExposedUserRepository
import com.poisonedyouth.springcucumber.framework.adapters.output.file.FileEventExport
import com.poisonedyouth.springcucumber.framework.adapters.output.rabbitmq.RabbitMqUserSender
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfiguration {

    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper().registerModules(KotlinModule.Builder().build(), JavaTimeModule())
    }

    @Bean fun userOutputPort(): UserOutputPort = ExposedUserRepository()

    @Bean fun eventOutputPort(): EventOutputPort = ExposedEventRepository()

    @Bean
    fun notifyEventOutputPort(rabbitTemplate: RabbitTemplate, queue: Queue): NotifyEventOutputPort {
        return RabbitMqUserSender(rabbitTemplate, queue)
    }

    @Bean
    fun writeUserUseCase(
        userOutputPort: UserOutputPort,
        notifyEventOutputPort: NotifyEventOutputPort
    ): WriteUserUseCase = WriteUserInputPort(userOutputPort, notifyEventOutputPort)

    @Bean
    fun readUserUseCase(userOutputPort: UserOutputPort): ReadUserUseCase =
        ReadUserInputPort(userOutputPort)

    @Bean
    fun eventUseCase(eventOutputPort: EventOutputPort): EventUseCase =
        UserEventInputPort(eventOutputPort)

    @Bean
    fun eventExportPort(eventOutputPort: EventOutputPort): EventExportPort =
        FileEventExport(eventOutputPort)
}
