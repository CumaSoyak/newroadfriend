package roadfriend.app.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.appcompat.widget.AppCompatEditText

class CVTextInputEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr), PopupMenu.OnMenuItemClickListener {
    init {
        listenE(context)
    }

    private fun listenE(context: Context) {
//        this.listener { text ->
//            if (text.contains("@")) {
//                showPopup(this.textString(), this)
//            }
//        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        this.setText(this.text.toString() + "@" + item?.title)
        return true
    }
}