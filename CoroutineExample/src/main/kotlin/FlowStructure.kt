package org.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

/**
 * Value Source
 * Each launched flow begins with a value source, the origin where emissions are generated.
 * In contrast to normal functions that return a single value, flows can emit multiple values over time.
 *
 * Intermediate Operators
 * In between the value source and terminal operator, optional intermediate operators can be utilized.
 * These operators transform the flow, deciding how emissions are handled. Examples include:
 * - filter - Filters specific emissions
 * - flow on - Switches to a specific coroutine dispatcher
 * - map - Maps the emissions
 *
 * Terminal Operators
 * The last step is a terminal operator, which finalizes the flow execution. Examples include:
 * - launch in - Launches and collects the flow
 * - collect - Acts as a terminal operation that allows emission response
 * - first - Suspends until the first emission and returns it
 *
 * Note: Without a terminal operator, the flow will not execute as it is not launched.
 *
 * Launching and Collecting Flows
 * To execute a flow, it must be explicitly launched. This is typically done using the collect function,
 * allowing access to each emission as received. Alternatively, the launch in function can launch a flow
 * within a coroutine scope, acting like collect but without a collect block.
 */
fun main(): Unit  = runBlocking {
    GlobalScope.launch {
        flow<Int> { // -> Value source
            delay(1.seconds)
            emit(1)
            delay(2.seconds)
            emit(2)
            delay(3.seconds)
            emit(3)
        }.collect { // -> Terminal operator
            println("Example 1 emit: $it")
        }
    }.join() // Note: In Android you don't have to use join()

    // With intermediate operator
    flow<Int> { // -> Value source
        delay(1.seconds)
        emit(1)
        delay(2.seconds)
        emit(2)
        delay(3.seconds)
        emit(3)
    }.onEach { // -> Intermediate operator
        println("onEach intermedia operator: $it")
    }.launchIn(GlobalScope).join() // -> Terminal operator Note: In Android you don't have to use join()
    // Note: The launch in function allows launching flows within a specified coroutine scope and
    //       reduces the need for global scope blocks, simplifying flow management.


    // With intermediate operator example 2
    GlobalScope.launch {
        flow<Int> { // -> Value source
            delay(1.seconds)
            emit(1)
            delay(2.seconds)
            emit(2)
            delay(3.seconds)
            emit(3)
        }.onEach { // -> Intermediate operator
            println("with first() terminal operator -> $it")
        }.first() // -> Terminal operator
    }.join() // Note: In Android you don't have to use join()
}