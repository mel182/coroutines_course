package org.example

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

fun main()  = runBlocking {

    // Simple coroutine cancellation exception
    val job = launch {
        delay(2.seconds)
        println("Coroutine finished!")
    }

    delay(1.seconds)
    job.cancel()
    println("Coroutine cancelled!")
}