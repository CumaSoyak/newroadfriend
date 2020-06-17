package roadfriend.app.utils.extensions

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.TintAwareDrawable
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BaseTarget
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import roadfriend.app.CoreApp.Companion.context
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


fun ImageView.tintSrc(@ColorRes colorRes: Int) {
    val drawable = DrawableCompat.wrap(drawable)
    DrawableCompat.setTint(drawable, ContextCompat.getColor(context, colorRes))
    setImageDrawable(drawable)
    if (drawable is TintAwareDrawable) invalidate() // Because in this case setImageDrawable will not call invalidate()
}


fun ImageView.loadFromUrl(url: String) =
    Glide.with(this.context.applicationContext)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)!!

fun ImageView.loadUrlAndPostponeEnterTransition(url: String, activity: FragmentActivity) {
    val target: Target<Drawable> = ImageViewBaseTarget(this, activity)
    Glide.with(context.applicationContext).load(url).into(target)
}

private class ImageViewBaseTarget(var imageView: ImageView?, var activity: FragmentActivity?) :
    BaseTarget<Drawable>() {
    override fun removeCallback(cb: SizeReadyCallback) {
        imageView = null
        activity = null
    }

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        imageView?.setImageDrawable(resource)
        activity?.supportStartPostponedEnterTransition()
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        super.onLoadFailed(errorDrawable)
        activity?.supportStartPostponedEnterTransition()
    }

    override fun getSize(cb: SizeReadyCallback) = cb.onSizeReady(SIZE_ORIGINAL, SIZE_ORIGINAL)
}


fun ImageView.load(imageUrl: String?) {
    Glide.with(context).load(imageUrl).dontAnimate().into(this)
}

fun ImageView.load(@DrawableRes resourceId: Int) {
    Glide.with(context).load(resourceId)
        .into(this)
}

fun ImageView.load(@DrawableRes resourceId: Int, cornersRadius: Int) {
    Glide.with(context).load(resourceId).transform(RoundedCorners(cornersRadius))
        .into(this)
}


fun ImageView.load(imageUrl: String, cornersRadius: Int) {
    Glide.with(context).load(imageUrl).transform(RoundedCorners(cornersRadius))
        .into(this)
}

fun ImageView.loadCircle(path: String) {
    Glide.with(context)
        .load(path)
        .apply(
            RequestOptions()
                .circleCrop()
        )
        .into(this)
}

fun ImageView.filter(color: Int) {
    this.setColorFilter(
        ContextCompat.getColor(context, color),
        PorterDuff.Mode.SRC_IN
    )
}


fun setRecyclerViewFallDownAnimation(itemView: View, i: Int) {

    var i = i

    val isNotFirstItem = i == -1
    i++
    itemView.alpha = 0f

    val animatorSet = AnimatorSet()
    val animator = ObjectAnimator.ofFloat(itemView, "alpha", 0f, 0.5f, 1.0f)

    ObjectAnimator.ofFloat(itemView, "alpha", 0f).start()
    animator.startDelay = if (isNotFirstItem) 300 / 2 else (i * 300 / 3).toLong()
    animator.duration = 500
    animatorSet.play(animator)
    animator.start()


}

/**
 * Loads image with Glide into the [ImageView].
 *
 * @param url url to load
 * @param previousUrl url that already loaded in this target. Needed to prevent white flickering.
 * @param round if set, the image will be round.
 * @param cornersRadius the corner radius to set. Only used if [round] is `false`(by default).
 * @param crop if set to `true` then [CenterCrop] will be used. Default is `false` so [FitCenter] is used.
 */
@SuppressLint("CheckResult")
fun ImageView.load(
    url: String,
    previousUrl: String? = null,
    round: Boolean = false,
    cornersRadius: Int = 0,
    crop: Boolean = false
) {

    val requestOptions = when {
        round -> RequestOptions.circleCropTransform()

        cornersRadius > 0 -> {
            RequestOptions().transforms(
                if (crop) CenterCrop() else FitCenter(),
                RoundedCorners(cornersRadius)
            )
        }

        else -> null
    }

    Glide
        .with(context)
        .load(url)
        .let {
            // Apply request options
            if (requestOptions != null) {
                it.apply(requestOptions)
            } else {
                it
            }
        }
        .let {
            // Workaround for the white flickering.
            // See https://github.com/bumptech/glide/issues/527
            // Thumbnail changes must be the same to catch the memory cache.
            if (previousUrl != null) {
                it.thumbnail(
                    Glide
                        .with(context)
                        .load(previousUrl)
                        .let {
                            // Apply request options
                            if (requestOptions != null) {
                                it.apply(requestOptions)
                            } else {
                                it
                            }
                        }
                )
            } else {
                it
            }
        }
        .into(this)
}

fun getUri(url: String, uriCallBack: (uri: Uri) -> Unit) {
    Glide.with(context)
        .asBitmap()
        .load(url)
        .into(object : CustomTarget<Bitmap>() {
            override fun onLoadCleared(placeholder: Drawable?) {
            }

            override fun onResourceReady(
                resource: Bitmap,
                transition: Transition<in Bitmap>?
            ) {
                uriCallBack(bitmapToUri(resource))
            }
        })
}

fun bitmapToUri(bitmap: Bitmap): Uri {
    // Get the context wrapper
    val wrapper = ContextWrapper(context)

    var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
    file = File(file, "${UUID.randomUUID()}.jpg")

    try {
        // Compress the bitmap and save in jpg format
        val stream: OutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    // Return the saved bitmap uri
    return Uri.parse(file.absolutePath)
}