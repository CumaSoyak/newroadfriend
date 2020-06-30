package roadfriend.app.ui.intro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import roadfriend.app.databinding.ItemTutorialBinding

class TutorialAdapter() : PagerAdapter() {

    private val items = TutorialType.values()

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        val binding = ItemTutorialBinding.inflate(inflater, container, false)
        binding.tutorialType = TutorialType.values()[position]
        container.addView(binding.root)
        return binding.root
    }
}