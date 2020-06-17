package roadfriend.app.ui.maps

import roadfriend.app.data.repository.DataManager
import roadfriend.app.ui.base.BaseViewModel
import roadfriend.app.ui.base.IBasePresenter

class MapViewModel(dataManager: DataManager) : BaseViewModel<IBasePresenter>(dataManager) {
    fun get() {
        getPresenter()?.showLoading()
    }
}
