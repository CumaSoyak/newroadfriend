package roadfriend.app.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.telephony.TelephonyManager
import roadfriend.app.CoreApp.Companion.context
import roadfriend.app.R
import roadfriend.app.ui.sales.SalesActivity
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


object OtherUtils {

    var europeList: ArrayList<String> = arrayListOf(
        "at",
        "be",
        "bg",
        "dk",
        "fi",
        "fr",
        "de",
        "gr",
        "hu",
        "ıt",
        "ıe",
        "lt",
        "mt",
        "pl",
        "pt",
        "ro",
        "sk",
        "si",
        "es",
        "gb"
    )

    fun versionNumber(): String? {
        var version: String = ""
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            version = pInfo.versionName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return version
    }

    fun countryIsTurkey(): Boolean {
        return getCountryCode().equals("tr")
    }

    fun isEurope(): Boolean {
        return europeList.contains(getCurrentCountryCode())
    }

    fun getCountryCode(): String {
        if (europeList.contains(getCurrentCountryCode())) {
            return "europe"
        } else {
            return getCurrentCountryCode()
        }
    }

    fun getCurrentCountryCode(): String {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryIso = telephonyManager.simCountryIso.toLowerCase()
        return "tr"
    }


    fun getJsonDataFromAsset(fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    fun getCurreny():String{
        if (getCountryCode().equals("europe"))
        {
            return "Eur"
        }
        else{
            return Currency.getInstance(Locale.getDefault()).toString()
        }
    }

    fun openGooglePlay(activity: SalesActivity) {
        val appPackageName = activity.getPackageName()
        try {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + appPackageName)
                )
            )
        } catch (anfe: android.content.ActivityNotFoundException) {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)
                )
            )
        }
    }

    fun introImage(position: Int): Int {
        when (position) {
            1 -> {
                if (countryIsTurkey())
                    return R.drawable.img_select_status_tr
                else
                    return R.drawable.img_select_status_en
            }
            2 -> {
                if (countryIsTurkey())
                    return R.drawable.img_select_city_tr
                else
                    return R.drawable.img_select_city_en

            }
            3 -> {
                if (countryIsTurkey())
                    return R.drawable.img_no_trip_tr
                else
                    return R.drawable.img_no_trip_en

            }
            4 -> {
                if (countryIsTurkey())
                    return R.drawable.img_chat
                else
                    return R.drawable.img_chat

            }
            5 -> {
                if (countryIsTurkey())
                    return R.drawable.img_sales_tr
                else
                    return R.drawable.img_sales_en

            }
            else -> {
                return R.drawable.img_sales_tr
            }
        }

    }


}