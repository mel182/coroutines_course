package org.example

import kotlinx.coroutines.*
import kotlinx.coroutines.time.delay
import kotlin.time.Duration.Companion.seconds

fun main(): Unit = runBlocking {

    val customScope = CoroutineScope(Dispatchers.Default)

    customScope.launch {
        delay(2.seconds)
        println("Job finished!")
    }

    launch {
        delay(1.seconds)
        //customScope.cancel() // if you use the cancel() function it will cancel the coroutine and cannot be used anymore
        customScope.coroutineContext.cancelChildren() // this will only cancel the children and not the entire coroutine so it can still be used!
        customScope.launch { println("Hello World!") }
    }

    // Non cancellable coroutines example:
    // This meant for when for example a database
    // or file input stream is still open while the
    // coroutine is cancelled, you can still close
    // them or do some cleanup if needed. Note: Such
    // context is dangerous so you have to be caution
    // with it and only use it for such use case.
    val testJob = launch {
        try {
            delay(2.seconds)
            println("Trying........")
            throw Exception("Testing")
        }catch (e: Exception) {
            println("Exception: ${e}")
            if (e is CancellationException) throw e
            println("Exception in runBlocking: ${e.message}")
        } finally {
            if (isActive) {
                println("Job finished!")
            } else {
                withContext(NonCancellable) {
                    println("Job finished non cancellable!")
                }
            }
        }
    }

    delay(1.seconds)
    testJob.cancel()
}