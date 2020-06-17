package roadfriend.app.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import roadfriend.app.data.local.model.Search
import roadfriend.app.data.remote.model.trips.GetTripRequest
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.data.remote.model.user.User
import roadfriend.app.data.remote.network.ResultWrapper
import roadfriend.app.data.repository.DataManager
import roadfriend.app.ui.base.BaseViewModel
import roadfriend.app.ui.base.IBasePresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(dataManager: DataManager) : BaseViewModel<IBasePresenter>(dataManager) {

    val userData: MutableLiveData<User> = MutableLiveData()
    val tripData: MutableLiveData<ArrayList<Trips>> = MutableLiveData()
    val first: MutableLiveData<Search> = MutableLiveData()
    val second: MutableLiveData<Search> = MutableLiveData()

    fun getTrips(getTripRequest: GetTripRequest) {
        getPresenter()?.showLoading()
        GlobalScope.launch(Dispatchers.Main) {
            when (val result =
                withContext(Dispatchers.IO) { dataManager.getTrips(getTripRequest) }) {
                is ResultWrapper.Success -> {
                    getPresenter()?.hideLoading()
                    tripData.value = result.data.data
                }
                is ResultWrapper.Error -> {
                    getPresenter()?.hideLoading()
                }

            }

        }

    }

    fun getUser() = viewModelScope.launch {
        //        getPresenter()?.showLoading()
//        GlobalScope.launch(Dispatchers.Main) {
//            when (val result = withContext(Dispatchers.IO) { dataManager.getTrips() }) {
//                is ResultWrapper.Success -> {
//                    getPresenter()?.hideLoading()
//                    userData.value = DummyData.getUser()
//
//
//                }
//                is ResultWrapper.Error -> {
//                    getPresenter()?.hideLoading()
//                }
//
//            }
//
//        }
    }

}