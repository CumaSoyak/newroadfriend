package roadfriend.app.utils.extensions

import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment

fun Fragment.getDrawable(drawable: Int): Drawable {
    return this.resources.getDrawable(drawable)
}
