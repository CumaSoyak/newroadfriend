package roadfriend.app.data.remote

import android.util.Log
import roadfriend.app.data.remote.model.auth.RegisterResponse
import roadfriend.app.data.remote.model.auth.login.LoginRequest
import roadfriend.app.data.remote.model.auth.login.LoginResponse
import roadfriend.app.data.remote.model.base.EmptyResponse
import roadfriend.app.data.remote.model.city.CityResponse
import roadfriend.app.data.remote.model.maps.MapsResponse
import roadfriend.app.data.remote.model.notification.GetNotificationRequest
import roadfriend.app.data.remote.model.notification.NotificationResponse
import roadfriend.app.data.remote.model.trips.*
import roadfriend.app.data.remote.network.RemoteDataException
import roadfriend.app.data.remote.network.ResultWrapper
import roadfriend.app.data.remote.service.IUserService
import roadfriend.app.data.remote.service.maps.IMapsService
import roadfriend.app.data.remote.service.notification.INotificationService
import roadfriend.app.data.remote.service.trip.ITripService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class RemoteDataManager(
    private val iTripService: ITripService,
    private val iMapsService: IMapsService,
    private val iUserService: IUserService,
    private val iNotificationService: INotificationService
) : IRemoteDataManager {


    override suspend fun getTrips(getTripRequest: GetTripRequest): ResultWrapper<TripsResponse> =
        withContext(Dispatchers.Main) {
            resultWrapper(iTripService.getTrip(getTripRequest.countryCode,getTripRequest.startCity,getTripRequest.endCity))
        }

    override suspend fun postTrip(request: TripRequest): ResultWrapper<PostTripResponse> {
        return withContext(Dispatchers.Main) {
            resultWrapper(iTripService.postTrip(request))
        }
    }

    override suspend fun getMaps(
        origin: String,
        destination: String,
        apiKey: String
    ): ResultWrapper<MapsResponse> =
        withContext(Dispatchers.Main) {
            resultWrapper(iMapsService.getDirection(origin, destination, apiKey))
        }


    override suspend fun postLoginRequest(request: LoginRequest): ResultWrapper<LoginResponse> =
        resultWrapper(iUserService.postLogin(request))

//    override suspend fun updateProfileAsync(
//        body: RequestBody?,
//        image: MultipartBody.Part?,
//        full_name: RequestBody,
//        email: RequestBody
//    ): ResultWrapper<UserUpdateResponse> = withContext(Dispatchers.Main) {
//        resultWrapper(iUserService.postUserUpdateProfile(body, image, full_name, email))
//    }

    override suspend fun postRegisterUser(
        image: MultipartBody.Part?,
        full_name: RequestBody,
        email: RequestBody,
        password: RequestBody
    ): ResultWrapper<RegisterResponse> {
        return resultWrapper(iUserService.postUserRegister(image, full_name, email, password))
    }

    override suspend fun getCity(countryCode: String): ResultWrapper<CityResponse> {
        return resultWrapper(iTripService.getCity())
    }

    override suspend fun postTripBid(postTripBidRequest: PostTripBidRequest): ResultWrapper<EmptyResponse> {
        return resultWrapper(iTripService.postTripBid())
    }

    override suspend fun getNotification(request: GetNotificationRequest): ResultWrapper<NotificationResponse> {
        return resultWrapper(iNotificationService.getNotification())
    }

    private suspend inline fun <reified T : Any> resultWrapper(request: Deferred<Response<T>>): ResultWrapper<T> {
        return try {
            val response = request.await()
            if (response.isSuccessful) {
                ResultWrapper.Success(response.body()!!)
            } else {
                Log.i("Responde code=>>>>>>>", response.code().toString())
                ResultWrapper.Error(RemoteDataException(response.errorBody(), response.code()))
            }
        } catch (ex: Throwable) {
            Log.i("ServisHata", ex.message.toString())
            ResultWrapper.Error(RemoteDataException(ex))
        }
    }

}