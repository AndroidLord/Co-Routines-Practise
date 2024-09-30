package org.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

//3. Simulating a Bank Transaction
//Problem: Imagine you’re building a banking system. You need to transfer money between two accounts. The transfer consists of two steps: debiting the first account and crediting the second account. Both operations need to be done atomically (if one fails, the other should be rolled back).
//
//Exercise: Write a coroutine-based solution that ensures both debit and credit operations are atomic. Use withContext to simulate debiting and crediting with different dispatchers for different operations (e.g., database operations).

// My Approach 1
//
//data class Account(val id: Int, var balance: Int)
//
//suspend fun debit(account: Account, amount: Int): Int {
//
//    if (account.balance < amount) {
//        throw InsufficientFundsException("Insufficient funds in the account")
//    }
//
//    account.balance -= amount
//    return amount
//}
//
//class InsufficientFundsException(message: String) : Exception(message)
//
//suspend fun credit(account: Account, amount: Int): Int {
//    account.balance += amount
//    return amount
//}
//suspend fun transfer(transferFrom: Account, transferTo: Account, amount: Int) {
//
//    var debitedAmount = 0
//    try {
//        withContext(Dispatchers.Default) {
//            debitedAmount = debit(transferFrom, amount)
//            try {
//                withContext(Dispatchers.IO) {
//                    credit(transferTo, amount)
//                }
//            } catch (e: Exception) {
//                println("Credit failed: ${e.message}")
//                throw e
//            }
//        }
//    } catch (e: Exception) {
//        credit(transferFrom, debitedAmount)
//        println("Transaction failed: ${e.message}")
//    }
//
//}
//
//fun main() {
//
//    val account1 = Account(1, 100)
//    val account2 = Account(2, 2000)
//
//    println("Before transfer:")
//    println("Account 1 balance: ${account1.balance}")
//    println("Account 2 balance: ${account2.balance}")
//
//    runBlocking {
//        transfer(account1, account2, 500)
//    }
//
//    println("After transfer:")
//    println("Account 1 balance: ${account1.balance}")
//    println("Account 2 balance: ${account2.balance}")
//}
//


// Approach I should have taken

import kotlinx.coroutines.*

data class Account(val id: Int, var balance: Int)

// Custom exception for insufficient funds
class InsufficientFundsException(message: String) : Exception(message)

// Function to debit from an account
suspend fun debit(account: Account, amount: Int): Int {
    if (account.balance < amount) {
        throw InsufficientFundsException("Insufficient funds in account ${account.id}")
    }
    account.balance -= amount
    return amount
}

// Function to credit to an account
suspend fun credit(account: Account, amount: Int): Int {
    account.balance += amount
    return amount
}

// Function to handle a transfer with rollback on failure
suspend fun transfer(transferFrom: Account, transferTo: Account, amount: Int) {
    var debitedAmount = 0

    // Start the transfer in Default Dispatcher for processing
    try {
        debitedAmount = withContext(Dispatchers.Default) {
            debit(transferFrom, amount)
        }

        // Perform the credit operation in IO Dispatcher (simulating DB write)
        withContext(Dispatchers.IO) {
            credit(transferTo, debitedAmount)
        }

        println("Transfer completed successfully.")

    } catch (e: InsufficientFundsException) {
        println("Transaction failed: ${e.message}")

    } catch (e: Exception) {
        // In case of any failure, rollback the debit operation
        withContext(Dispatchers.Default) {
            credit(transferFrom, debitedAmount)
        }
        println("Transaction failed: ${e.message}. Rollback performed.")
    }
}

fun main() {
    // Initialize two accounts with balance
    val account1 = Account(1, 1000)
    val account2 = Account(2, 500)

    println("Before transfer:")
    println("Account 1 balance: ${account1.balance}")
    println("Account 2 balance: ${account2.balance}")

    // Perform the transfer
    runBlocking {
        transfer(account1, account2, 300)
    }

    println("After transfer:")
    println("Account 1 balance: ${account1.balance}")
    println("Account 2 balance: ${account2.balance}")
}
