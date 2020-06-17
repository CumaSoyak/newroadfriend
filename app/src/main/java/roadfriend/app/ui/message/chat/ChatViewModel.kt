package roadfriend.app.ui.message.chat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import roadfriend.app.data.remote.model.base.EmptyResponse
import roadfriend.app.data.remote.model.firebasemessage.NotificationRequest
import roadfriend.app.data.remote.model.message.MessageModel
import roadfriend.app.data.remote.service.IUserService
import roadfriend.app.data.repository.DataManager
import roadfriend.app.ui.base.BaseViewModel
import roadfriend.app.ui.base.IBasePresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatViewModel(dataManager: DataManager) : BaseViewModel<IBasePresenter>(dataManager) {
    val chatData: MutableLiveData<ArrayList<MessageModel>> = MutableLiveData()

    fun postNotification(request: NotificationRequest) {
        val apiServices = RetrofitClient.apiServices()
        apiServices.postNotification(request).enqueue(object : Callback<EmptyResponse> {
            override fun onResponse(
                call: Call<EmptyResponse>,
                response: Response<EmptyResponse>
            ) {
                Log.i("Push", "Succes")
            }

            override fun onFailure(call: Call<EmptyResponse>, t: Throwable) {
                Log.i("Push", t.message + "")
            }
        })
    }

    private object RetrofitClient {
        fun apiServices(): IUserService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://fcm.googleapis.com/")
                .build()

            return retrofit.create<IUserService>(IUserService::class.java)
        }
    }
}
