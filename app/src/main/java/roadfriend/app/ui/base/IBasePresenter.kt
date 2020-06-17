package roadfriend.app.ui.base


interface IBasePresenter {
    fun showLoading()

    fun hideLoading()

    fun onError(message: String, code: Int)

    fun onSucces(message: String)

}