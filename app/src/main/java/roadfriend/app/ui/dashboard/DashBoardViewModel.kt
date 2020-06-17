package roadfriend.app.ui.dashboard

import androidx.lifecycle.MutableLiveData
import roadfriend.app.CoreApp
import roadfriend.app.data.repository.DataManager
import roadfriend.app.ui.base.BaseViewModel
import roadfriend.app.ui.base.IBasePresenter

class DashBoardViewModel(dataManager: DataManager) : BaseViewModel<IBasePresenter>(dataManager) {

    val tripSizeData: MutableLiveData<Int> = MutableLiveData()
    val userSizeData: MutableLiveData<Int> = MutableLiveData()

    fun tripSize() {
        val docRef = CoreApp.db.collection("trip")
        docRef.addSnapshotListener { snapshot, e ->
            tripSizeData.value = snapshot!!.size()
        }
    }

    fun userSize() {
        val docRef = CoreApp.db.collection("users")
        docRef.addSnapshotListener { snapshot, e ->
            userSizeData.value = snapshot!!.size()
        }
    }
}