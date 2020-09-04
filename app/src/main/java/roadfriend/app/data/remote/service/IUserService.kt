package roadfriend.app.data.remote.service

import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import roadfriend.app.data.remote.model.auth.RegisterResponse
import roadfriend.app.data.remote.model.auth.login.LoginRequest
import roadfriend.app.data.remote.model.auth.login.LoginResponse
import roadfriend.app.data.remote.model.base.EmptyResponse
import roadfriend.app.data.remote.model.firebasemessage.NotificationRequest

interface IUserService {

    @Multipart
    @POST("auth/register")
    fun postRegister(
        @Part image: MultipartBody.Part?,
        @Part("full_name") full_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody
    ): Deferred<Response<RegisterResponse>>

    @POST("auth/login")
    fun postLogin(@Body request: LoginRequest): Deferred<Response<LoginResponse>>

    @Multipart
    @POST("auth/register")
    fun postUserRegister(
        @Part image: MultipartBody.Part?,
        @Part("full_name") full_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody
    ): Deferred<Response<RegisterResponse>>

    @Headers(
        "Content-Type:application/json",
        "Authorization:key=AAAA9RNolwA:APA91bEfSA0YHzSLAgV5LhFqf08mu9P1lT2_wTf50t-eVYSThUanX59H6H4YiOSsrUllFXyfYk1Ie5mbtB8V0imBjiQbEguUL0W6c15UuhzG_HzjQK0I3WvopAh9WQL3WHiXIs44dMmR"
    )
    @POST("fcm/send")
    fun postNotification(@Body body: NotificationRequest): Call<EmptyResponse>

    @POST(" androidpublisher/v3/applications/{packageName}/purchases/subscriptions/{subscriptionId}/tokens/{token}:refund")
    fun postRefundOrder(
        @Path("packageName") packageName: String,
        @Path("subscriptionId") subscriptionId: String,
        @Path("token") token: String
    ): Call<EmptyResponse>
}