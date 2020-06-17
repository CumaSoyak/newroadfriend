package roadfriend.app.utils.extensions

import android.widget.TextView

fun TextView.setString(int: Int) {
    this.text = context.resources.getString(int)
}