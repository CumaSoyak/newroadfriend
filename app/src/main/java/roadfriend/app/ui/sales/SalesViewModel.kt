package roadfriend.app.ui.sales

import androidx.lifecycle.MutableLiveData
import roadfriend.app.data.repository.DataManager
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.ui.base.BaseViewModel
import roadfriend.app.ui.base.IBasePresenter

class SalesViewModel(dataManager: DataManager) : BaseViewModel<IBasePresenter>(dataManager) {

    val trip: MutableLiveData<Trips> = MutableLiveData()

    fun setTrips(tripData: Trips) {
        trip.value = tripData
    }
}