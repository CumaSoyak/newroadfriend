package roadfriend.app.ui.homemain


import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import roadfriend.app.CoreApp
import roadfriend.app.R
import roadfriend.app.data.local.model.Search
import roadfriend.app.data.local.model.VALUE
import roadfriend.app.ui.base.BaseFragment
import roadfriend.app.ui.home.FirstFragment
import roadfriend.app.ui.home.HomeViewModel
import roadfriend.app.ui.home.SecondFragment
import roadfriend.app.utils.extensions.gone
import roadfriend.app.utils.extensions.visible
import roadfriend.app.utils.helper.LiveBus
import roadfriend.app.utils.helper.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_home_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class HomeMainFragment : BaseFragment() {

    private val viewModel by viewModel<HomeViewModel>()

    companion object {
        val TAG: String = HomeMainFragment::class.java.name
        fun newInstance(): HomeMainFragment = HomeMainFragment().apply {}
    }

    override val layoutId: Int?
        get() = R.layout.fragment_home_main

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI(view: View) {
    }

    override fun initListener() {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(FirstFragment(), resources.getString(R.string.add_arac_ariyorum))
        adapter.addFragment(SecondFragment(), resources.getString(R.string.add_yuk_ariyorum))
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)


        cvSearchFirst.openSearchCityDialog(this, callBackFilter = {
            LiveBus.setAndSendSticky(Search(VALUE.FIRSTDATA, null, null))
        }) { startCity, endCity, status ->
            LiveBus.setAndSendSticky(Search(VALUE.FIRSTFILTER, startCity, endCity))
        }
        cvSearchSecond.openSearchCityDialog(this, callBackFilter = {
            LiveBus.setAndSendSticky(Search(VALUE.SECONDDATA, null, null))
        }) { startCity, endCity, status ->
            LiveBus.setAndSendSticky(Search(VALUE.SECONDFILTER, startCity, endCity))
        }
//        tabs.getTabAt(0)?.setIcon(getDrawable(R.drawable.ic_truck))
//        tabs.getTabAt(1)?.setIcon(getDrawable(R.drawable.ic_boxes))

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    CoreApp.statusSearch = "1"
                    cvSearchFirst.visible()
                    cvSearchSecond.gone()
                } else {
                    CoreApp.statusSearch = "2"
                    cvSearchFirst.gone()
                    cvSearchSecond.visible()
                }
            }

        })
    }

}


