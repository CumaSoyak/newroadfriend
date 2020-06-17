package roadfriend.app.utils.extensions

import android.app.Activity
import android.view.MenuItem
import android.widget.EditText
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import roadfriend.app.CoreApp.Companion.context
import roadfriend.app.R
import com.tapadoo.alerter.Alerter

fun Fragment.showError(message: String?) {
    Alerter.create(activity)
        .setTitle(message ?: "")
        .setBackgroundColorRes(R.color.red_ribbon)
        .show()
}

fun Fragment.showSucces(message: String?) {
    Alerter.create(activity)
        .setTitle(message ?: "")
        .setBackgroundColorRes(R.color.lima)
        .show()
}

fun Activity.showError(message: String?) {
    Alerter.create(this)
        .setTitle(message ?: "")
        .setBackgroundColorRes(R.color.red_ribbon)
        .show()
}

fun Activity.showSucces(message: String?) {
    Alerter.create(this)
        .setTitle(message ?: "")
        .setBackgroundColorRes(R.color.lima)
        .show()
}

fun showPopup(mailStart: String, view: EditText) {
    val popup: PopupMenu
    popup = PopupMenu(context, view)
    popup.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener,
        PopupMenu.OnMenuItemClickListener {
        override fun onMenuItemClick(item: MenuItem?): Boolean {
            popup.dismiss()
            view.setText(mailStart + item?.title)
            return true
        }
    })
    popup.menu.add(mailStart + "@gmail.com")
    popup.menu.add(mailStart + "@hotmail.com")
    popup.menu.add(mailStart + "@yahoo.com")
    popup.menu.add(mailStart + "@outlook.com")
    popup.show()
}
