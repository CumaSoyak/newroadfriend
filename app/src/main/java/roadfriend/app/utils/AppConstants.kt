package roadfriend.app.utils

import roadfriend.app.BuildConfig


object AppConstants {
    const val DELAY_TIME: Long = 3000
    var API_URL = BuildConfig.BASE_URL
    const val REQUEST_TIMEOUT: Long = 60
    const val DB_NAME = "birebirdiyet.db"
    const val PREF_NAME = "birebirdiyetpref"
    const val ADD_SEARCH = "ADD_SEARCH"
    const val ADD_SEARCH_STATION = "ADD_SEARCH_STATION"
    const val HOME_SEARCH = "HOME_SEARCH"
    const val IS_USER_LOGGED = "is_user_logged"
    const val IS_RATED = "is_rated"
    const val IS_PHONE_AVAILABLE = "is_phoneis_available"
    const val USER_DETAIL = "USER_DETAIL"
    const val TOKEN = "token"
    const val FIRABESE_TOKEN = "FIRABESE_TOKEN"
    const val CITY = "CITY_OF_COUNTRY"
    const val KEEP_SEASRCH = "KEEP_SEASRCH"
    const val TRAVEL = "TRAVEL"
    const val GOOGLE_SIGN_IN = 100
    const val SPACE = " "
    const val OAUTH2 = "oauth2:"



    /***/
    const val NOTIFICATION_TYPE_APP = "NOTIFICATION_TYPE_APP"
    const val NOTIFICATION_TYPE_FOND_ROUTE = "NOTIFICATION_TYPE_FOND_ROUTE"
    const val NOTIFICATION_TYPE_BID = "NOTIFICATION_TYPE_BID"


}