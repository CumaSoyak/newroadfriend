package roadfriend.app.utils.extensions

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment


fun Activity.logger(message: String) {
    Log.d(this.localClassName, message)
}

fun Activity.logger(message: Int) {
    Log.d(this.localClassName, message.toString())
}

fun Fragment.logger(message: String) {
    Log.d(this.activity?.localClassName, message)
}

fun Fragment.logger(message: Int) {
    Log.d(this.activity?.localClassName, message.toString())
}
