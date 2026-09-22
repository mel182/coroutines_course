package org.example

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.seconds

fun main()  = runBlocking {

    val result = withTimeoutOrNull(10.seconds) {
        coroutineScope {
            launch { while (true) {
                delay(1.seconds)
                println("Coo")
            } }
            launch { while (true) {
                delay(2.seconds)
                println("Caw")
            } }
            launch { while (true) {
                delay(3.seconds)
                println("Chirp")
            } }
        }
    }
    if (result == null)
        println("10 second passed and all jobs are cancelled")
}