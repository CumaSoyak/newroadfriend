package roadfriend.app.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import roadfriend.app.CoreApp
import roadfriend.app.data.remote.model.city.City
import roadfriend.app.data.remote.model.user.User
import roadfriend.app.utils.AppConstants.FIRABESE_TOKEN
import roadfriend.app.utils.AppConstants.IS_RATED
import roadfriend.app.utils.AppConstants.IS_USER_LOGGED
import roadfriend.app.utils.AppConstants.KEEP_SEASRCH
import roadfriend.app.utils.AppConstants.PREF_NAME
import roadfriend.app.utils.AppConstants.TOKEN
import roadfriend.app.utils.AppConstants.USER_DETAIL
import roadfriend.app.utils.OtherUtils.getJsonDataFromAsset
import roadfriend.app.utils.extensions.get
import roadfriend.app.utils.extensions.removeValue
import roadfriend.app.utils.extensions.setValue

object PrefUtils {
    private const val FIRST_TIME = "is_First_Time_Open"


    private val instance: SharedPreferences =
        CoreApp.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun checkIsFirstTimeOpen(): Boolean {
        val firstTime = instance.getBoolean(FIRST_TIME, true)
        if (firstTime) {
            instance.setValue(FIRST_TIME, false)
            return true
        }
        return false
    }

    fun isLogin(): Boolean = instance.getBoolean(IS_USER_LOGGED, false)

    fun getUser(): User {
        if (instance.getString(USER_DETAIL, "").isNullOrEmpty()) {
            logOut()
            return User()
        } else
            return GsonBuilder().create()
                .fromJson(instance.getString(USER_DETAIL, ""), User::class.java)
    }

    fun getUserId(): String {
        return getUser().id
    }

    fun getToken(): String {
        if (instance.getString(TOKEN, null).isNullOrEmpty()) {
            return ""
        } else {
            return GsonBuilder().create()
                .fromJson(instance.getString(TOKEN, ""), String::class.java)
        }
    }

    fun createUser(user: User) {
        user.firebaseToken = getFirebaseToken()
        val userJson = GsonBuilder().serializeNulls().create().toJson(user)
        if (!isLogin()) {
            instance.setValue(IS_USER_LOGGED, true)
        }
        instance.setValue(USER_DETAIL, userJson)

    }

    fun setRated() {
        instance.setValue(IS_RATED, true)
    }

    fun isRated(): Boolean {
        return instance.getBoolean(IS_RATED, false)
    }

    fun createToken(token: String) {
        instance.setValue(TOKEN, token)
    }


    fun createFirebeseToken(token: String) {
        instance.setValue(FIRABESE_TOKEN, token)
    }

    fun getFirebaseToken(): String {
        return instance.getString(FIRABESE_TOKEN, "")!!
    }


    fun getCity(countryCode: String): ArrayList<City>? {
        val turnsType = object : TypeToken<ArrayList<City>>() {}.type
        val turns = Gson().fromJson<ArrayList<City>>(getJsonDataFromAsset(countryCode), turnsType)
        return turns
    }


    fun remove(key: String) {
        instance.removeValue(key)
    }

    fun get(key: String, value: Any?) {
        instance.get(key, value)
    }

    fun logOut() {
        remove(USER_DETAIL)
        remove(TOKEN)
        instance.setValue(IS_USER_LOGGED, false)
    }


}