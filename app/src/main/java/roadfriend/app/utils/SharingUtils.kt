package roadfriend.app.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri


object SharingUtils {
    val endString=" uygulaması yüklü değil."
//
//    fun maps(context: Context, latitute: Double?, longitute: Double?) {
//        var strUri = ""
//        if (PrefUtils.getUserLastLocation().latitude.isNullOrEmpty() && PrefUtils.getUserLastLocation().longitude.isNullOrEmpty())
//            strUri = "http://maps.google.com/maps?saddr=$latitute,$longitute"
//        else
//         strUri="https://www.google.com/maps/dir/?api=1&origin=$latitute,$longitute&destination=${PrefUtils.getUserLastLocation().latitude},${PrefUtils.getUserLastLocation().longitude}&travelmode=driving"
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
//
//        intent.setPackage("com.google.android.apps.maps")
//
//        context.startActivity(intent)
//    }

    fun email(context: Context, emailAdress: String) {

        val emailIntent = Intent(
            Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", emailAdress, null
            )
        )
        context.startActivity(Intent.createChooser(emailIntent, ""))
    }

    fun website(context: Context, webSiteUrl: String) {

        if (!webSiteUrl.isNullOrEmpty()) {
            var reultUrl = ""
            if (!webSiteUrl!!.startsWith("https://") && !webSiteUrl.startsWith("http://")) {
                reultUrl = "http://$webSiteUrl"
            }
            val openUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(reultUrl))
            context.startActivity(openUrlIntent)
        }
    }

    fun phone(context: Context, phone: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + phone)
        context.startActivity(intent)
    }

    fun instagram(context: Context, instagramUrl: String) {
        val uri = Uri.parse("http://instagram.com/_u/"+instagramUrl)
        val likeIng = Intent(Intent.ACTION_VIEW, uri)

        likeIng.setPackage("com.instagram.android")

        try {
            context.startActivity(likeIng)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/"+instagramUrl)
                )
            )
        }

    }




}