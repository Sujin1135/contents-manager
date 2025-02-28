package io.contents

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<CollectorApplication>().with(TestcontainersConfiguration::class).run(*args)
}
