package roadfriend.app.data.remote.model.trips

import retrofit2.http.FieldMap

class TripRequest(
    var status: String,
    @FieldMap
//    var cities: HashMap<String?, Int?>,
    var start_city_id:Int,
    var end_city_id:Int,
    var date: String? = "",
    var time: String? = "",
    var description: String? = ""
)