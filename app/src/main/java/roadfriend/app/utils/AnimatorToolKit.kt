package roadfriend.app.utils

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.RotateAnimation
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce

object AnimatorToolKit {

    fun scaleUp(view: View, bounce: Float) {
        scaleUp(view, 0, bounce)
    }


    fun scaleUp(view: View, delay: Long = 0, bounce: Float = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY) {
        view.tag = "animate"

        val pvhsX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f)
        val pvhsY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f)
        val scaler = ObjectAnimator.ofPropertyValuesHolder(view, pvhsX, pvhsY)
        scaler.duration = 300
        scaler.startDelay = delay
        scaler.addUpdateListener { animation ->
            if (view.tag == "animate" && animation.animatedFraction >= 0.7) {
                view.tag = ""
                val spring = SpringForce(1f).setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
                    .setStiffness(SpringForce.STIFFNESS_LOW)

                val anim = SpringAnimation(view, DynamicAnimation.SCALE_Y).setStartVelocity(4f)
                    .setSpring(spring)
                val anim2 = SpringAnimation(view, DynamicAnimation.SCALE_X).setStartVelocity(4f)
                    .setSpring(spring)
                anim.start()
                anim2.start()
            }
        }
        scaler.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

                view.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator) {


            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        scaler.start()

    }

    fun scaleUp(view: View, duration: Long) {
        view.tag = "animate"

        val pvhsX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f)
        val pvhsY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f)
        val scaler = ObjectAnimator.ofPropertyValuesHolder(view, pvhsX, pvhsY)
        scaler.duration = duration
        scaler.startDelay = duration
        scaler.addUpdateListener { animation ->
            if (view.tag == "animate" && animation.animatedFraction >= 0.7) {
                view.tag = ""
                val spring = SpringForce(1f).setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
                    .setStiffness(SpringForce.STIFFNESS_LOW)

                val anim = SpringAnimation(view, DynamicAnimation.SCALE_Y).setStartVelocity(4f)
                    .setSpring(spring)
                val anim2 = SpringAnimation(view, DynamicAnimation.SCALE_X).setStartVelocity(4f)
                    .setSpring(spring)
                anim.start()
                anim2.start()
            }
        }
        scaler.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

                view.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator) {


            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        scaler.start()

    }

    fun scaleUpY(view: View) {
        view.tag = "animate"

        val pvhsY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f)
        val scaler = ObjectAnimator.ofPropertyValuesHolder(view, pvhsY)
        scaler.duration = 300
        scaler.addUpdateListener { animation ->
            if (view.tag == "animate" && animation.animatedFraction >= 0.7) {
                view.tag = ""
                val spring = SpringForce(1f).setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
                    .setStiffness(SpringForce.STIFFNESS_LOW)

                val anim = SpringAnimation(view, DynamicAnimation.SCALE_Y).setStartVelocity(4f)
                    .setSpring(spring)
                anim.start()
            }
        }
        scaler.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

                view.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator) {


            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        scaler.start()

    }

    @JvmOverloads
    fun scaleDown(view: View, scaleAnimationListener: ScaleAnimationListener? = null) {

        val pvhsX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f)
        val pvhsY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f)
        val scaler = ObjectAnimator.ofPropertyValuesHolder(view, pvhsX, pvhsY)
        scaler.duration = 200
        scaler.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                scaleAnimationListener?.onAnimationStart()
            }

            override fun onAnimationEnd(animation: Animator) {
                view.visibility = View.GONE
                scaleAnimationListener?.onAnimationEnd()
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        scaler.start()

    }


    fun scaleDownY(view: View) {
        scaleDown(view, null)
    }

    fun scaleDownY(view: View, scaleAnimationListener: ScaleAnimationListener?) {

        val pvhsY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f)
        val scaler = ObjectAnimator.ofPropertyValuesHolder(view, pvhsY)
        scaler.duration = 200
        scaler.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                scaleAnimationListener?.onAnimationStart()
            }

            override fun onAnimationEnd(animation: Animator) {
                view.visibility = View.GONE
                scaleAnimationListener?.onAnimationEnd()
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        scaler.start()

    }


    interface ScaleAnimationListener {

        fun onAnimationStart()

        fun onAnimationEnd()
    }


    fun rotateToCross(view: View) {
        val rotateAnimation: RotateAnimation
        rotateAnimation =
            RotateAnimation(0f, 135f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.duration = 150
        rotateAnimation.fillAfter = true
        view.startAnimation(rotateAnimation)
    }

    fun rotateToPlus(view: View) {
        val rotateAnimation: RotateAnimation
        rotateAnimation =
            RotateAnimation(135f, 0f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.duration = 150
        rotateAnimation.fillAfter = true
        view.startAnimation(rotateAnimation)
    }

    fun rotateArrowDown(view: View) {
        // rotate counterclockwise
        val rotateAnimation: RotateAnimation
        rotateAnimation =
            RotateAnimation(90f, 0f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.duration = 100
        rotateAnimation.fillAfter = true
        view.startAnimation(rotateAnimation)
    }

    fun rotateArrowRight(view: View) {
        // rotate clockwise
        val rotateAnimation: RotateAnimation
        rotateAnimation =
            RotateAnimation(0f, 90f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.duration = 100
        rotateAnimation.fillAfter = true
        view.startAnimation(rotateAnimation)
    }


    private fun moveView(view: View, from: Int, to: Int) {
        val mover = ObjectAnimator.ofFloat(view, "translationY", from.toFloat(), to.toFloat())
        mover.duration = 400
        mover.start()
    }


    fun scaleDownAndMove(view: View, from: Int, to: Int) {

        val pvhsX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.6f)
        val pvhsY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.6f)
        val scaler = ObjectAnimator.ofPropertyValuesHolder(view, pvhsX, pvhsY)
        scaler.duration = 300
        scaler.start()

        moveView(view, from, to)
    }

    fun animateColor(view: View, colorFrom: Int, colorTo: Int) {
        val colorAnimation = ValueAnimator.ofObject(android.animation.ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 300 // milliseconds
        colorAnimation.addUpdateListener { animator -> view.setBackgroundColor(animator.animatedValue as Int) }
        colorAnimation.start()
    }

}
