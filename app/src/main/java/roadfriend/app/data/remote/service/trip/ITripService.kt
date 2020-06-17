package roadfriend.app.data.remote.service.trip

import roadfriend.app.data.remote.model.base.EmptyResponse
import roadfriend.app.data.remote.model.city.CityResponse
import roadfriend.app.data.remote.model.trips.PostTripResponse
import roadfriend.app.data.remote.model.trips.TripRequest
import roadfriend.app.data.remote.model.trips.TripsResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ITripService {
    @GET("trips")
    fun getTrip(
        @Query("status") status: String?,
        @Query("startCityTitle") startCityTitle: String?,
        @Query("endCityTitle") endCityTitle: String?
    ): Deferred<Response<TripsResponse>>

    @POST("trips")
    fun postTrip(@Body request: TripRequest): Deferred<Response<PostTripResponse>>

    @GET("city")
    fun getCity(): Deferred<Response<CityResponse>>

    @POST("trips")
    fun postTripBid(): Deferred<Response<EmptyResponse>>
}