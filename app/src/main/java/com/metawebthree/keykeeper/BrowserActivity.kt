package com.metawebthree.keykeeper

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.metawebthree.keykeeper.ui.theme.DefaultTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.createCoroutine

class BrowserActivity : ComponentActivity() {

    private val mainScope = MainScope()
    private val delayTime: Long = 1500

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContent {
            DefaultTheme {
//                AndroidView(factory = )
                Text(text = "The browser for show web page")
            }
        }
        sideEffect()
    }

    private fun sideEffect() {
        /*val root: View = binding.root
        //  协程：业务写法，内部封装了基础写法
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {

        }
        mainScope.launch {
//            delay(delayTime)
            val newDelayTime = ::delayTime
            delay(newDelayTime.get())
            Log.d("0", "In to main scope launch task")
            root.allViews.forEach {
                when (it) {
                    is TextView -> {
                        Log.d("1", "This is a text view to update data: ${it.id}")
                    }

                    else -> Log.d("1", "This is not a text view")
                }
            }
            root.allViews.forEachIndexed(::iteratorInt)
            Log.d("api calling", "current time is : ${Thread.currentThread().name}")
        }*/
        //  协程：基础写法
        suspend {
            //  耗时处理操作
            loadAnimation {

            }
            5
        }.createCoroutine(object : Continuation<Int> {
            override val context: CoroutineContext = EmptyCoroutineContext

            override fun resumeWith(result: Result<Int>) {
                Log.d("api calling", "current time is: ${Thread.currentThread()}")
            }
        })
        runBlocking {
            launch {
                loadAnimation {
                    withContext(Dispatchers.Main) {

                    }
                }
            }
        }
    }

    /*private fun iteratorInt(index: Int, value: View) {
        Log.d(index.toString(), value.toString())
    }*/

    //  todo 加在动画
    private suspend fun loadAnimation(next: suspend () -> Unit) {
        delay(delayTime)
        next()
    }
}