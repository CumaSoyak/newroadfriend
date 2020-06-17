package roadfriend.app.ui.base

import androidx.lifecycle.ViewModel
import roadfriend.app.data.repository.DataManager
import java.lang.ref.WeakReference

abstract class BaseViewModel<P>(val dataManager: DataManager) : ViewModel() {

    private lateinit var presenter: WeakReference<P>

    fun getPresenter(): P? {
        return presenter.get()
    }

    fun setPresenter(presenter: P) {
        this.presenter = WeakReference(presenter)
    }
}