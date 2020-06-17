package roadfriend.app.data.repository

import roadfriend.app.data.local.LocalDataManager
import roadfriend.app.data.remote.RemoteDataManager
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

class DataManager(
    private val remoteDataManager: RemoteDataManager,
    private val localDataManager: LocalDataManager
) : IDataManager {

    override suspend fun getTrips(getTripRequest: GetTripRequest): ResultWrapper<TripsResponse> =
        remoteDataManager.getTrips(getTripRequest)

    override suspend fun postTrip(request: TripRequest): ResultWrapper<PostTripResponse> {
        return remoteDataManager.postTrip(request)
    }

    override suspend fun getMaps(
        origin: String,
        destination: String,
        apiKey: String
    ): ResultWrapper<MapsResponse> =
        remoteDataManager.getMaps(origin, destination, apiKey)

    override suspend fun postRegisterUser(
        image: MultipartBody.Part?,
        full_name: RequestBody,
        email: RequestBody,
        password: RequestBody
    ): ResultWrapper<RegisterResponse> =
        remoteDataManager.postRegisterUser(image, full_name, email, password)

    override suspend fun postLoginRequest(request: LoginRequest): ResultWrapper<LoginResponse> =
        remoteDataManager.postLoginRequest(request)

    override suspend fun getCity(countryCode: String): ResultWrapper<CityResponse> {
        return remoteDataManager.getCity(countryCode)
    }

    override suspend fun postTripBid(postTripBidRequest: PostTripBidRequest): ResultWrapper<EmptyResponse> {
        return remoteDataManager.postTripBid(postTripBidRequest)
    }

    override suspend fun getNotification(request: GetNotificationRequest): ResultWrapper<NotificationResponse> {
        return remoteDataManager.getNotification(request)
    }
//    override suspend fun updateProfileAsync(
//        body: RequestBody?,
//        image: MultipartBody.Part?,
//        full_name: RequestBody,
//        email: RequestBody
//    ): ResultWrapper<UserUpdateResponse> =
//        remoteDataManager.updateProfileAsync(body, image, full_name, email)

}