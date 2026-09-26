package org.example

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlin.time.Duration.Companion.milliseconds

/*
* This example focuses on the difference between supervisor scope and coroutine scope.
* Supervisor scope is preferred for independent image compression tasks, where failing one task
* doesn't affect others.
* Conversely, coroutine scope is ideal for tasks requiring all or nothing results, like API calls
* for profile data aggregation.
*/
@OptIn(ExperimentalStdlibApi::class)
fun main(): Unit = runBlocking {

    val dummyUris = (0..20).map { "Uri: $it" }.toList()

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        println("\n| ----------------- Coroutine '${coroutineContext[CoroutineName]?.name}' exception --------------- |")
        println("Context $coroutineContext")
        println("Job: ${coroutineContext[Job]}")
        println("Name: '${coroutineContext[CoroutineName]?.name}'")
        println("Handler: ${coroutineContext[CoroutineExceptionHandler]}")
        println("Dispatcher: ${coroutineContext[CoroutineDispatcher]}")
        println("Message: ${exception.message}")
        println("| ----------------- Coroutine exception --------------- |\n")
    }
    // supervisorScope example
    val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    coroutineScope.launch(exceptionHandler) {
        compressImageDummy(dummyUris)
    }.join()

    // coroutineScope example
    coroutineScope.launch(exceptionHandler + CoroutineName("Get profile")) {
        getProfile()
    }.join()
}

private suspend fun compressImageDummy(uris: List<String>) {
    // With supervisor scope if one failed the other ones will continue
    supervisorScope {
        uris.forEach { uri ->
            launch(CoroutineName(uri)) {
                compressImage(uri = uri).also {
                    saveCompressImage(uri = it)
                }
            }
        }
    }
}

private suspend fun compressImage(uri: String): String {
    delay(200.milliseconds)
    println("Compressed image $uri")

    if (uri == "Uri: 10") // Dummy exception to simulate
        throw Exception("Not a valid uri")

    return uri
}

private suspend fun saveCompressImage(uri: String) {
    delay(100.milliseconds)
    println("$uri saved!")
}

private suspend fun getProfile() {
    // coroutineScope is useful in a use case were you have two independent calls
    // and you need both to succeed in order to continue. If one od them failed
    // the other one would failed as well.
    coroutineScope {

        val profileData = async {
            getProfileData()
        }

        val profilePosts = async {
            getProfilePost()
        }

        val data = profileData.await()
        val posts = profilePosts.await()

        println("\nData: $data\nPosts: $posts")
    }
}

private suspend fun getProfileData(): String {
    delay(300.milliseconds)
    return "Profile data"
}

private suspend fun getProfilePost(): String {
    delay(300.milliseconds)
    throw Exception("Not a valid post")
    return "Profile posts"
}