package com.poisonedyouth.springcucumber.application.feature.user

import arrow.core.Either
import com.poisonedyouth.springcucumber.application.feature.TestNotifyEventOutputPort
import com.poisonedyouth.springcucumber.application.feature.TestState
import com.poisonedyouth.springcucumber.application.feature.TestUserOutputPort
import com.poisonedyouth.springcucumber.application.user.ports.input.AddressDto
import com.poisonedyouth.springcucumber.application.user.ports.input.CountryDto
import com.poisonedyouth.springcucumber.application.user.ports.input.NewUserDto
import com.poisonedyouth.springcucumber.application.user.ports.input.WriteUserInputPort
import com.poisonedyouth.springcucumber.domain.common.vo.Identity
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeTypeOf

class AddUserSteps {

    private val userOutputPort = TestUserOutputPort()
    private val writeUserInputPort = WriteUserInputPort(userOutputPort, TestNotifyEventOutputPort())

    private val testState = TestState<NewUserDto, Identity>()

    @Given("I have valid user input data available")
    fun `create valid user object`() {
        testState.input =
            NewUserDto(
                firstName = "John",
                lastName = "Doe",
                address =
                    AddressDto(
                        streetName = "Main Street",
                        streetNumber = "12A",
                        city = "Los Angeles",
                        zipCode = 12345,
                        country = CountryDto("United States", "USA")
                    )
            )
    }

    @Given("I have invalid user input data available")
    fun `create invalid user object`() {
        testState.input =
            NewUserDto(
                firstName = "John",
                lastName = "Doe",
                address =
                    AddressDto(
                        streetName = "Main Street",
                        streetNumber = "12A",
                        city = "Los Angeles",
                        zipCode = 123456, // Invalid
                        country = CountryDto("United States", "USA")
                    )
            )
    }

    @When("I add the user to the system")
    fun `add user object to system`() {
        testState.output = Either.catch { writeUserInputPort.add(testState.input) }
    }

    @Then("the id of the user is returned")
    fun `check for the identity returned`() {
        testState.output.shouldBeRight().also { userOutputPort.findBy(it) shouldNotBe null }
    }

    @Then("I get an exception")
    fun `add invalid user object to system`() {
        testState.output.shouldBeLeft().shouldBeTypeOf<IllegalArgumentException>()
    }
}
