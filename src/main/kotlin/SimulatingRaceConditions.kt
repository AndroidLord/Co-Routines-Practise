package org.example

import kotlinx.coroutines.*
import kotlinx.coroutines.selects.whileSelect
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger

//6. Simulating Race Conditions
//Problem: You have two coroutines that both attempt to increment a shared counter 1000 times each. Without proper synchronization, this can lead to race conditions, where the final count is less than 2000.
//
//Exercise: Write coroutines to simulate this race condition and then fix the issue using appropriate coroutine mechanisms like Mutex or AtomicInteger to synchronize access to the shared counter.

//fun main() {
//
//    var num: AtomicInteger = AtomicInteger(0)
//
//    runBlocking {
//        CoroutineScope(Dispatchers.IO).launch {
//
//            launch { repeat(1000){
//                println(num.incrementAndGet())
//            } }
//
//            launch {
//                repeat(1000){
//                    println(num.incrementAndGet())
//                }
//            }
//
//        }
//    }
//
//    println("Final value of integer is $num")
//}



// Simulating Race Conditions
//fun main() {
//    // Simulate race condition with a regular Int
//    var num = 0
//
//    // To simulate race condition
//    runBlocking {
//        val job1 = launch(Dispatchers.Default) {
//            repeat(1000) {
//                num++ // This operation is not thread-safe
//            }
//        }
//
//        val job2 = launch(Dispatchers.Default) {
//            repeat(1000) {
//                num++ // This operation is not thread-safe
//            }
//        }
//
//        joinAll(job1, job2)
//    }
//
//    println("Final value of integer is (Expected 2000): $num") // Likely < 2000 due to race condition
//
//    // Fixing the issue with Mutex
//    num = 0 // Resetting for synchronization test
//    val mutex = Mutex()
//
//    runBlocking {
//        val job1 = launch(Dispatchers.Default) {
//            repeat(1000) {
//                mutex.withLock {
//                    num++ // This operation is now thread-safe
//                }
//            }
//        }
//
//        val job2 = launch(Dispatchers.Default) {
//            repeat(1000) {
//                mutex.withLock {
//                    num++ // This operation is now thread-safe
//                }
//            }
//        }
//
//        joinAll(job1, job2)
//    }
//
//    println("Final value of integer after fixing (Should be 2000): $num") // Should be 2000
//}

fun main() = runBlocking {
    val sharedCounter = AtomicInteger(0)

    // Simulate two coroutines incrementing the counter
    val coroutine1 = launch {
        repeat(1000) {
            println(sharedCounter.incrementAndGet())
        }
    }

    val coroutine2 = launch {
        repeat(1000) {
            println(sharedCounter.incrementAndGet())
        }
    }

    joinAll(coroutine1,coroutine2)

    // Print the final count
    println("Final count: ${sharedCounter.get()}")
}
