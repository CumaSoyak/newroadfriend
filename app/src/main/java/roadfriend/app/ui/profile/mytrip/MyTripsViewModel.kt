package roadfriend.app.ui.profile.mytrip

import androidx.lifecycle.MutableLiveData
import roadfriend.app.data.remote.model.trips.GetTripRequest
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.data.remote.network.ResultWrapper
import roadfriend.app.data.repository.DataManager
import roadfriend.app.ui.base.BaseViewModel
import roadfriend.app.ui.base.IBasePresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyTripsViewModel(dataManager: DataManager) : BaseViewModel<IBasePresenter>(dataManager) {

    val tripData: MutableLiveData<ArrayList<Trips>> = MutableLiveData()

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
}
