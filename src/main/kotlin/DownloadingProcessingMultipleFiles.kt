package org.example

//5. Downloading and Processing Multiple Files
//Problem: Your application needs to download a list of 5 files from the internet and process them in parallel. Each file must be downloaded and processed concurrently, but the result of each download should be available only after the file is fully downloaded.
//
//Exercise: Write a coroutine that downloads and processes all 5 files concurrently. Use async and await to ensure that each file is processed only after the download is complete.

//
//import kotlinx.coroutines.*
//
//data class File(val id: Int, val size: Int)
//suspend fun download(file: File): Long{
//    val time = (file.size*1.5).toLong()
//    delay(time)
//    println("Downloaded File ${file.id} in $time")
//    return time
//}
//suspend fun processed(file: File): Long{
//    val time = (file.size*1.2).toLong()
//    delay(time)
//    println("Processed File ${file.id} in $time")
//    return time
//}
//
//suspend fun DownloadFile(file:File){
//    println("Started Download file ${file.id}")
//    var delay = download(file)
//    delay += processed(file)
//    println("Downloaded and Processed File Complete in $delay")
//}
//
//
//fun main() {
//
//
//    val list = listOf(
//        File(1,1000),
//        File(2,2000),
//        File(3,1500),
//        File(4,1000),
//        File(5,2000),
//    )
//
//    runBlocking(Dispatchers.IO) {
//        list.forEach{
//            launch { DownloadFile(it) }
//        }
//    }
//}

import kotlinx.coroutines.*

// Simulate a File data class with id and size
data class File(val id: Int, val size: Int)

// Function simulating file download
suspend fun download(file: File): Long {
    val time = (file.size * 1.5).toLong()
    delay(time)  // Simulate download time
    println("Downloaded File ${file.id} in $time ms")
    return time
}

// Function simulating file processing
suspend fun process(file: File): Long {
    val time = (file.size * 1.2).toLong()
    delay(time)  // Simulate processing time
    println("Processed File ${file.id} in $time ms")
    return time
}

// Function to download and process the file
suspend fun downloadAndProcessFile(file: File): Long {
    println("Started Download and Process for file ${file.id}")
    val downloadTime = download(file)
    val processTime = process(file)
    val totalTime = downloadTime + processTime
    println("Completed File ${file.id} in $totalTime ms")
    return totalTime
}

fun main() = runBlocking {

    // Create a list of files to download and process
    val fileList = listOf(
        File(1, 1000),
        File(2, 2000),
        File(3, 1500),
        File(4, 1000),
        File(5, 2000)
    )

    // Using async to handle download and processing concurrently
    val results = fileList.map { file ->
        async(Dispatchers.IO) { downloadAndProcessFile(file) }
    }

    // Await for all files to be downloaded and processed
    results.awaitAll().forEach { result ->
        println("File operation for completed in $result ms")
    }
}
