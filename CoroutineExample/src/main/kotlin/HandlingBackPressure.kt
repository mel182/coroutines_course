package org.example

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

fun main(): Unit = runBlocking {

    // This example shows how to handle back pressure in a flow

    // Simple example
    /*
    flow<Int> {
        repeat(10) {
            emit(it) // Producer emits twice a second
            delay(500.milliseconds)
        }
    }.onEach {
        delay(1.seconds) // While the collector can process only one a second
        println("Processed emission: $it")
        // By nature the producer will wait until the collector has finished before proceeding with the next emission
    }.launchIn(this)
    */

    // Solution 1: Using buffer()
    /*
    println("Food example using buffer")
    flow<String> {
        println("FOOD: Preparing appetizer")
        delay(500.milliseconds)
        emit("Appetizer")

        println("FOOD: Preparing main dish")
        delay(1.seconds)
        emit("Main dish")

        println("FOOD: Preparing dessert")
        delay(500.milliseconds)
        emit("Dessert")

    }
        .buffer() // Buffer act like a buffer and still keep on
                  // collecting emission of the flow (in separated coroutine)
                  // and cached these values that the collector was not yet able to collect and process
                  // Important: In a more complex case when there is a lot if emission being
                  // produced it can lead to buffer overflow and crash with a buffer overflow exception.
                  // To avoid this use: buffer(capacity:Int 'amount of buffer to cached' DEFAULT: 1, onBufferOverflow: BufferOverflow 'buffer overflow strategy' DEFAULT: 'BufferOverflow.SUSPEND')
                  // Usage: This can be used if you need to call a suspendable function and need to process all the emission coming in, but be a bit careful that the buffer doesn't overflow.
        .onEach { dish ->
            println("\nFOOD: Start eating $dish")
            delay(2500.milliseconds)
            println("FOOD: Finished eating $dish")
        }.launchIn(this)
    */

    // Solution 2: Using conflat()
    /*
    println("Food example using conflate")
    flow<String> {
        println("FOOD: Preparing appetizer")
        delay(500.milliseconds)
        emit("Appetizer")

        println("FOOD: Preparing main dish")
        delay(1.seconds)
        emit("Main dish")

        println("FOOD: Preparing dessert")
        delay(500.milliseconds)
        emit("Dessert")

    }.conflate() // Conflate will drop all subsequence emissions until there is a time to collect and process it.
                    // Under the hood it is just a buffer(onBufferOverflow: BufferOverflow.DROP_OLDEST).
                    // Usage: This can be used if you need to call a suspendable function, and you're fine with some emissions being skipped when the collector is busy.
    .onEach { dish ->
        println("\nFOOD: Start eating $dish")
        delay(2500.milliseconds)
        println("FOOD: Finished eating $dish")
    }.launchIn(this)
    */

    // Solution 3: Using collect latest
    println("Food example using collect latest")
    flow<String> {
        println("FOOD: Preparing appetizer")
        delay(500.milliseconds)
        emit("Appetizer")

        println("FOOD: Preparing main dish")
        delay(1.seconds)
        emit("Main dish")

        println("FOOD: Preparing dessert")
        delay(500.milliseconds)
        emit("Dessert")

    }.collectLatest { dish -> // Collect latest can also be used to handle back pressure
                              // It will process all emissions, but when a new emission is
                              // being collected, it will drop the previous emission and
                              // cancel it and immediately start processing the new one.
                              // Usage: This solution and conflate is similar, but this one will cancel the current emission being process while conflate does not.
                              //        This is mostly used in UI state updates.
        println("\nFOOD: Start eating $dish")
        delay(2500.milliseconds)
        println("FOOD: Finished eating $dish")
    }

}