package org.example

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

// Function to simulate a long-running task with dummy logic
suspend fun taskOne(): String {
    delay(2000) // Simulate task that takes 2 seconds
    return "Task One Completed"
}

suspend fun taskTwo(): String {
    delay(4000) // Simulate task that takes 4 seconds (This will timeout)
    return "Task Two Completed"
}

suspend fun taskThree(): String {
    delay(1500) // Simulate task that takes 1.5 seconds
    return "Task Three Completed"
}

fun main() {

    runBlocking {
        val time = measureTimeMillis {
            // Launch the three tasks concurrently with a timeout of 3 seconds for each
            val resultOne = withTimeoutOrNull(3000) { taskOne() }
            val resultTwo = withTimeoutOrNull(3000) { taskTwo() }
            val resultThree = withTimeoutOrNull(3000) { taskThree() }

            // Log the results (null means the task timed out)
            println("Result of Task One: ${resultOne ?: "Task One Timed Out"}")
            println("Result of Task Two: ${resultTwo ?: "Task Two Timed Out"}")
            println("Result of Task Three: ${resultThree ?: "Task Three Timed Out"}")
        }

        println("Total time taken: $time ms")
    }
}
