package roadfriend.app.ui.auth.login

import androidx.lifecycle.viewModelScope
import roadfriend.app.data.remote.model.auth.login.LoginRequest
import roadfriend.app.data.remote.network.ResultWrapper
import roadfriend.app.data.repository.DataManager
import roadfriend.app.ui.base.BaseViewModel
import roadfriend.app.ui.base.IBasePresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(dataManager: DataManager) : BaseViewModel<IBasePresenter>(dataManager) {

    fun postLogin(request: LoginRequest, isSucces: () -> Unit) = viewModelScope.launch {

        getPresenter()?.showLoading()
        when (val result = withContext(Dispatchers.IO)
        { dataManager.postLoginRequest(request) }) {

            is ResultWrapper.Success -> {
                getPresenter()?.hideLoading()
                isSucces()


            }
            is ResultWrapper.Error -> {

                getPresenter()?.hideLoading()

                getPresenter()?.onError(result.exception.message, result.exception.code)

            }
        }
    }
}

