package roadfriend.app

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
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
        lateinit var firebaseAnalytics: FirebaseAnalytics
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        configureDi()
        MobileAds.initialize(this) {

        }
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

    private fun configureDi() = startKoin {
        androidLogger()
        androidContext(this@CoreApp)
        modules(appModule)
     }

}