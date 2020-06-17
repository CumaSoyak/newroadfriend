package roadfriend.app.utils

import kotlinx.coroutines.*

object CoroutinesUtils {

    fun <T : Any> io(work: suspend (() -> T?)): Job =
        CoroutineScope(Dispatchers.IO).launch {
            work()
        }

    fun <T : Any> ioThenMain(work: suspend (() -> T?), callback: ((T?) -> Unit)? = null): Job =
        CoroutineScope(Dispatchers.Main).launch {
            val data = CoroutineScope(Dispatchers.IO).async {
                return@async work()
            }.await()
            callback?.let {
                it(data)
            }
        }

    fun <T : Any> ui(work: suspend (() -> T?)): Job =
        CoroutineScope(Dispatchers.Main).launch {
            work()
        }
}