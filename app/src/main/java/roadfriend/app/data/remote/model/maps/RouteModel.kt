package roadfriend.app.data.remote.model.maps

import com.google.gson.annotations.SerializedName
import com.google.maps.android.geometry.Bounds


data class RouteModel(
    @SerializedName("bounds")
    var bounds: Bounds?,
    @SerializedName("copyrights")
    var copyrights: String?,
    @SerializedName("legs")
    var legs: List<Leg?>?,
    @SerializedName("overview_polyline")
    var overviewPolyline: OverviewPolyline?,
    @SerializedName("summary")
    var summary: String?,
    @SerializedName("warnings")
    var warnings: List<Any?>?,
    @SerializedName("waypoint_order")
    var waypointOrder: List<Any?>?
)