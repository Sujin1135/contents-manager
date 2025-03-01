package io.contents.collector

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.elasticsearch.ElasticsearchContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {
    @Bean
    @ServiceConnection
    fun elasticsearchContainer(): ElasticsearchContainer =
        ElasticsearchContainer(DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:7.17.10"))

    @Bean
    @ServiceConnection
    fun postgresContainer(): PostgreSQLContainer<*> = PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
}
