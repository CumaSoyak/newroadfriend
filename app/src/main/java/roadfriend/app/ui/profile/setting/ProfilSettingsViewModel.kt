package roadfriend.app.ui.profile.setting

import androidx.lifecycle.MutableLiveData
import roadfriend.app.data.remote.model.user.User
import roadfriend.app.data.repository.DataManager
import roadfriend.app.ui.base.BaseViewModel
import roadfriend.app.ui.base.IBasePresenter
import roadfriend.app.utils.PrefUtils

class ProfilSettingsViewModel(dataManager: DataManager) :
    BaseViewModel<IBasePresenter>(dataManager) {

    val userData: MutableLiveData<User> = MutableLiveData()

    fun setUser() {
        userData.value = PrefUtils.getUser()
    }

//    fun updateUser(
//        body: RequestBody?,
//        image: MultipartBody.Part?,
//        full_name: RequestBody,
//        etEmail: RequestBody
//    ) = viewModelScope.launch {
//        getPresenter()?.showLoading()
//
//        when (val result = withContext(Dispatchers.IO)
//
//        {
//            dataManager.updateProfileAsync(body, image, full_name, etEmail)
//        }) {
//
//            is ResultWrapper.Success -> {
//                PrefUtils.createUser(Gson().toJson(result.data.data))
//
//
//            }
//
//            is ResultWrapper.Error -> {
//                getPresenter()?.hideLoading()
//                getPresenter()?.onError(result.exception.message, result.exception.code)
//            }
//        }
//
//
//    }

}
