package roadfriend.app.utils.manager

import android.os.Bundle
import roadfriend.app.CoreApp
import roadfriend.app.CoreApp.Companion.firebaseAnalytics
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.utils.OtherUtils
import roadfriend.app.utils.helper.TripBundle

object EventManager {

    fun clickNoSearch(tripBundle: TripBundle?) {
        var bundle = Bundle()
        bundle.putString("tripStatus", tripBundle?.tripStatus)
        bundle.putString("cityFirst", tripBundle?.tripsList?.get(0)?.name)
        bundle.putString("citySecond", tripBundle?.tripsList?.get(1)?.name)
        firebaseAnalytics.logEvent("no_search", bundle)
    }

    fun analyticsSendOrderComplete(
        tripData: Trips,
        moneyType: String?
    ) {
        val params = Bundle().apply {
            putString("trip_id", tripData.id)
            putParcelable("user", tripData.user)
            putString("price", moneyType)
            putString("startCity", tripData.startCity.name)
            putString("endCity", tripData.endCity.name)
            putString("country", OtherUtils.getCountryCode())
        }
        firebaseAnalytics.logEvent("satis", params)
    }

    fun clickSatisOnizleme() {
        var bundle = Bundle()
        bundle.putString(CoreApp.testDatabase + "satisOnizleme", "satisOnizleme")
        firebaseAnalytics.logEvent(CoreApp.testDatabase + "satisOnizleme", bundle)
    }
}