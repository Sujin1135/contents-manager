package io.contents

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CollectorApplication

fun main(args: Array<String>) {
    System.setProperty("spring.config.name", "boot,application,infrastructure,presentation")
    runApplication<CollectorApplication>(*args)
}
