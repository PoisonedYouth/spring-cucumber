package com.poisonedyouth.springcucumber.framework.adapters.output.exposed

import com.poisonedyouth.springcucumber.domain.common.vo.Identity
import com.poisonedyouth.springcucumber.domain.user.entity.User
import com.poisonedyouth.springcucumber.domain.user.vo.Address
import com.poisonedyouth.springcucumber.domain.user.vo.Country
import com.poisonedyouth.springcucumber.domain.user.vo.Name
import com.poisonedyouth.springcucumber.domain.user.vo.ZipCode
import com.poisonedyouth.springcucumber.framework.adapters.configuration.ApplicationConfiguration
import com.poisonedyouth.springcucumber.framework.adapters.configuration.RabbitMqConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJpaTest
@Import(ApplicationConfiguration::class, ExposedConfig::class, RabbitMqConfiguration::class)
internal class ExposedUserRepositoryTest {

    @Autowired private lateinit var userRepository: ExposedUserRepository

    @Test
    fun `storeUser should store user and return correct identity`() {
        // given
        val user =
            User(
                identity = Identity.NoIdentity,
                name = Name("John", "Doe"),
                address =
                    Address(
                        streetName = "Street Name",
                        streetNumber = "Street Number",
                        city = "City",
                        zipCode = ZipCode(12345),
                        country = Country(name = "United States", code = "USA")
                    )
            )
        // when
        val actual = userRepository.store(user)
        // then
        assertThat(actual).isInstanceOf(Identity.UUIDIdentity::class.java)
    }

    @Test
    fun `findUserBy should return null when no user is found`() {
        // given
        val identity = Identity.UUIDIdentity.NEW
        // when
        val actual = userRepository.findBy(identity)
        // then
        assertThat(actual).isNull()
    }

    @Test
    fun `findUserBy should return correct user when user is found`() {
        // given
        val user =
            User(
                identity = Identity.NoIdentity,
                name = Name("John", "Doe"),
                address =
                    Address(
                        streetName = "Street Name",
                        streetNumber = "Street Number",
                        city = "City",
                        zipCode = ZipCode(12345),
                        country = Country("United States", "USA")
                    )
            )

        val userId = userRepository.store(user)

        // when
        val actual = userRepository.findBy(userId)
        // then
        assertThat(actual).isNotNull()
        assertThat(actual).isEqualTo(user.copy(identity = userId))
    }
}
