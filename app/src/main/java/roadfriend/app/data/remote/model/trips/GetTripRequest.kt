package roadfriend.app.data.remote.model.trips

class GetTripRequest(
    val countryCode: String?,
    val status: String?,
    val startCity: String?,
    val endCity: String?
)