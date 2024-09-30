package org.example

import kotlinx.coroutines.*

//4. Lazy Coroutine Execution
//Problem: You’re building a feature where expensive computations (like generating reports) should only be triggered when the user requests them. Some users may not request the report at all.
//
//Exercise: Create a coroutine that performs the report generation but starts lazily (i.e., only when the result is explicitly requested). Test the lazy execution of the coroutine.

//suspend fun generateReport(): String {
//    println("Generating report...")
//    val random = (1..100).random()
//    delay(random.toLong())
//    return "Report generated in $random ms"
//}
//
//data class ApiRequest(val id: Int, val generateReport: Boolean) {
//    fun report() = CoroutineScope(Dispatchers.IO).async {
//        if (generateReport) async(start = CoroutineStart.LAZY) { generateReport() }
//        else async(start = CoroutineStart.LAZY) { "Report not requested" }
//    }
//
//}
//
//
//fun main() {
//
//    val user1 = ApiRequest(1, false)
//    val user2 = ApiRequest(2, true)
//
//    runBlocking {
//        println("User 1 requested report")
//        println(user1.report().await().await())
//
//        println("User 2 requested report")
//        println(user2.report().await().await())
//    }
//
//}



import kotlinx.coroutines.*

// Simulates an expensive operation of report generation.
suspend fun generateReport(): String {
    println("Generating report...")
    val random = (1..100).random()
    delay(random.toLong()) // Simulate a delay
    return "Report generated in $random ms"
}

// Represents an API request from a user
data class ApiRequest(val id: Int, val generateReport: Boolean)

// A function that launches a lazy coroutine for report generation
fun lazyReportGeneration(request: ApiRequest): Deferred<String> {
    return CoroutineScope(Dispatchers.IO).async(start = CoroutineStart.LAZY) {
        if (request.generateReport) {
            generateReport()  // Only generate if the flag is true
        } else {
            "Report not requested"
        }
    }
}

fun main() = runBlocking {
    val user1 = ApiRequest(1, false)
    val user2 = ApiRequest(2, true)

    // Lazy coroutines for both users
    val report1 = lazyReportGeneration(user1)
    val report2 = lazyReportGeneration(user2)

    // Manually starting and awaiting the result
    println("User 1 requested report")
    println(report1.await())  // This will not generate a report

    println("User 2 requested report")
    println(report2.await())  // This will generate the report
}
