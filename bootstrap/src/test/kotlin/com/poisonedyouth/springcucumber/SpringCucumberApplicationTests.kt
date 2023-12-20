package com.poisonedyouth.springcucumber

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class SpringCucumberApplicationTests {

    @Test
    fun contextLoads() {
        // Check that application can start successful
    }
}
