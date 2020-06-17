package roadfriend.app.data.local.model

import roadfriend.app.data.remote.model.city.City

class Search(
    var type: VALUE,
    var startCity: City?,
    var endCity: City?

)

enum class VALUE {
    FIRSTDATA, FIRSTFILTER,
    SECONDDATA, SECONDFILTER
}