package roadfriend.app.ui.message

import androidx.lifecycle.MutableLiveData
import roadfriend.app.data.remote.model.message.MessageModel
import roadfriend.app.data.repository.DataManager
import roadfriend.app.ui.base.BaseViewModel
import roadfriend.app.ui.base.IBasePresenter

class MessageViewModel(dataManager: DataManager) : BaseViewModel<IBasePresenter>(dataManager) {

    val messageModelData: MutableLiveData<ArrayList<MessageModel>> = MutableLiveData()

    fun getMessageData() {

        val list: ArrayList<MessageModel> = arrayListOf()
        list.add(MessageModel(  "dd", "me"))
        list.add(MessageModel(  "dd", "you"))
        list.add(MessageModel(  "dd", "me"))
        list.add(MessageModel(  "dd", "you"))
        list.add(MessageModel(  "dd", "you"))
        list.add(MessageModel(  "dd", "you"))
        messageModelData.value = list
    }

    fun getBid() {
//        getPresenter()?.showLoading()
//        GlobalScope.launch(Dispatchers.Main) {
//            when (val result = withContext(Dispatchers.IO) { dataManager.getMaps() }) {
//                is ResultWrapper.Success -> {
//                    getPresenter()?.hideLoading()
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