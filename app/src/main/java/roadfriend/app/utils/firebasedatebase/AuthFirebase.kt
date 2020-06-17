package roadfriend.app.utils.firebasedatebase

import roadfriend.app.CoreApp
import roadfriend.app.data.remote.model.user.User
import roadfriend.app.utils.PrefUtils
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.*

class AuthFirebase {
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var userIsAvailable: Boolean = true
    val test = CoreApp.testDatabase

    fun getUserLogin(
        email: String,
        password: String,
        userCallBack: (user: User?) -> Unit
    ) {
        val docRef =
            db.collection(test + "users").whereEqualTo("email", email)
                .whereEqualTo("password", password)
        docRef.addSnapshotListener { snapshot, e ->
            if (snapshot?.size() == 0) {
                //kullanıcı yok
                userCallBack(null)
            } else {
                //kullanıcı var
                snapshot?.forEachIndexed { index, queryDocumentSnapshot ->
                    val user: User = queryDocumentSnapshot.toObject(User::class.java)
                    user.firebaseToken = PrefUtils.getFirebaseToken()
                    fireBaseTokenUpdate(user.id)
                    PrefUtils.createUser(user)
                    userCallBack(user)
                }
            }
        }
    }

    fun getUserSocialLogin(
        user: User,
        userCallBack: (user: User?) -> Unit
    ) {
        var first = true

        val docRef = db.collection(test + "users").whereEqualTo("email", user.email)
        docRef.addSnapshotListener { snapshot, e ->
            if (snapshot?.size() == 0) {
                //kullanıcı yok
                if (userIsAvailable) {
                    userIsAvailable = false
                    newUserCreate(user, succes = {
                        if (first) {
                            userCallBack(null)
                            first = false
                        }
                    })
                }
            } else {
                //kullanıcı var
                snapshot?.forEachIndexed { index, queryDocumentSnapshot ->
                    val user: User = queryDocumentSnapshot.toObject(User::class.java)
                    if (first) {
                        user.firebaseToken = PrefUtils.getFirebaseToken()
                        PrefUtils.createUser(user)
                        fireBaseTokenUpdate(user.id)
                        userCallBack(user)
                        first = false
                    }
                }
            }
        }
    }
    fun newUserCreate(user: User, succes: () -> Unit) {
        val id = UUID.randomUUID().toString()
        user.id = id
        db.collection(test + "users")
            .document(id)
            .set(user, SetOptions.merge()).addOnSuccessListener {
                PrefUtils.createUser(user)
                succes.invoke()
            }
    }

    fun fireBaseTokenUpdate(documentKey: String) {
        CoreApp.db.collection(CoreApp.testDatabase + "users").document(documentKey)
            .update("firebaseToken", PrefUtils.getFirebaseToken())
    }
}
//                        val documentKey = queryDocumentSnapshot.reference.path.replace("users/", "")