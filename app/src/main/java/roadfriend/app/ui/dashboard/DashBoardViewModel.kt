package roadfriend.app.ui.dashboard

import androidx.lifecycle.MutableLiveData
import roadfriend.app.CoreApp
import roadfriend.app.data.remote.model.user.User
import roadfriend.app.data.repository.DataManager
import roadfriend.app.ui.base.BaseViewModel
import roadfriend.app.ui.base.IBasePresenter

class DashBoardViewModel(dataManager: DataManager) : BaseViewModel<IBasePresenter>(dataManager) {

    val tripSizeData: MutableLiveData<Int> = MutableLiveData()
    val userSizeData: MutableLiveData<Int> = MutableLiveData()
    val foreignuserSizeData: MutableLiveData<Int> = MutableLiveData()

    fun tripSize() {
        val docRef = CoreApp.db.collection("trip")
        docRef.addSnapshotListener { snapshot, e ->
            tripSizeData.value = snapshot!!.size()
        }
    }

    fun userSize() {
        var userList: ArrayList<User> = arrayListOf()
        val docRef = CoreApp.db.collection("users")
        docRef.addSnapshotListener { snapshot, e ->
            snapshot?.forEachIndexed { index, queryDocumentSnapshot ->
                val data: User = queryDocumentSnapshot.toObject(User::class.java)
                userList.add(data)
            }
            foreignuserSizeData.value = userList.filter { !it.country.equals("tr") && !it.country.isNullOrEmpty()}.size
            userSizeData.value = snapshot!!.size()
        }
    }
}