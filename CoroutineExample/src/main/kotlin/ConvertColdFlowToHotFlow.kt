package org.example

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

/**
 * Converting Cold to Hot Flows
 * The need to convert cold flows to hot flows using the stateIn function is discussed. Cold flows start a new set of
 * emissions for each collector, while hot flows share a single stream.
 *
 * Note: StateIn Operator: Converts a cold flow to a hot state flow, enabling the flow to cache its latest emission.
 *
 * Behavior of Hot Flows
 * Hot flows only start collecting when there's at least one subscriber. The sharing started property controls when the
 * flow starts collecting, either eagerly, lazily, or while subscribed.
 *
 * Real-World Application
 * An example is provided using a tracker app to demonstrate how flows are used to track location, with stateIn caching
 * the current location in a state flow for efficient access.
 *
 * Tips:
 * - The stateIn operator is especially useful for caching data that needs to be accessed by multiple
 *   components without reinitializing.
 * - Application-wide scope ensures flows are accessible throughout the app.
 */
@OptIn(DelicateCoroutinesApi::class)
fun main(): Unit = runBlocking {

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

}