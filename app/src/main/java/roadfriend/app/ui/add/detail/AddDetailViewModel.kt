package roadfriend.app.ui.add.detail

import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.data.repository.DataManager
import roadfriend.app.ui.base.BaseViewModel
import roadfriend.app.ui.base.IBasePresenter
import roadfriend.app.utils.firebasedatebase.FirebaseHelper
import roadfriend.app.utils.firebasedatebase.TripFirebase

class AddDetailViewModel(dataManager: DataManager) : BaseViewModel<IBasePresenter>(dataManager) {
    fun postTripAdverment(
        trips: Trips,
        resultService: (isSucces: Boolean, trips: Trips?, tripId: String) -> Unit
    ) {
        getPresenter()?.showLoading()
        FirebaseHelper().createTrip(trips) { trips, id ->
            getPresenter()?.hideLoading()
            resultService(true, trips, id)
            TripFirebase().bidTableCreate(trips)

        }
    }

}


