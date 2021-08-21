package roadfriend.app.ui.base

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import roadfriend.app.data.remote.network.RemoteDataException
import roadfriend.app.data.repository.DataManager
import java.lang.ref.WeakReference

abstract class BaseViewModel : ViewModel(), KoinComponent {
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
    val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val errorMessageAlerter = MutableLiveData<String>()
    val succesMessage = MutableLiveData<String>()
    val succesMessageAlerter = MutableLiveData<String>()
    val dispatchGroup = DispatchGroup()

    private fun showLoading() {
        if (dispatchGroup.count == 0) {
            isLoading.postValue(true)
        }
        dispatchGroup.enter()
    }

    private fun hideLoading() {
        dispatchGroup.leave()
        dispatchGroup.notify {
            isLoading.postValue(false)
        }
    }

    fun showError(message: String) {
        errorMessage.postValue(message)
    }

    fun showSucces(message: String) {
        succesMessage.postValue(message)
    }

    fun showErrorAlerter(message: String) {
        errorMessageAlerter.postValue(message)
    }

    fun showSuccesAlerter(message: String) {
        succesMessageAlerter.postValue(message)
    }

    protected suspend fun <T> executeFlow(
        callFlow: Flow<T>,
        runWithLoading: Boolean = true,
        completionHandler: (data: T) -> Unit
    ) = callFlow
        .onStart {
            if (runWithLoading) {
                showLoading()
            }
        }
        .onCompletion {
            if (runWithLoading) {
                hideLoading()
            }
        }
        .catch {
            RemoteDataException(it)
        }
        .collect { state ->

            completionHandler(state)

        }


    protected fun launchOn(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(Dispatchers.IO) { block() }
    }

}

class DispatchGroup {
    var count = 0
    private var runnable: (() -> Unit)? = null

    init {
        count = 0
    }

    @Synchronized
    fun enter() {
        count++
    }

    @Synchronized
    fun leave() {
        count--
        notifyGroup()
    }

    fun notify(r: () -> Unit) {
        runnable = r
        notifyGroup()
    }

    private fun notifyGroup() {
        if (count <= 0 && runnable != null) {
            runnable!!()
        }
    }
}