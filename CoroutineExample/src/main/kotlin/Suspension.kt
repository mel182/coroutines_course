@file:OptIn(DelicateCoroutinesApi::class)

package org.example

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.milliseconds

fun main() = runBlocking {
    // Coroutine can pause itw works and resume when the result is present.
    val suspensionJob = GlobalScope.launch {
        repeat(100) {
            delay(50.milliseconds) // suspension point meaning it can't do anything but wait until it is finished
            println("Hello from coroutine 1 -> $it")
        }
    }
    suspensionJob.join()
}