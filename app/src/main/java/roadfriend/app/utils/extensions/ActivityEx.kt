package roadfriend.app.utils.extensions

import android.app.Activity
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import roadfriend.app.CoreApp
import roadfriend.app.R

fun AppCompatActivity.showToast(message: String) {
    Toast.makeText(CoreApp.context, message, Toast.LENGTH_LONG).show()
}

fun Fragment.showToast(message: String?) {
    Toast.makeText(CoreApp.context, message + "", Toast.LENGTH_LONG).show()
}


fun AppCompatActivity.overridePendingTransitionEnter() {
    overridePendingTransition(R.anim.right_to_left_in, R.anim.right_to_left_exit)
}


fun AppCompatActivity.overridePendingTransitionExit() {
    overridePendingTransition(R.anim.left_to_right_in, R.anim.left_to_right_exit)
}

fun Fragment.overridePendingTransitionEnter() {
    this.activity?.overridePendingTransition(R.anim.right_to_left_in, R.anim.right_to_left_exit)
}

fun Activity.getDrawable(drawable: Int): Drawable {
    return this.resources.getDrawable(drawable)
}

fun Activity.TAG(): String {
    return this::class.java.name
}