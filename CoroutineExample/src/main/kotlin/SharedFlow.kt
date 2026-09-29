package org.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * A shared flow (hot flow) is created with the mutable shared flow class,
 * allowing the emission of values from any coroutine.
 * Unlike traditional flow builders, shared flows emit values without requiring a collector;
 * thus, some emissions can be lost if not actively handled.
 *
 * Collectors and Replay Cache
 * By setting a replay cache, you ensure that the shared flow caches a specified number
 * of emissions, which are sent immediately to new collectors.
 * This prevents lost emissions when collectors are not present.
 *
 * Handling Buffer and Buffer Overflow
 * The emit function in shared flows suspends progress until all collectors have processed
 * the emission. You can adjust buffer capacities and strategies, such as using drop oldest
 * to manage overflow, where the oldest emissions are discarded to make room for new ones.
 *
 * Practical Use Cases
 * Shared flows are useful for actions like triggering UI events just once, such as showing a
 * toast notification, or for sharing data like location updates among multiple collectors,
 * ensuring synchronized data without duplicate callbacks.
 *
 */
fun main(): Unit  = runBlocking {

    val sharedFlow = MutableSharedFlow<Int>(
        replay = 3, // ensure that the shared flow caches a specified number
        extraBufferCapacity = 3, // The buffer capacity
        onBufferOverflow = BufferOverflow.DROP_OLDEST // Buffer overflow, default: BufferOverflow.SUSPEND
    )

    GlobalScope.launch {
        sharedFlow.onEach {
            println("Collector 1: $it")
            delay(5000L)
        }.launchIn(GlobalScope)

        sharedFlow.onEach {
            println("Collector 2: $it")
        }.launchIn(GlobalScope)
    }.join() // Note: In Android you don't have to use 'join()'

    GlobalScope.launch {
        repeat(10) {
            delay(500L)
            sharedFlow.emit(it)
        }
    }.join() // Note: In Android you don't have to use 'join()'

}