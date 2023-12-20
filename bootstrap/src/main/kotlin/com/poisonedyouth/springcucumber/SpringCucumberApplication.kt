package com.poisonedyouth.springcucumber

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication class SpringCucumberApplication

fun main(args: Array<String>) {
    runApplication<SpringCucumberApplication>(*args)
}
