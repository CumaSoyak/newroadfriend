package roadfriend.app.utils.extensions

import android.os.Build
import android.telephony.PhoneNumberUtils
import android.text.Html
import android.text.Spanned
import roadfriend.app.CoreApp
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

fun String.toSpanned(): Spanned {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        return Html.fromHtml(this)
    }
}

fun Double.formatMoney(): String {
    val symbols = DecimalFormatSymbols.getInstance(Locale("tr", "TR"))
    symbols.groupingSeparator = '.'
    symbols.decimalSeparator = ','
    val df = DecimalFormat("#,##", symbols)
    df.groupingSize = 3
    df.isGroupingUsed = true
    df.maximumFractionDigits = 2
    df.minimumFractionDigits = 2
    if (this == 0.0) {
        return "0,00"
    } else {
        return df.format(this)
    }
}

val String.jsonObject: JSONObject?
    get() = try {
        JSONObject(this)
    } catch (e: JSONException) {
        null
    }

val String.jsonArray: JSONArray?
    get() = try {
        JSONArray(this)
    } catch (e: JSONException) {
        null
    }

fun getString(id: Int):String{
    return CoreApp.context.getString(id)
}
fun String.phoneFormat():String{
    return PhoneNumberUtils.formatNumber(this)
}
fun String.checkPhoNumber():Boolean{
    return this.length !in 1..9
}

