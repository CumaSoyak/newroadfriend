package roadfriend.app.ui.auth.register

import androidx.lifecycle.viewModelScope
import roadfriend.app.data.remote.network.ResultWrapper
import roadfriend.app.data.repository.DataManager
import roadfriend.app.ui.base.BaseViewModel
import roadfriend.app.ui.base.IBasePresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RegisterViewMoel(dataManager: DataManager) : BaseViewModel<IBasePresenter>(dataManager) {


    fun postRegister(
        image: MultipartBody.Part?,
        full_name: RequestBody,
        mail: RequestBody,
        password: RequestBody,
        isSucces: () -> Unit
    ) = viewModelScope.launch {

        getPresenter()?.showLoading()

        when (val result = withContext(Dispatchers.IO)
        { dataManager.postRegisterUser(image, full_name, mail, password) }) {

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


