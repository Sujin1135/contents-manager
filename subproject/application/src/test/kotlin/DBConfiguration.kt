package io.contents.collector

import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.ConnectionFactoryOptions.DATABASE
import io.r2dbc.spi.ConnectionFactoryOptions.DRIVER
import io.r2dbc.spi.ConnectionFactoryOptions.HOST
import io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD
import io.r2dbc.spi.ConnectionFactoryOptions.PORT
import io.r2dbc.spi.ConnectionFactoryOptions.USER
import org.flywaydb.core.Flyway
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.testcontainers.containers.PostgreSQLContainer

@TestConfiguration(proxyBeanMethods = false)
@Import(TestcontainersConfiguration::class)
class DBConfiguration {
    @Bean
    fun connectionFactory(container: PostgreSQLContainer<*>): ConnectionFactory =
        ConnectionFactories.get(
            ConnectionFactoryOptions
                .builder()
                .option(DRIVER, "postgresql")
                .option(HOST, container.host)
                .option(PORT, container.firstMappedPort)
                .option(USER, container.username)
                .option(PASSWORD, container.password)
                .option(DATABASE, container.databaseName)
                .build(),
        )

    @Bean
    fun dslContext(connectionFactory: ConnectionFactory): DSLContext = DSL.using(connectionFactory)

    @Bean
    fun flyway(postgresqlContainer: PostgreSQLContainer<*>): Flyway =
        Flyway
            .configure()
            .dataSource(
                postgresqlContainer.jdbcUrl,
                postgresqlContainer.username,
                postgresqlContainer.password,
            ).locations("classpath:db/migration")
            .cleanDisabled(false)
            .load()
}
