package org.example

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

fun main() = runBlocking {

    val job1 = launch {
        repeat(4) { i ->
            delay(1.seconds)
            println("Coo")
        }
    }

    val job2 = launch {
        repeat(4) { i ->
            delay(2.seconds)
            println("Caw")
        }
    }

    val job3 = launch {
        repeat(4) { i ->
            delay(3.seconds)
            println("Chirp")
        }
    }

    job1.join()
    job2.join()
    job3.join()
}