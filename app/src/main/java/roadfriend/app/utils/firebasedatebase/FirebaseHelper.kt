package roadfriend.app.utils.firebasedatebase

import android.annotation.SuppressLint
import android.app.Activity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import roadfriend.app.CoreApp
import roadfriend.app.CoreApp.Companion.db
import roadfriend.app.data.remote.model.city.City
import roadfriend.app.data.remote.model.trips.GetTripRequest
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.data.remote.model.user.User
import roadfriend.app.utils.OtherUtils
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.extensions.getCurrentDate
import roadfriend.app.utils.extensions.showSucces
import java.util.*

class FirebaseHelper {
    private var userIsAvailable: Boolean = true
    val test = CoreApp.testDatabase
    fun chatCreateUser(user: User) {
        val userMe = PrefUtils.getUser()
        //benim chat listeme ekle

        val dataYou = hashMapOf(
            "id" to user.id,
            "fullName" to user.fullName,
            "image" to user.image,
            "firebaseToken" to user.firebaseToken
        )

        db.collection(test + "chat")
            .document(userMe!!.id)
            .collection("info")
            .document(user.id)
            .set(dataYou, SetOptions.merge())


        // onun chat listesine ekle benim datamı ekle

        val dataMe = hashMapOf(
            "id" to userMe.id,
            "fullName" to userMe.fullName,
            "image" to userMe.image,
            "firebaseToken" to userMe.firebaseToken
        )
        db.collection(test + "chat")
            .document(user.id)
            .collection("info")
            .document(userMe.id)
            .set(dataMe, SetOptions.merge())
    }

    fun setNotificationBadge(user: User, type: Boolean) {
        db.collection(test + "notification")
            .document(user.id)
            .set(hashMapOf("message" to type), SetOptions.merge())
    }

    fun setBidNotificationBadge(user: User, type: Boolean) {
        db.collection(test + "notification")
            .document(user.id)
            .set(hashMapOf("bid" to type), SetOptions.merge())
    }

    fun getMessageList(requestCallback: (data: ArrayList<User>) -> Unit) {
        val messageData: ArrayList<User> = arrayListOf()
        val docRef =
            db.collection(test + "chat").document(PrefUtils.getUserId().toString())
                .collection("info")
        docRef.addSnapshotListener { snapshot, e ->
            snapshot?.forEachIndexed { index, queryDocumentSnapshot ->
                val data: User = queryDocumentSnapshot.toObject(User::class.java)
                messageData.add(data)
            }
            requestCallback(messageData)
        }
    }

    fun getNotification(callBack: (result: Boolean) -> Unit) {
        val docRef =
            db.collection(test + "notification").document(PrefUtils.getUserId())
        docRef.addSnapshotListener { snapshot, e ->
            if (snapshot?.data != null)
                if (snapshot.data!!["message"] != null)
                    callBack(snapshot.data!!["message"] as Boolean)
        }
    }

    fun getNotificationBid(callBack: (result: Boolean) -> Unit) {
        val docRef =
            db.collection(test + "notification").document(PrefUtils.getUserId())
        docRef.addSnapshotListener { snapshot, e ->
            if (snapshot?.data != null)
                if (snapshot.data!!["bid"] != null)
                    callBack(snapshot.data!!["bid"] as Boolean)
        }
    }

    fun isAppUpdate(update: (isUpdate: Boolean) -> Unit) {
        var isFirst = true
        val docRef = db.collection(CoreApp.testDatabase + "update")
        docRef.addSnapshotListener { snapshot, e ->
            if (snapshot != null) {
                if (!snapshot.documents.isNullOrEmpty()) {
                    val updateVersion = snapshot.documents.get(0)["lastVersion"].toString()
                    if (updateVersion > OtherUtils.versionNumber()!!) {
                        if (isFirst) {
                            isFirst = false
                            update(true)
                        }
                    } else {
                        if (isFirst) {
                            isFirst = false
                            update(false)

                        }
                    }
                }
            }
        }
    }


    fun createTrip(trips: Trips, result: (trips: Trips, tripId: String) -> Unit) {
        val uuid = UUID.randomUUID().toString()
        trips.documentKey = uuid
        db.collection(test + "trip")
            .document(uuid)
            .set(tripsConvert(trips, uuid), SetOptions.merge()).addOnSuccessListener {
                result(trips, uuid)
            }
    }

    fun getDefaultTrip(
        status: String,
        requestCallback: (data: ArrayList<Trips>) -> Unit
    ) {
        val trips: ArrayList<Trips> = arrayListOf()
        val docRef = db.collection(test + "trip")
            .whereEqualTo("codeCountry", OtherUtils.getCountryCode())
            .whereEqualTo("status", status)
            .orderBy("firebaseTime", Query.Direction.DESCENDING).limit(30)
        docRef.addSnapshotListener { snapshot, e ->
            trips.clear()
            snapshot?.forEachIndexed { index, queryDocumentSnapshot ->
                val data: Trips = queryDocumentSnapshot.toObject(Trips::class.java)
                if (queryDocumentSnapshot["firebaseTime"] != null) {
                    val seconds = (queryDocumentSnapshot["firebaseTime"] as Timestamp)
                    data.firebaseTimeSecond = seconds.seconds
                } else {
                    data.firebaseTimeSecond = 100
                }

                trips.add(data)
            }
            requestCallback(tripsPremimumCalculate(trips))
        }
    }

