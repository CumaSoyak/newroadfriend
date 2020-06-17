package roadfriend.app.utils.extensions

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


inline fun <reified T> modelConvert(data: String): T {
    val replace = GsonBuilder().create().fromJson(data, object : TypeToken<T>() {}.type) as T
    return replace
}
inline fun <reified T> modelReturn(data: String): T {
    val replace = Gson().toJson(data)
    val turnsType = object : TypeToken<T>() {}.type
    val turns = Gson().fromJson<T>(replace, turnsType)
    return turns
}
