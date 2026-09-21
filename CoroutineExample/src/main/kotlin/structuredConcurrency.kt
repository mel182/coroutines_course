@file:OptIn(DelicateCoroutinesApi::class)

package org.example

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

fun main() = runBlocking {

    // An example of structured coroutine
    val job = GlobalScope.launch parentCoroutine@{ // parent coroutine
        launch child1Coroutine@ { // child1 coroutine
            launch childCoroutineChild1@ { // child coroutine of child1
                delay(1.seconds) // Will only suspend the child coroutine of child1
                println("Child coroutine of child 1 finished!")
            }
            delay(500.milliseconds) // Will only suspend the child1 coroutine
            println("Child 1 coroutine finished!")
        }
        println("Parent coroutine finished!")
    }

    job.join()

}