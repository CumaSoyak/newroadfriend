package roadfriend.app.utils.firebasedatebase

import android.app.Activity
import roadfriend.app.CoreApp
import roadfriend.app.CoreApp.Companion.db
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.extensions.showError
import roadfriend.app.utils.extensions.showSucces
import com.google.firebase.firestore.SetOptions
import roadfriend.app.R

class TripFirebase {
    val test = CoreApp.testDatabase
    fun deleteTrip(context: Activity, document: String) {
        db.collection(test + "trip").document(document)
            .delete()
            .addOnSuccessListener {
                context.showSucces(context.getString(R.string.yolculuk_silinmistir))
                deleteBidList(context, document)
            }
            .addOnFailureListener {
                context.showError("Bir şeyler ters gitti tekrar deneyiniz")
            }
    }
    fun deleteTripAccount(context: Activity, document: String) {
        db.collection(test + "trip").document(document)
            .delete()
            .addOnSuccessListener {
                context.showSucces(context.getString(R.string.yolculuk_silinmistir))
             }
            .addOnFailureListener {
                context.showError("Bir şeyler ters gitti tekrar deneyiniz")
            }
    }


    fun bidTableCreate(trips: Trips) {
        val userMe = PrefUtils.getUser()

        val bidList = hashMapOf(
            "id" to trips.id,
            "startCityName" to trips.startCityName,
            "endCityName" to trips.endCityName
         )

        db.collection(test + "bidList")
            .document(userMe.id)
            .collection("info")
            .document(trips.id!!)
            .set(bidList, SetOptions.merge())
    }

    fun deleteBidList(context: Activity, document: String) {
        val userMe = PrefUtils.getUser()
        db.collection(test + "bidList")
            .document(userMe.id)
            .collection("info")
            .document(document)
            .delete()
    }

}