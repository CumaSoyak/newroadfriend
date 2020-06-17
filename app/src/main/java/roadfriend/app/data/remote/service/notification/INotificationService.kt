package roadfriend.app.data.remote.service.notification

import roadfriend.app.data.remote.model.notification.NotificationResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.POST

interface INotificationService {
    @POST("auth/login")
    fun getNotification(): Deferred<Response<NotificationResponse>>
}