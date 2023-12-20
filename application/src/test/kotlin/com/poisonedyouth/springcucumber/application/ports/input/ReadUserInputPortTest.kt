package com.poisonedyouth.springcucumber.application.ports.input

import com.poisonedyouth.springcucumber.application.user.ports.input.ReadUserInputPort
import com.poisonedyouth.springcucumber.application.user.ports.output.UserOutputPort
import com.poisonedyouth.springcucumber.domain.common.vo.Identity
import com.poisonedyouth.springcucumber.domain.user.entity.User
import com.poisonedyouth.springcucumber.domain.user.vo.Address
import com.poisonedyouth.springcucumber.domain.user.vo.Country
import com.poisonedyouth.springcucumber.domain.user.vo.Name
import com.poisonedyouth.springcucumber.domain.user.vo.ZipCode
import java.util.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class ReadUserInputPortTest {

    private val userOutputPort: UserOutputPort = mock()

    private val readUserInputPort = ReadUserInputPort(userOutputPort)

    @Test
    fun `should return userDto when user exists`() {
        // Given
        val userId = UUID.randomUUID()
        val user =
            User(
                identity = Identity.UUIDIdentity(userId),
                name = Name("John", "Doe"),
                address =
                    Address(
                        "Street",
                        "123",
                        "City",
                        ZipCode(12345),
                        Country("United States", "USA")
                    )
            )
        whenever(userOutputPort.findBy(Identity.UUIDIdentity(userId))).thenReturn(user)

        // When
        val result = readUserInputPort.find(userId)

        // Then
        assertThat(result).isNotNull
        assertThat(result?.identity).isEqualTo(userId)
        assertThat(result?.firstName).isEqualTo("John")
        assertThat(result?.lastName).isEqualTo("Doe")
    }

    @Test
    fun `should return null when user does not exist`() {
        // Given
        val userId = UUID.randomUUID()
        whenever(userOutputPort.findBy(Identity.UUIDIdentity(userId))).thenReturn(null)

        // When
        val result = readUserInputPort.find(userId)

        // Then
        assertThat(result).isNull()
    }
}
