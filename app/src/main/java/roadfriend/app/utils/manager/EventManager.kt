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

    fun premiumShow() {
        firebaseAnalytics.logEvent(CoreApp.testDatabase + "premiumShow", Bundle())
    }
    fun setupBillingClient(){
        firebaseAnalytics.logEvent(CoreApp.testDatabase + "onPurchasesUpdatedSetupBillingClientFail", Bundle())
    }
    fun userCancelled(){
        firebaseAnalytics.logEvent(CoreApp.testDatabase + "onPurchasesUpdatedUserCanceled", Bundle())
    }
    fun salesError() {
        firebaseAnalytics.logEvent(CoreApp.testDatabase + "onPurchasesUpdatedError", Bundle())
    }
    fun searchClick() {
        firebaseAnalytics.logEvent(CoreApp.testDatabase + "searchClick"+CoreApp.statusSearch, Bundle())
    }

    fun orderRefuned(trips: Trips) {
        var bundle = Bundle()
        bundle.putParcelable(CoreApp.testDatabase + "trip", trips)
        firebaseAnalytics.logEvent(CoreApp.testDatabase + "order_refund", bundle)
    }

}