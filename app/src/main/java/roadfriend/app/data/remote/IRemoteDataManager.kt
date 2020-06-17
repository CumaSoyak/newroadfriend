package roadfriend.app.data.remote

import roadfriend.app.data.remote.model.auth.RegisterResponse
import roadfriend.app.data.remote.model.auth.login.LoginRequest
import roadfriend.app.data.remote.model.auth.login.LoginResponse
import roadfriend.app.data.remote.model.base.EmptyResponse
import roadfriend.app.data.remote.model.city.CityResponse
import roadfriend.app.data.remote.model.maps.MapsResponse
import roadfriend.app.data.remote.model.notification.GetNotificationRequest
import roadfriend.app.data.remote.model.notification.NotificationResponse
import roadfriend.app.data.remote.model.trips.*
import roadfriend.app.data.remote.network.ResultWrapper
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface IRemoteDataManager {
    suspend fun getTrips(getTripRequest: GetTripRequest): ResultWrapper<TripsResponse>
    suspend fun getMaps(
        origin: String,
        destination: String,
        apiKey: String
    ): ResultWrapper<MapsResponse>

    suspend fun postRegisterUser(
        image: MultipartBody.Part?,
        full_name: RequestBody,
        email: RequestBody,
        password: RequestBody
    ): ResultWrapper<RegisterResponse>

    suspend fun postLoginRequest(request: LoginRequest): ResultWrapper<LoginResponse>

    suspend fun postTrip(request: TripRequest): ResultWrapper<PostTripResponse>

    suspend fun getCity(countryCode: String): ResultWrapper<CityResponse>

    suspend fun postTripBid(postTripBidRequest: PostTripBidRequest): ResultWrapper<EmptyResponse>

    suspend fun getNotification(request: GetNotificationRequest): ResultWrapper<NotificationResponse>

//    suspend fun updateProfileAsync(
//        body: RequestBody?,
//        image: MultipartBody.Part?,
//        full_name: RequestBody,
//        email: RequestBody
//    ): ResultWrapper<UserUpdateResponse>

}