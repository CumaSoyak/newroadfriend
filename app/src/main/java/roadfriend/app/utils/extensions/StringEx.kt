package roadfriend.app.utils.extensions

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.telephony.PhoneNumberUtils
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import lt.neworld.spanner.Spanner
import lt.neworld.spanner.Spans.foreground
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import roadfriend.app.CoreApp
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
    val symbols = DecimalFormatSymbols.getInstance(Locale.getDefault())
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

fun getString(id: Int): String {
    return CoreApp.context.getString(id)
}

fun String.phoneFormat(): String {
    return PhoneNumberUtils.formatNumber(this)
}

fun String.checkPhoNumber(): Boolean {
    return this.length !in 1..9
}

fun Int.calculateDaySecond(): Int {
    return this * 86400
}

fun TextView.makeLinks(
    vararg links: Triple<String, View.OnClickListener, String>,
    context: Context,
    colorId: Int? = null
) {
    val spannableString = SpannableStringBuilder(this.text)
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.typeface = Typeface.DEFAULT_BOLD
                if (colorId != null) {
                    ds.color = ContextCompat.getColor(context, colorId)
                } else {

                }
            }

            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
            }
        }


        val linkFirstIndex = spannableString.indexOf(link.first)
        spannableString.replace(linkFirstIndex, linkFirstIndex + link.first.length, link.third)

        val startIndexOfLink = spannableString.indexOf(link.third)
        spannableString.setSpan(
            clickableSpan,
            startIndexOfLink,
            startIndexOfLink + link.third.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    this.movementMethod =
        LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun Context.spnableChangeColor(
    fullString: String,
    old: String,
    new: String,
    colorId: Int
): Spanner {
    val replaceString = fullString.replace(old, new)
    return Spanner(replaceString)
        .span(new, foreground(colorId))
}
