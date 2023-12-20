package com.poisonedyouth.springcucumber.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.architecture.Layer
import com.lemonappdev.konsist.api.ext.list.imports
import com.lemonappdev.konsist.api.ext.list.withPackage
import com.lemonappdev.konsist.api.verify.assertFalse
import org.junit.jupiter.api.Test

class ArchitectureTest {

    @Test
    fun `dependencies of root packages are correct`() {
        Konsist.scopeFromProduction().assertArchitecture {
            val applicationLayer =
                Layer("application", "com.poisonedyouth.springcucumber.application..")
            val domain = Layer("domain", "com.poisonedyouth.springcucumber.domain..")
            val framework = Layer("framework", "com.poisonedyouth.springcucumber.framework..")

            domain.dependsOnNothing()
            applicationLayer.dependsOn(domain)
            framework.dependsOn(applicationLayer, domain)
        }
    }

    @Test
    fun `domain layer does not use Spring annotations`() {
        Konsist.scopeFromProduction()
            .files
            .withPackage("com.poisonedyouth.springcucumber.domain..")
            .imports
            .assertFalse { import -> import.name.startsWith("org.springframework.") }
    }

    @Test
    fun `application player does not use framework layer`() {
        Konsist.scopeFromProduction()
            .files
            .withPackage("com.poisonedyouth.springcucumber.application..")
            .imports
            .assertFalse { import ->
                import.name.startsWith("com.poisonedyouth.springcucumber.framework..")
            }
    }
}
