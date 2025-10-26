package com.metawebthree.app.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class FlowApi {
    fun createFlow(value: String): Flow<String> = flow<String> {
        emit(value)
    }.filter {
        it != ""
    }.map {
        "Hello, $it"
    }

    fun createHotFlow() {
        val sharedFlow = MutableSharedFlow<Int>()
        runBlocking {
            launch(Dispatchers.IO) {
                sharedFlow.collect {
                    println(it)
                }
            }
            delay(1000)
            (0..100).forEach {
                sharedFlow.emit(it)
                delay(500)
            }
        }
    }

    fun test() {
        runBlocking {
            createFlow("world").collect {
                println(it)
            }
        }
    }
}