    fun getFilterTrip(
        getTripRequest: GetTripRequest? = null,
        requestCallback: (data: ArrayList<Trips>) -> Unit
    ) {
        val trips: ArrayList<Trips> = arrayListOf()
        val docRef = db.collection(test + "trip")
            .whereEqualTo("startCityName", getTripRequest?.startCity)
            .whereEqualTo("endCityName", getTripRequest?.endCity)
             .whereEqualTo("codeCountry", OtherUtils.getCountryCode())
            .orderBy("firebaseTime", Query.Direction.DESCENDING)
        docRef.addSnapshotListener { snapshot, e ->
            snapshot?.forEachIndexed { index, queryDocumentSnapshot ->
                val data: Trips = queryDocumentSnapshot.toObject(Trips::class.java)
                if (queryDocumentSnapshot["firebaseTime"] != null) {
                    val seconds = (queryDocumentSnapshot["firebaseTime"] as Timestamp)
                    data.firebaseTimeSecond = seconds.seconds
                } else {
                    data.firebaseTimeSecond = 1000
                }
                trips.add(data)
            }
            requestCallback(tripsPremimumCalculate(trips))
        }
    }


    @SuppressLint("RestrictedApi")
    fun getMyTrip(requestCallback: (data: ArrayList<Trips>) -> Unit) {
        val trips: ArrayList<Trips> = arrayListOf()
        val docRef = db.collection(test + "trip").whereEqualTo("user.id", PrefUtils.getUser()?.id)
            .orderBy("firebaseTime", Query.Direction.DESCENDING)
        docRef.addSnapshotListener { snapshot, e ->
            snapshot?.forEachIndexed { index, queryDocumentSnapshot ->
                val data: Trips = queryDocumentSnapshot.toObject(Trips::class.java)
                trips.add(data)
            }
            requestCallback(trips)
        }
    }

    fun savedTrip(context: Activity, trips: Trips) {
        val uuid = UUID.randomUUID().toString()
        trips.documentKey = uuid
        db.collection(test + "savedTrip")
            .document(userMe().id)
            .collection("info")
            .document(uuid)
            .set(tripsConvert(trips, uuid), SetOptions.merge()).addOnSuccessListener {
                context.showSucces("Yolculuk kaydedilmiştir")
            }
    }


    fun getSavedTrip(requestCallback: (data: ArrayList<Trips>) -> Unit) {
        val trips: ArrayList<Trips> = arrayListOf()
        val docRef =
            db.collection(test + "savedTrip").document(userMe().id).collection("info")
                .orderBy("id", Query.Direction.DESCENDING)
        docRef.addSnapshotListener { snapshot, e ->
            snapshot?.forEachIndexed { index, queryDocumentSnapshot ->
                val data: Trips = queryDocumentSnapshot.toObject(Trips::class.java)
                trips.add(data)
            }
            requestCallback(trips)
        }
    }

    fun tripsConvert(trips: Trips, tripsId: String): HashMap<String, Any?> {

        val admin: Boolean = CoreApp.addAdminTrip

        val time = FieldValue.serverTimestamp()
        val dataMe = hashMapOf(
            "id" to tripsId,
            "user" to trips.user,
            "phone" to trips.phone,
            "date" to trips.date,
            "description" to trips.description,
             "price" to trips.price,
            "paymentType" to trips.paymentType,
            "startCity" to City(
                trips.startCity.name
            ),
            "endCity" to City(
                trips.endCity.name
            ),
            "startCityName" to trips.startCityName,
            "endCityName" to trips.endCityName,
            "bidOption" to trips.bidOption,
            "firebaseToken" to PrefUtils.getFirebaseToken(),
            "firebaseTime" to time,
            "codeCountry" to OtherUtils.getCountryCode(),
            "documentKey" to trips.documentKey,
            "purchaseToken" to trips.purchaseToken,
            "adminPost" to admin
        )
        return dataMe
    }


    fun getUserRegister(email: String, isUserAvailable: (isAvailable: Boolean) -> Unit) {
        val docRef = db.collection(test + "users").whereEqualTo("email", email)
        docRef.addSnapshotListener { snapshot, e ->
            if (snapshot?.size() == 0) {
                //kullanıcı yok
                if (userIsAvailable) {
                    isUserAvailable(false)
                    userIsAvailable = false
                }
            } else {
                //kullanıcı var
                if (userIsAvailable) {
                    isUserAvailable(true)
                    userIsAvailable = true
                }
            }
        }
    }


    fun userMe(): User {
        return PrefUtils.getUser()

    }


    fun tripsPremimumCalculate(trips: ArrayList<Trips>): ArrayList<Trips> {
        //1 gün 86400
        //1 hafta 604800
        //1 ay 2629743

        val tripList: ArrayList<Trips> = arrayListOf()
        tripList.clear()
        val current = getCurrentDate()

        val nowTrips = trips.filter {
            (current - it.firebaseTimeSecond!!) < 200
        }
        val premium = trips.filter {
            it.paymentType.equals("day")
                    || it.paymentType.equals("week")
                    || it.paymentType.equals("monday")

        }
        val notPremium = trips.filter {
            !it.paymentType.equals("day")
                    && !it.paymentType.equals("week")
                    && !it.paymentType.equals("monday") && (current - it.firebaseTimeSecond!!) >= 200

        }

        tripList.addAll(nowTrips)
        tripList.addAll(premium)
        tripList.addAll(notPremium)

        return tripList
    }
}