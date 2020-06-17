package roadfriend.app.ui.intro

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import roadfriend.app.R
import roadfriend.app.ui.base.BaseActivity
import roadfriend.app.ui.main.MainActivity
import roadfriend.app.utils.extensions.launchActivity
import roadfriend.app.utils.helper.PageTransformerZoomOut
import kotlinx.android.synthetic.main.activity_intro.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class IntroActivity : BaseActivity() {
    override val layoutId: Int?
        get() = R.layout.activity_intro

    internal var currentPage = 1
    internal val lastPage = 3 // 0, 1, 2
    private val fragments = Vector<Fragment>()

    private val viewModel by viewModel<IntroViewModel>()

    private val mConstraintSetStepMain by lazy { ConstraintSet() }
    private var mPagerAdapter: IntroPagerAdapter? = null

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        setUpViewPager()
        setUpAdapter()
    }

    private fun setUpAdapter() {

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        mPagerAdapter = IntroPagerAdapter(supportFragmentManager, fragments)
        vpIntro.offscreenPageLimit = 1
        vpIntro.setPageTransformer(true, PageTransformerZoomOut())
        vpIntro.adapter = mPagerAdapter
        indicatorIntro.setViewPager(vpIntro)
        mPagerAdapter!!.registerDataSetObserver(indicatorIntro.dataSetObserver)

        addSlide(
            IntroFragment.newInstance(
                getString(R.string.appname),
                getString(R.string.appname),
                R.drawable.ic_notifications
            )
        )
        addSlide(
            IntroFragment.newInstance(
                getString(R.string.appname),
                getString(R.string.appname),
                R.drawable.ic_notifications
            )
        )
        addSlide(
            IntroFragment.newInstance(
                getString(R.string.appname),
                getString(R.string.appname),
                R.drawable.ic_notifications
            )
        )
        mConstraintSetStepMain.clone(clIntro)
    }


    private fun addSlide(fragment: Fragment) {

        fragments.add(fragment)
        mPagerAdapter!!.notifyDataSetChanged()

    }

    override fun initListener() {
    }

    fun setUpViewPager() {
        vpIntro.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                currentPage = position
                if (position == lastPage) {
                    btnIntro.text = resources.getString(R.string.intro_start)
                }

            }
        })

        btnIntro.setOnClickListener {
            if (currentPage < lastPage) {
                vpIntro.currentItem = currentPage + 1
                if (currentPage == lastPage-1) {
                    btnIntro.setActive()
                }
            } else {
                passRegisterPage()
            }
        }
    }

    private fun passRegisterPage() {
        launchActivity<MainActivity> { }
    }

    class IntroPagerAdapter(fm: FragmentManager, private val fragments: List<Fragment>) :
        FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return this.fragments[position]
        }

        override fun getCount(): Int {
            return this.fragments.size
        }

        fun getSelectedFragment(pos: Int): Fragment? {
            try {
                return instantiateItem(null!!, pos) as Fragment

            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return null
        }
    }

}
