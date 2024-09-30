package org.example

//6. Simulating Race Conditions
//Problem: You have two coroutines that both attempt to increment a shared counter 1000 times each. Without proper synchronization, this can lead to race conditions, where the final count is less than 2000.
//
//Exercise: Write coroutines to simulate this race condition and then fix the issue using appropriate coroutine mechanisms like Mutex or AtomicInteger to synchronize access to the shared counter.

