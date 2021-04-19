package com.interview.book

import com.interview.book.config.ApplicationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties::class)
class BookAppApplication

fun main(args: Array<String>) {
    runApplication<BookAppApplication>(*args)
}
