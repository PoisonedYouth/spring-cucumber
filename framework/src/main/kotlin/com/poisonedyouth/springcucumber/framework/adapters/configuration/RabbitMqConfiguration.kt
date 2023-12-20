package com.poisonedyouth.springcucumber.framework.adapters.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class RabbitMqConfiguration {

    @Value("\${com.poisonedyouth.rabbitmq.queue}") private lateinit var queueName: String

    @Bean
    fun userQueue(): Queue {
        return Queue(queueName)
    }

    @Bean
    fun messageConverter(objectMapper: ObjectMapper): MessageConverter {
        return Jackson2JsonMessageConverter(objectMapper)
    }

    @Bean
    fun connectionFactory(environment: Environment): ConnectionFactory {
        val connectionFactory = CachingConnectionFactory()
        connectionFactory.setHost(environment.getRequiredProperty("spring.rabbitmq.host"))
        connectionFactory.port = environment.getRequiredProperty("spring.rabbitmq.port").toInt()
        connectionFactory.username = environment.getRequiredProperty("spring.rabbitmq.username")
        connectionFactory.setPassword(environment.getRequiredProperty("spring.rabbitmq.password"))
        return connectionFactory
    }

    @Bean
    fun rabbitTemplate(
        connectionFactory: ConnectionFactory,
        messageConverter: MessageConverter
    ): RabbitTemplate {
        val template = RabbitTemplate(connectionFactory)
        template.messageConverter = messageConverter
        return template
    }

    @Bean
    fun abstractRabbitListenerContainerFactory(
        connectionFactory: ConnectionFactory,
        messageConverter: MessageConverter
    ): SimpleRabbitListenerContainerFactory {
        val factory = SimpleRabbitListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        factory.setMessageConverter(messageConverter)
        return factory
    }
}
