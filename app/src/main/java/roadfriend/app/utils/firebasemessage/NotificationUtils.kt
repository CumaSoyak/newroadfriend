package roadfriend.app.utils.firebasemessage

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import roadfriend.app.R
import roadfriend.app.ui.splash.SplashActivity
import roadfriend.app.utils.extensions.getString

object NotificationUtils {
    private val notificationIcon: Int
        get() {
            val useWhiteIcon =
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
            return if (useWhiteIcon) R.drawable.ic_notification_icon else R.drawable.ic_notification_icon
        }

     fun sendNotification(activity: Activity, messageBody: String?, title: String?) {
        val intent = Intent(activity, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(
            activity,
            0 /* Request code */,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val title = title ?: R.string.app_name

        val channelId = getString(R.string.appname)
        val pattern = longArrayOf(400, 400)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(activity, channelId)
            .setSmallIcon(notificationIcon)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setContentTitle(title.toString())
            .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))
            .setLights(Color.BLUE, 500, 500)
            .setColor(activity.resources.getColor(R.color.green))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setVibrate(pattern)
            .setContentIntent(pendingIntent)

        val notificationManager =
            activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                activity.resources.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

}