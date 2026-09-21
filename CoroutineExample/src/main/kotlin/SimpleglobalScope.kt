package org.example

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    // This is an example of concurrency.
    // Note: This is not parallelism since that means for example
    // cooking the chicken and rice at the same time (you need 4 hands).
    // However, in concurrency while you wait for the chicken to fully cooked you cook the rice.
    val job1 = GlobalScope.launch {
        repeat(100) {
            println("Hello from coroutine 1 -> $it")
        }
    }

    val job2 = GlobalScope.launch {
        repeat(100) {
            println("Hello from coroutine 2 -> $it")
        }
    }

    job1.join()
    job2.join()
}