package roadfriend.app

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.android.startKoin
import roadfriend.app.di.appModule
import java.io.File


class CoreApp : MultiDexApplication() {

    companion object {
        lateinit var context: Context
        var chatID: String? = null
        lateinit var db: FirebaseFirestore
        var notLogin = false
        var testDatabase = ""
        var addAdminTrip = false
        var statusSearch: String = "1"
        var reklam: Boolean = true
        lateinit var  firebaseAnalytics: FirebaseAnalytics
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        configureDi()
        MobileAds.initialize(this, "ca-app-pub-6449577219947127~7608577534")
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        JodaTimeAndroid.init(this)
        db = FirebaseFirestore.getInstance()

        val googleBug = getSharedPreferences("google_bug_154855417", Context.MODE_PRIVATE)
        if (!googleBug.contains("fixed")) {
            val corruptedZoomTables = File(getFilesDir(), "ZoomTables.data")
            corruptedZoomTables.delete()
            googleBug.edit().putBoolean("fixed", true).apply()
        }

    }

    private fun configureDi() = startKoin(this, appModule)

}