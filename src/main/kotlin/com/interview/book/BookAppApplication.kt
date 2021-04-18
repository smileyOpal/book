package com.interview.book

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BookAppApplication

fun main(args: Array<String>) {
	runApplication<BookAppApplication>(*args)
}
