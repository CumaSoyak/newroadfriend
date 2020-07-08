package roadfriend.app.ui.home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import roadfriend.app.data.repository.DataManager
import roadfriend.app.ui.base.BaseViewModel
import roadfriend.app.ui.base.IBasePresenter

class HomeViewModel(dataManager: DataManager) : BaseViewModel<IBasePresenter>(dataManager) {

    val myTripsSize: MediatorLiveData<Int> = MediatorLiveData<Int>()

}