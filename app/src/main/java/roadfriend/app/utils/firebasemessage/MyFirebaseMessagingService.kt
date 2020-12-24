package roadfriend.app.utils.firebasemessage

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
import roadfriend.app.data.remote.model.user.User
import roadfriend.app.ui.biddetail.BidDetailActivity
import roadfriend.app.ui.message.chat.ChatActivity
import roadfriend.app.ui.splash.SplashActivity
import roadfriend.app.utils.PrefUtils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

    private val notificationIcon: Int
        get() {
            val useWhiteIcon =
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
            return   R.drawable.ic_icon
        }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // console'dan bildirim gönderiliyorsa
        remoteMessage.notification?.let {
            sendNotification(it.body, it.title)
            return
        }
        // postman'den bildirim gönderiliyorsa
        remoteMessage.data.isNotEmpty().let {
            remoteMessage.let {
                val data = it.data
                sendNotificationData(applicationContext!!, remoteMessage.notification, data)
            }
        }


    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        PrefUtils.createFirebeseToken(token)
    }

    private fun sendNotification(messageBody: String?, title: String?) {
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0 /* Request code */,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val title = title ?: R.string.app_name

        val channelId = getString(R.string.appname)
        val pattern = longArrayOf(400, 400)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(notificationIcon)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setContentTitle(title.toString())
            .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))
            .setLights(Color.BLUE, 500, 500)
            .setColor(resources.getColor(R.color.green))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setVibrate(pattern)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                resources.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun sendNotificationData(
        context: Context,
        message: RemoteMessage.Notification?,
        data: Map<String, String>
    ) {
        val user = User(
            id = data["id"]!!.toString(),
            image = data["image"].toString(),
            fullName = data["full_name"].toString()
        )
        var intent: Intent? = null
        if (data["type"].equals("chat")) {
            intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("model", user)
            intent.putExtra("firebaseMessage", "firebaseMessage")
            intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
        } else if (data["type"].equals("bid")) {
            intent = Intent(this, BidDetailActivity::class.java)
            intent.putExtra("model", data["id"])
            intent.putExtra("firebaseMessage", "firebaseMessage")
            intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
        }


        val requestID = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getActivity(
            context, requestID,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )


        val channelId = getString(R.string.appname)
        val pattern = longArrayOf(400, 400)
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(notificationIcon)
            .setContentTitle(data["full_name"])
            .setStyle(NotificationCompat.BigTextStyle().bigText(message?.body))
            .setContentText(data["message"])
            .setLights(Color.BLUE, 500, 500)
            .setAutoCancel(true)
            .setSound(alarmSound)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setContentIntent(pendingIntent)
            .setVibrate(pattern)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())

    }

}
