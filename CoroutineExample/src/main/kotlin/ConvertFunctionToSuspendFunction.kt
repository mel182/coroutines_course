package org.example

import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.system.measureTimeMillis

@OptIn(ExperimentalStdlibApi::class)
fun main(): Unit = runBlocking {

    val handler2 = CoroutineExceptionHandler { coroutineContext, exception ->
        println("Context $coroutineContext")
        println("Job: ${coroutineContext[Job]}")
        println("Name: '${coroutineContext[CoroutineName]?.name}'")
        println("Handler: ${coroutineContext[CoroutineExceptionHandler]}")
        println("Dispatcher: ${coroutineContext[CoroutineDispatcher]}")
        println("Caught ${exception.message}")
//        exception.printStackTrace()
    }

    val timeMillis = measureTimeMillis {
        launch(handler2) {
            try {
                val result = dummyFunctionWithCallback()
                println("Result: $result")
            }catch (e: Exception) {
                println("Active: ${coroutineContext.job.isActive}")
                println("Exception at launch: $e")
            }
        }
    }

    println("Completed in $timeMillis ms")
}

suspend fun dummyFunctionWithCallback(): String {

    return suspendCancellableCoroutine { continuation ->

        try {
            dummyFunctionWithCallback {
                continuation.resume(it)
            }
        }catch (e: Exception) {
            println("Exception: $e")
            continuation.context.job.ensureActive()
            continuation.resumeWithException(e)
        }

        continuation.invokeOnCancellation {
            // Invoked when the coroutine is cancelled!
            println("Cancelled")
        }
    }
}

fun dummyFunctionWithCallback(result: (String) -> Unit) {
    //Thread.sleep(3000L)

    val testMap = (0..1000).map { "$it" }
    testMap.forEach {
        println("Test: $it")
    }
//    throw Exception("Test") // Simulate throwing exception
    result("World!")
}
