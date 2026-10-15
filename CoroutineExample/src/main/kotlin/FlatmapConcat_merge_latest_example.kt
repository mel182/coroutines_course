package org.example

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.ZoneOffset
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
fun main(): Unit = runBlocking {

    // In most cases you are fine with flat map latest if you for instance work
    // with a UI state were you care only about the latest state,
    // If you need to use a suspend function in a flow pipeline and you can do it concurrently use 'Flat map merge'
    // and you care about all emissions make use of 'flat map concat'


    /*
    println("Flat map concat example")
    flow {
        emit(1)
        delay(1.seconds)
        emit(2)
        delay(1.seconds)
        emit(3)
        delay(1.seconds)
    }.onEach {
        println("Emitting $it.....")
    }.flatMapConcat {
        // Flat map concat map each emission from the flow to the new flow
        flow {
            emit("One")
            delay(1.seconds)
            emit("Two")
            delay(1.seconds)
            emit("Three")
            delay(1.seconds)
        }
    }.onEach {
        println(it)
    }.launchIn(this)
    */

    /*
    println("Flat map latest example")
    flow {
        emit(1)
        delay(500.milliseconds)
        emit(2)
        delay(500.milliseconds)
        emit(3)
        delay(500.milliseconds)
    }.onEach {
        println("Emitting $it.....")
    }.flatMapLatest {
        // Flat map latest while drop the previous flow and cancel it and process the new emission
        flow {
            emit("One")
            delay(1.seconds)
            emit("Two")
            delay(1.seconds)
            emit("Three")
            delay(1.seconds)
        }
    }.onEach {
        println(it)
    }.launchIn(this)
    */

    println("Flat map merge example")
    flow {
        emit(1)
        delay(500.milliseconds)
        emit(2)
        delay(500.milliseconds)
        emit(3)
        delay(500.milliseconds)
    }.onEach {
        println("Emitting $it.....")
    }.flatMapMerge {
        println("Handling emit: $it")
        // Flat map merge does not process sequentially, but concurrently so each emission while will be mapped to a new coroutine
        // parallel coroutines
        flow {
            emit("'One' of the elements: $it")
            delay(1.seconds)
            emit("'Two' of the elements: $it")
            delay(1.seconds)
            emit("'Three' of the elements: $it")
            delay(1.seconds)
        }
    }.onEach {
        println(it)
    }.launchIn(this)
}


// Android Example
class DemoViewModel(
    private val taskDao: TaskDao
) {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    // In this example ones the selected date changed it automatically cancel
    // the current collecting flow and start collecting from the new flow based on the changes
    @OptIn(ExperimentalCoroutinesApi::class)
    val tasks = selectedDate
        .map { date ->
            val startMillis = date
                .atStartOfDay()
                .toEpochSecond(ZoneOffset.UTC) * 1000L
            val endMillis = date
                .plusDays(1)
                .atStartOfDay()
                .toEpochSecond(ZoneOffset.UTC) * 1000L

            startMillis to endMillis
        }.flatMapConcat { (startMillis, endMillis) ->
            taskDao.getTasksBetween(startMillis, endMillis)
        }.onEach {
            it // <-- list of int
        }.stateIn(
            GlobalScope, // <-- in a Android view model use 'viewModelScope'
            SharingStarted.WhileSubscribed(5000L),
            initialValue = LocalDate.now(),
        )


}


interface TaskDao {

    fun getTasksBetween(startMillis: Long, endMillis: Long): Flow<List<Int>>
}
