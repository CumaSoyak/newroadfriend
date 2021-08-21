package roadfriend.app.ui.home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import roadfriend.app.data.remote.model.trips.GetTripRequest
import roadfriend.app.data.repository.DataManager
import roadfriend.app.ui.base.BaseViewModel
import roadfriend.app.ui.base.IBasePresenter

class HomeViewModel : BaseViewModel(){
    val tripRequest = MutableLiveData<GetTripRequest>()

}