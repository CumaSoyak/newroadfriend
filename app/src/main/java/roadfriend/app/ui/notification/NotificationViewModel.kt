package roadfriend.app.ui.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import roadfriend.app.data.remote.model.notification.GetNotificationRequest
import roadfriend.app.data.remote.model.notification.NotificationModel
import roadfriend.app.data.remote.network.ResultWrapper
import roadfriend.app.data.repository.DataManager
import roadfriend.app.ui.base.BaseViewModel
import roadfriend.app.ui.base.IBasePresenter
import roadfriend.app.utils.DummyData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationViewModel(dataManager: DataManager) : BaseViewModel<IBasePresenter>(dataManager) {
    val notificationData: MutableLiveData<ArrayList<NotificationModel>> = MutableLiveData()

    fun getNotificationData() {
        notificationData.value = DummyData.getNotification()
    }

    fun getNotification(request: GetNotificationRequest) =
        viewModelScope.launch {
            getPresenter()?.showLoading()
            when (val result = withContext(Dispatchers.IO) {
                dataManager.getNotification(request)
            }) {
                is ResultWrapper.Success -> {
                    getPresenter()?.hideLoading()
                    notificationData.value = result.data.data
                }
                is ResultWrapper.Error -> {
                    getPresenter()?.hideLoading()
                    getPresenter()?.onError(result.exception.message, result.exception.code)
                }

            }

        }
}