package org.example

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

fun main(): Unit  = runBlocking {

    println("| ------------------- Using mutex locks example ------------------- |")
    val normalHashmap = hashMapOf<Int, Int>()
    val concurrentHashmap = ConcurrentHashMap<Int, Int>()

    val mutex = Mutex()
    val mutex2 = Mutex()

    val scope = CoroutineScope(Dispatchers.Default)
    scope.launch {
        (1..100000).map {
            launch {
                val random = Random.nextInt(1, 9)

                mutex.withLock {
                    val concurrentCount = concurrentHashmap[random] ?: 0
                    concurrentHashmap[random] = concurrentCount + 1
                }

                mutex2.withLock {
                    val normalCount = normalHashmap[random] ?: 0
                    normalHashmap[random] = normalCount + 1
                }
            }
        }.joinAll()
    }.join()

    println("\nNormal Hashmap:")
    normalHashmap
        .toSortedMap()
        .forEach { (key, value) ->
        println("$key -> $value")
    }

    println("\nConcurrent Hashmap:")
    concurrentHashmap
        .toSortedMap()
        .forEach { (key, value) ->
            println("$key -> $value")
        }
    println("| ------------------- Using mutex locks example ------------------- |\n\n")

    println("| ------------------- Using dispatcher limited parallelism example ------------------- |")
    val normalHashmap2 = hashMapOf<Int, Int>()
    val concurrentHashmap2 = ConcurrentHashMap<Int, Int>()

    // This is highly recommended in production since it is the most bug-free and deliver the best performance.
    val scope2 = CoroutineScope(Dispatchers.Default.limitedParallelism(1))
    scope2.launch {
        (1..100000).map {
            launch {
                val random2 = Random.nextInt(1, 9)

                val concurrentCount = concurrentHashmap2[random2] ?: 0
                concurrentHashmap2[random2] = concurrentCount + 1

                val normalCount = normalHashmap2[random2] ?: 0
                normalHashmap2[random2] = normalCount + 1
            }
        }.joinAll()
    }.join()

    println("Normal Hashmap 2:")
    normalHashmap2
        .toSortedMap()
        .forEach { (key, value) ->
            println("$key -> $value")
        }

    println("\nConcurrent Hashmap 2:")
    concurrentHashmap2
        .toSortedMap()
        .forEach { (key, value) ->
            println("$key -> $value")
        }
    println("| --------------------------------------------------- |\n\n")

}