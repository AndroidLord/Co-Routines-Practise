package org.example

import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext
import kotlin.system.measureTimeMillis

//7. Long-Running Background Task
//Problem: You’re building an app that compresses large video files. The compression task can take a long time, so it should run in the background without freezing the UI. Also, the user should be able to cancel the operation at any time.
//
//Exercise: Write a coroutine that compresses a video file in the background using Dispatchers.IO to prevent blocking the UI. Also, add support for cancellation if the user requests it mid-process.

//suspend fun compressVideo(){
//    println("Video Compression Started")
//    repeat(5){
//        delay(1000)
//        println("Prcessed ${5*4*it}")
//    }
//    println( "Video Compressed Ended - xx")
//}
//
//fun main() = runBlocking {
//
//    val job = launch { compressVideo() }
//
//    launch {
//        delay(10000)
//        print("Cancelling Video Compression")
//        job.cancel()
//    }
//    job.join()
//    println("Application end")
//
//}


suspend fun compressVideo() {
    println("Video Compression Started")
    repeat(5) {
        delay(1000)
        if (coroutineContext.job.isActive) { // Check if the coroutine is cancelled
            println("Processed ${5 * 4 * it}")
        } else {
            println("Compression cancelled")
            return // Exit the coroutine if cancelled
        }
    }
    println("Video Compressed Ended - xx")
}

fun main() = runBlocking {
    val job = launch(Dispatchers.IO) { compressVideo() }

    launch {
        delay(3000) // User waits for 3 seconds before canceling
        println("Cancelling Video Compression")
        job.cancelAndJoin() // Cancel and wait for completion
    }

    job.join() // Wait for the compressVideo coroutine to complete
    println("Application end")
}
