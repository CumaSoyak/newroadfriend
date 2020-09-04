package roadfriend.app.ui.profile.mytrip

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import roadfriend.app.CoreApp.Companion.context
import roadfriend.app.R
import roadfriend.app.data.remote.model.base.EmptyResponse
import roadfriend.app.data.remote.service.IUserService
import roadfriend.app.data.repository.DataManager
import roadfriend.app.ui.base.BaseViewModel
import roadfriend.app.ui.base.IBasePresenter

class MyTripsViewModel(dataManager: DataManager) : BaseViewModel<IBasePresenter>(dataManager) {


    fun postRefundOrder(subscriptionId: String, token: String) {
        getPresenter()?.showLoading()
        val apiServices = RetrofitClient.apiServices()
        apiServices.postRefundOrder("roadfriend.app", subscriptionId, token)
            .enqueue(object : Callback<EmptyResponse> {
                override fun onResponse(
                    call: Call<EmptyResponse>,
                    response: Response<EmptyResponse>
                ) {
                    getPresenter()?.hideLoading()
                    getPresenter()?.onSucces(context.getString(R.string.odeme_iade_edildi))
                }

                override fun onFailure(call: Call<EmptyResponse>, t: Throwable) {
                    getPresenter()?.hideLoading()
                    getPresenter()?.onError(context.getString(R.string.bir_seyler_ters_gitti), 0)
                }
            })
    }

    private object RetrofitClient {
        fun apiServices(): IUserService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://androidpublisher.googleapis.com/")
                .build()

            return retrofit.create<IUserService>(IUserService::class.java)
        }
    }
}
