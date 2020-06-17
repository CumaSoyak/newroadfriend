package roadfriend.app.data.remote.model.maps

import com.google.gson.annotations.SerializedName


data class Maps(
    @SerializedName("geocoded_waypoints")
    var geocodedWaypoints: List<GeocodedWaypoint?>?,
    @SerializedName("routes")
    var routes: List<RouteModel?>?,
    @SerializedName("status")
    var status: String?
)