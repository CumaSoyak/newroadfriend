package roadfriend.app.ui.profile

import androidx.lifecycle.MutableLiveData
import roadfriend.app.data.remote.model.user.User
import roadfriend.app.data.repository.DataManager
import roadfriend.app.ui.base.BaseViewModel
import roadfriend.app.ui.base.IBasePresenter
import roadfriend.app.utils.PrefUtils

class ProfilViewModel(dataManager: DataManager) : BaseViewModel<IBasePresenter>(dataManager) {

    val userData: MutableLiveData<User> = MutableLiveData()

    fun getUserDatas() {
        userData.value = PrefUtils.getUser()
    }
}