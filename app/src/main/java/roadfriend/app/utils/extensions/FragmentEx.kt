package roadfriend.app.utils.extensions

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.getDrawable(drawable: Int): Drawable? {
    return ContextCompat.getDrawable(this.requireContext(),drawable)
}
