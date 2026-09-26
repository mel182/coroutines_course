package org.example

import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalStdlibApi::class)
fun main(): Unit = runBlocking {

    /*
    val handler1 = CoroutineExceptionHandler { coroutineContext, exception ->
        println("Context $coroutineContext")
        println("Caught ${exception.message}")
        exception.printStackTrace()
    }

    val coroutineScope1 = CoroutineScope(coroutineContext)

    coroutineScope1.launch(handler1) {
        launch {
            delay(1.seconds)
            throw Exception("Oeps!") // App will crash since it throws the exception on the thread
        }
        delay(2.seconds)
        println("Coroutine 1 finished")
    }

    coroutineScope1.launch {
        delay(2.seconds)
        println("Coroutine 2 finished")
    }
    */

    // The right way to handle coroutine exception
    val handler2 = CoroutineExceptionHandler { coroutineContext, exception ->
        println("Context $coroutineContext")
        println("Job: ${coroutineContext[Job]}")
        println("Name: '${coroutineContext[CoroutineName]?.name}'")
        println("Handler: ${coroutineContext[CoroutineExceptionHandler]}")
        println("Dispatcher: ${coroutineContext[CoroutineDispatcher]}")
        println("Caught ${exception.message}")
//        exception.printStackTrace()
    }

    val coroutineScope2 = CoroutineScope(Dispatchers.IO + CoroutineName("Coroutine 2") + SupervisorJob()) // Coroutine name is optional and it is part of the example

    coroutineScope2.launch(handler2) {
        launch {
            delay(1.seconds)
            throw Exception("Oeps!") // App will crash since it throws the exception on the thread
        }
        delay(2.seconds)
        println("Coroutine 1 finished")
    }.join()

    coroutineScope2.launch(handler2) {
        delay(2.seconds)
        println("Coroutine 2 finished")
    }.join()

}