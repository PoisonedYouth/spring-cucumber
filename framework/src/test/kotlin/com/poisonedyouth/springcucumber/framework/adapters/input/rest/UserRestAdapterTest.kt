package com.poisonedyouth.springcucumber.framework.adapters.input.rest

// Kotlin imports
import com.fasterxml.jackson.databind.ObjectMapper
import com.poisonedyouth.springcucumber.application.user.ports.input.AddressDto
import com.poisonedyouth.springcucumber.application.user.ports.input.CountryDto
import com.poisonedyouth.springcucumber.application.user.ports.input.NewUserDto
import com.poisonedyouth.springcucumber.application.user.ports.input.UserDto
import com.poisonedyouth.springcucumber.application.user.usecases.ReadUserUseCase
import com.poisonedyouth.springcucumber.application.user.usecases.WriteUserUseCase
import com.poisonedyouth.springcucumber.domain.common.exception.NotFoundException
import com.poisonedyouth.springcucumber.domain.common.vo.Identity
import com.poisonedyouth.springcucumber.framework.adapters.configuration.ApplicationConfiguration
import com.poisonedyouth.springcucumber.framework.adapters.configuration.RabbitMqConfiguration
import java.util.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UserRestAdapter::class)
@Import(ApplicationConfiguration::class, RabbitMqConfiguration::class)
class UserRestAdapterTest {
    @Autowired private lateinit var mockMvc: MockMvc

    @MockBean private lateinit var writeUserUseCase: WriteUserUseCase

    @MockBean private lateinit var readUserUseCase: ReadUserUseCase

    @Autowired private lateinit var objectMapper: ObjectMapper

    @Test
    fun `given valid user data, when addUser is called, then a new user is created`() {
        // given
        val user =
            NewUserDto(
                "John",
                "Doe",
                AddressDto("Street", "123", "City", 12345, CountryDto("Country", "CR"))
            )

        val identity = Identity.UUIDIdentity.NEW
        whenever(writeUserUseCase.add(user)).thenReturn(identity)

        // when
        val result =
            mockMvc.perform(
                post("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(user))
            )

        // then
        result.andExpect(status().isCreated).andExpect { res ->
            val uuid = objectMapper.readValue(res.response.contentAsString, UUID::class.java)
            assertThat(uuid).isEqualTo(identity.value)
        }
    }

    @Test
    fun `given user data, when addUser is called, but use case return NoIdentity, then server error is returned`() {
        // given
        val user =
            NewUserDto(
                firstName = "John",
                lastName = "Doe",
                address =
                    AddressDto(
                        streetName = "Street",
                        streetNumber = "123",
                        city = "City",
                        zipCode = 12345,
                        country = CountryDto(name = "Country", code = "CR")
                    )
            )

        whenever(writeUserUseCase.add(user)).thenReturn(Identity.NoIdentity)

        // when
        val result =
            mockMvc.perform(
                post("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(user))
            )

        // then
        result.andExpect(status().isInternalServerError).andExpect { res ->
            assertThat(res.response.contentAsString).isEqualTo("Failed to create user")
        }
    }

    @Test
    fun `given invalid user data when calling add user endpoint a bad request is returned`() {
        // given
        val user =
            NewUserDto(
                firstName = "John",
                lastName = "Doe",
                address =
                    AddressDto(
                        streetName = "Street",
                        streetNumber = "123",
                        city = "City",
                        zipCode = 123456,
                        country = CountryDto(name = "Country", code = "CR")
                    )
            )

        whenever(writeUserUseCase.add(user)).thenThrow(IllegalArgumentException("Invalid!"))

        // when
        val result =
            mockMvc.perform(
                post("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(user))
            )

        // then
        result.andExpect(status().isBadRequest).andExpect { res ->
            assertThat(res.response.errorMessage).isEqualTo("Invalid!")
        }
    }

    @Test
    fun `given non-existing user id, when findUser is called, return not found status`() {
        // given
        val id = UUID.randomUUID()
        whenever(readUserUseCase.find(id)).thenReturn(null)

        // when
        val result =
            mockMvc.perform(
                get("/user").contentType(MediaType.APPLICATION_JSON).queryParam("id", "$id")
            )

        // then
        result.andExpect(status().isNotFound()).andExpect { res ->
            assertThat(res.response.contentAsString).isEqualTo("User with id '$id' does not exist.")
        }
    }

    @Test
    fun `given existing user id, when findUser is called, then return user details`() {
        // given
        val id = UUID.randomUUID()
        val userDto =
            UserDto(
                identity = id,
                firstName = "John",
                lastName = "Doe",
                address =
                    AddressDto(
                        streetName = "Street",
                        streetNumber = "123",
                        city = "City",
                        zipCode = 123456,
                        country = CountryDto(name = "Country", code = "CR")
                    )
            )
        whenever(readUserUseCase.find(id)).thenReturn(userDto)

        // when
        val result =
            mockMvc.perform(
                get("/user").contentType(MediaType.APPLICATION_JSON).queryParam("id", "$id")
            )

        // then
        result.andExpect(status().isOk()).andExpect { res ->
            assertThat(objectMapper.readValue(res.response.contentAsString, UserDto::class.java))
                .isEqualTo(userDto)
        }
    }

    @Test
    fun `given valid user data, when updateUser is called, then an existing user is updated`() {
        // given
        val identity = Identity.UUIDIdentity.NEW
        val user =
            UserDto(
                identity = identity.value,
                firstName = "John",
                lastName = "Doe",
                address =
                    AddressDto(
                        streetName = "Street",
                        streetNumber = "123",
                        city = "City",
                        zipCode = 12345,
                        country = CountryDto(name = "Country", code = "CR")
                    )
            )

        // when
        val result =
            mockMvc.perform(
                put("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(user))
            )

        // then
        result.andExpect(status().isOk)
    }

    @Test
    fun `given invalid user data, when updateUser is called, then a bad request is returned`() {
        // given
        val identity = Identity.UUIDIdentity.NEW
        val user =
            UserDto(
                identity = identity.value,
                firstName = "John",
                lastName = "Doe",
                address =
                    AddressDto(
                        streetName = "Street",
                        streetNumber = "123",
                        city = "City",
                        zipCode = 12345,
                        country = CountryDto(name = "Country", code = "CR")
                    )
            )

        whenever(writeUserUseCase.update(user)).thenThrow(IllegalArgumentException("Failed!"))

        // when
        val result =
            mockMvc.perform(
                put("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(user))
            )

        // then
        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `given existing userId, when deleteUser is called, then accepted is returned`() {
        // given
        val identity = Identity.UUIDIdentity.NEW

        // when
        val result =
            mockMvc.perform(
                delete("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .queryParam("id", "${identity.value}")
            )

        // then
        result.andExpect(status().isAccepted)
    }

    @Test
    fun `given not existing userId, when deleteUser is called, then not found is returned`() {
        // given
        val identity = Identity.UUIDIdentity.NEW

        whenever(writeUserUseCase.delete(identity.value)).thenThrow(NotFoundException("Failed!"))

        // when
        val result =
            mockMvc.perform(
                delete("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .queryParam("id", "${identity.value}")
            )

        // then
        result.andExpect(status().isNotFound)
    }
}
