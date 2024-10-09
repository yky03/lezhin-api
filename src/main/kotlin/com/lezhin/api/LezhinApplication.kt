package com.lezhin.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LezhinApplication

fun main(args: Array<String>) {
    runApplication<LezhinApplication>(*args)
}
