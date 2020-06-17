package roadfriend.app.data.remote.service.maps

import roadfriend.app.data.remote.model.maps.MapsResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IMapsService {
    @GET("maps/api/directions/json")
    fun getDirection(@Query("origin") origin: String,
                     @Query("destination") destination: String,
                     @Query("key") apiKey: String): Deferred<Response<MapsResponse>>
}