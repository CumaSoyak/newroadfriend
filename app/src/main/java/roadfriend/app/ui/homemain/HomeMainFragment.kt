package roadfriend.app.ui.homemain


import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_home_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.CoreApp
import roadfriend.app.R
import roadfriend.app.data.local.model.Search
 import roadfriend.app.ui.base.BaseFragment
import roadfriend.app.ui.home.FirstFragment
import roadfriend.app.ui.home.HomeViewModel
import roadfriend.app.ui.home.SecondFragment
import roadfriend.app.utils.DialogUtils
import roadfriend.app.utils.PrefUtils.isLogin
import roadfriend.app.utils.extensions.gone
import roadfriend.app.utils.extensions.visible
import roadfriend.app.utils.firebasedatebase.FirebaseHelper
import roadfriend.app.utils.helper.LiveBus
import roadfriend.app.utils.helper.ViewPagerAdapter

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
        if (isLogin()) {
            FirebaseHelper().getMyTrip { data ->
                if (data.size > 0) {
                    if (cvPremium != null) {
                        cvPremium.visible()
                        cvPremium.visibleContainer(data)
                     }

                }
            }
        }
    }
    fun showDietician() {
        val model = DialogUtils.DialogModel(
            "Tebrikler ücretsiz beslenme danışmanlığı kazandınız  \n ⚫ Hediye için Dm'den iletişime geçin. \n⚫ Takip edin ",
            "Hemen ",
            "",
            R.drawable.diyet_kocu,
            false
        )
        DialogUtils.showPopupModel(requireContext(), model, object : DialogUtils.DialogListener {
            override fun onNegativeClick() {

            }

            override fun onPositiveClick() {
                var uri = Uri.parse("http://instagram.com/diyet.kocu");
                var likeIng = Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/apptravelfriend")
                        )
                    );

                }
            }
        })
    }

    override fun initListener() {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(FirstFragment(), resources.getString(R.string.add_arac_ariyorum))
        adapter.addFragment(SecondFragment(), resources.getString(R.string.add_yuk_ariyorum))
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)



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


