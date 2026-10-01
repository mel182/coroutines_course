package org.example

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

/**
 * State flow is a concept in Android development, mainly used to hold state that can change over time.
 * It is a flow wrapper around a simple mutable property and is particularly helpful for UI updates.
 *
 * Defining and Using State Flow
 * A state flow is constructed using a mutable state flow, with an initial value. This type of flow is used
 * to hold a single value and will emit this cached value to new collectors.
 *
 * Note: State flow always holds a specific single value. If a new collector appears, it will immediately receive the cached value.
 *
 * State Flow Behavior
 * Unlike normal code flows, state flows share the value between collectors.
 * Changing the state flow's value will trigger an update for all collectors.
 *
 * Common Use Cases
 * State flows are commonly used to hold UI state that can impact the appearance and look of an Android app's UI,
 * such as filling UI fields or indicating loading states.
 *
 * Updating State Flow Safely
 * State flow values are thread safe, meaning they can be updated safely from concurrent coroutines.
 * This ensures the synchronization of the access to state flow values.
 *
 * Important: Using 'state.update' is recommended to avoid race conditions when updating UI state,
 * especially with concurrent operations.
 *
 * Practical Implementation
 * State flow is often used in conjunction with Compose to convert the state flow value into a
 * Compose state, allowing more efficient UI updates.
 *
 * Important: Sharing values between different collectors can prevent redundant operations and optimize performance for Android apps.
 *
 * Advanced Scenarios
 * Handling complex UI states with a data class can optimize the management of multiple state aspects or flow processing on different
 * threads to avoid performance bottlenecks.
 */
@OptIn(DelicateCoroutinesApi::class)
fun main() = runBlocking {

    val stateFlow = MutableStateFlow(0)

    val job = stateFlow.onEach {
        println("Value is $it")
    }.launchIn(this)

    repeat(10) { count ->
        stateFlow.update { it + count }
        delay(1.seconds)
    }

    job.cancel()
}