package roadfriend.app.ui.tripdetail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import roadfriend.app.CoreApp
import roadfriend.app.data.remote.model.base.EmptyResponse
import roadfriend.app.data.remote.model.firebasemessage.NotificationRequest
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.data.remote.service.IUserService
import roadfriend.app.data.repository.DataManager
import roadfriend.app.ui.base.BaseViewModel
import roadfriend.app.ui.base.IBasePresenter
import roadfriend.app.utils.PrefUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TripDetailViewModel(dataManager: DataManager) : BaseViewModel<IBasePresenter>(dataManager) {
    val tripData: MutableLiveData<Trips> = MutableLiveData()

    fun setTrip(trips: Trips) {
        tripData.value = trips
    }

    fun userPhoheUpdate(documentKey: String, phonoNumber: String) {
        CoreApp.db.collection(CoreApp.testDatabase + "users").document(documentKey)
            .update("phone", phonoNumber).addOnSuccessListener {
                val user = PrefUtils.getUser()
                user.phone = phonoNumber
                PrefUtils.createUser(user)
            }
    }

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
