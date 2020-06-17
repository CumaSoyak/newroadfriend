package roadfriend.app.utils.firebasemessage

import android.app.NotificationManager
import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import roadfriend.app.CoreApp.Companion.context

class NotificationReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onReceive(p0: Context?, intent: Intent?) {
        val remoteInput=RemoteInput.getResultsFromIntent(intent)
        if (remoteInput!=null)
        {
            val mBuilder = NotificationCompat.Builder(context, "birebirdiyetChannel")
                .setSmallIcon(android.R.drawable.ic_menu_info_details)
                .setContentTitle("You have approved the Request")
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, mBuilder.build())
        }
    }
}