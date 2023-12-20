package com.poisonedyouth.springcucumber.framework.adapters.output.exposed

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource
import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.getRequiredProperty

@Configuration
@ImportAutoConfiguration(
    value = [ExposedAutoConfiguration::class],
    exclude = [DataSourceTransactionManagerAutoConfiguration::class]
)
class ExposedConfig {

    @Bean fun databaseConfig() = DatabaseConfig { useNestedTransactions = true }

    @Bean
    fun dataSource(
        environment: Environment,
    ): DataSource {
        val config =
            HikariConfig().apply {
                this.poolName =
                    environment.getRequiredProperty("spring.datasource.hikari.pool-name")
                this.driverClassName =
                    environment.getRequiredProperty("spring.datasource.hikari.driverClassName")
                this.minimumIdle =
                    environment.getRequiredProperty<Int>("spring.datasource.hikari.minimum-idle")
                this.maximumPoolSize =
                    environment.getRequiredProperty<Int>(
                        "spring.datasource.hikari.maximum-pool-size"
                    )
                this.idleTimeout =
                    environment.getRequiredProperty<Long>("spring.datasource.hikari.idle-timeout")
                this.maxLifetime =
                    environment.getRequiredProperty<Long>("spring.datasource.hikari.max-lifetime")
                this.keepaliveTime =
                    environment.getRequiredProperty<Long>("spring.datasource.hikari.keepalive-time")
                username = environment.getRequiredProperty("spring.datasource.username")
                password = environment.getRequiredProperty("spring.datasource.password")
                jdbcUrl = environment.getRequiredProperty("spring.datasource.url")
            }
        return HikariDataSource(config)
    }

    @Bean
    fun exposedDatabaseConnection(dataSource: DataSource): Database {
        return Database.connect(dataSource)
    }
}
