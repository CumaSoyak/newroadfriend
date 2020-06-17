package roadfriend.app.ui.userdetail

import androidx.lifecycle.MutableLiveData
import roadfriend.app.data.remote.model.comment.Comment
import roadfriend.app.data.remote.model.user.User
import roadfriend.app.data.repository.DataManager
import roadfriend.app.ui.base.BaseViewModel
import roadfriend.app.ui.base.IBasePresenter
import roadfriend.app.utils.DummyData

class UserDetailViewModel(dataManager: DataManager) : BaseViewModel<IBasePresenter>(dataManager) {

    val userDetail: MutableLiveData<User> = MutableLiveData()
    val commentOfUser: MutableLiveData<ArrayList<Comment>> = MutableLiveData()

    fun getCommentOfUser() {
        commentOfUser.value = DummyData.getMyComment()
    }

    fun setUser(user: User?) {
        userDetail.value = user
    }

}
