package org.example

import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

/**
 * The Shear In Operator
 * The shareIn operator converts a cold normal flow to a sheared flow, turning it into a hot flow.
 * It discusses the importance of using coroutine scope and setting shearing started values for effective implementations.
 *
 * State Flow vs Shared Flow
 * The distinction between state flow and shared flow is highlighted.
 * State flow caches the latest value, ensuring subscribers receive the most recent emission.
 * In contrast, sheared flow distributes emissions to collectors only during direct emissions, without caching values.
 *
 * Tips:
 * Use state flow for maintaining the latest emission, while shared flow is ideal for capturing all emissions.
 *
 * Practical Application in Mobile and Smartwatch Integration
 * A practical example is provided using a running tracker app, where communication between a mobile device and a smartwatch is handled by shared flows.
 * This ensures all messaging actions are captured, such as heart rate and distance updates.
 *
 * When to Use Shear In Operator
 * The shear in operator is beneficial when all data emissions need to be captured and shared across different components, like display updates on the phone’s UI.
 * By contrast, for static data representation, such as UI displays of current location, using state flow is more efficient.
 */
fun main() = runBlocking {

    val flow = flow<Int> { // -> Value source
        delay(1.seconds)
        emit(1)
        delay(2.seconds)
        emit(2)
        delay(3.seconds)
        emit(3)
    }.shareIn(
        this,
        // SharingStarted.Eagerly -> Sharing is started immediately and never stops.
        // SharingStarted.Lazily -> Sharing is started when the first subscriber appears and never stops
        // SharingStarted.WhileSubscribed -> Sharing is started when the first subscriber appears, immediately stops when the last subscriber disappears (by default), keeping the replay cache forever (by default).
        SharingStarted.Eagerly
    )

    val collectJob = flow.onEach {
        println("Collector 1 $it")
    }.launchIn(this)

    val collectJob2 = launch {
        delay(5.seconds)
        flow.onEach {
            println("Collector 2 $it")
        }.launchIn(this)
    }

    flow.first { it == 3 }
    delay(1.seconds)
    collectJob.cancel()
    collectJob2.cancel()
    coroutineContext.cancelChildren()
}