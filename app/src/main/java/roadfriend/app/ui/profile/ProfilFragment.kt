package roadfriend.app.ui.profile


import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.View
import com.facebook.login.LoginManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import kotlinx.android.synthetic.main.fragment_profil.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.R
import roadfriend.app.data.local.model.ProfilMenu
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.databinding.FragmentProfilBinding
import roadfriend.app.ui.auth.login.LoginActivity
import roadfriend.app.ui.base.BindingFragment
import roadfriend.app.ui.profile.help.HelpActivity
import roadfriend.app.ui.profile.myaboutcomment.MyAboutCommentsActivity
import roadfriend.app.ui.profile.mytrip.MyTripsActivity
import roadfriend.app.ui.profile.savedtrip.SavedTripActivity
import roadfriend.app.ui.profile.setting.ProfilSettingsActivity
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.extensions.getDrawable
import roadfriend.app.utils.extensions.launchActivity
import roadfriend.app.utils.firebasedatebase.FirebaseHelper
import roadfriend.app.utils.firebasedatebase.TripFirebase
import roadfriend.app.utils.helper.genericadapter.GenericAdapter
import roadfriend.app.utils.helper.genericadapter.ListItemViewModel


class ProfilFragment : BindingFragment<FragmentProfilBinding>() {
    override val getLayoutBindId: Int
        get() = R.layout.fragment_profil

    private val viewModel by viewModel<ProfilViewModel>()

    private val adapter = GenericAdapter<ProfilMenu>(R.layout.item_profil_menu)
    private var mRewardedVideoAd: RewardedVideoAd? = null

    companion object {
        val TAG: String = ProfilFragment::class.java.simpleName
        fun newInstance(): ProfilFragment = ProfilFragment().apply {}
    }

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI(view: View) {
        binding.vm = viewModel
        binding.lifecycleOwner = this
        addMenu()

        viewModel.getUserData()
    }

    override fun initListener() {
        menuListener()
        rewardListener()
    }

    private fun addMenu() {
        val menuItem: ArrayList<ProfilMenu> = arrayListOf()
        val password = PrefUtils.getUser().password
        if (!password.equals("facebook") && !password.equals("google")) {
//            menuItem.add(
//                ProfilMenu("settings", getDrawable(R.drawable.ic_settings), "Bilgileri Düzenle")
//            )
        }

        menuItem.add(
            ProfilMenu(
                "mytrips",
                getDrawable(R.drawable.ic_mytrips),
                getString(R.string.yolculuklarım)
            )
        )
        menuItem.add(
            ProfilMenu(
                "saved",
                getDrawable(R.drawable.ic_save),
                getString(R.string.kaydedilen_yolculuklar)
            )
        )
        menuItem.add(
            ProfilMenu(
                "comment",
                getDrawable(R.drawable.ic_comment),
                getString(R.string.hakkimda_yorumlar)
            )
        )
        menuItem.add(
            ProfilMenu(
                "condition",
                getDrawable(R.drawable.ic_condition),
                getString(R.string.kosul_sartlar)
            )
        )
        menuItem.add(
            ProfilMenu(
                "help",
                getDrawable(R.drawable.ic_help),
                getString(R.string.yardim)
            )
        )

        menuItem.add(
            ProfilMenu(
                "support",
                getDrawable(R.drawable.ic_technical_support),
                getString(R.string.iletisime_gec)
            )
        )

        menuItem.add(
            ProfilMenu(
                "instagram",
                getDrawable(R.drawable.ic_instagram),
                getString(R.string.instagram)
            )
        )

        menuItem.add(
            ProfilMenu(
                "exit",
                getDrawable(R.drawable.ic_exit),
                getString(R.string.cikis_yap)
            )
        )
        menuItem.add(
            ProfilMenu(
                "delete",
                getDrawable(R.drawable.ic_delete),
                getString(R.string.hesabi_sil)
            )
        )

        rv.adapter = adapter
        adapter.addItems(menuItem)
    }

    private fun menuListener() {
        adapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClickDetail(view: View, position: Int, model: ListItemViewModel) {
                openDetail(model as ProfilMenu)
            }
        })
    }

    private fun openDetail(position: ProfilMenu) {
        when (position.id) {
            "destek" -> {
                if (mRewardedVideoAd?.isLoaded!!) {
                    mRewardedVideoAd!!.show()
                }
            }
            "settings" -> {
                /*settings*/
                requireContext().launchActivity<ProfilSettingsActivity> { }
            }
            "mytrips" -> {
                /*yolcuuklarım*/
                requireContext().launchActivity<MyTripsActivity> { }
            }
            "saved" -> {
                //"Kaydedilen Yolculuklar"
                requireContext().launchActivity<SavedTripActivity> { }
            }
            "comment" -> {
                //"Hakkımda yorumlar"
                requireContext().launchActivity<MyAboutCommentsActivity> {
                    putExtra("data", PrefUtils.getUser())
                    putExtra("myComment","myComment")
                }
            }
            "condition" -> {
                //Koşul ve şartlar
                requireContext().launchActivity<HelpActivity> {
                    this.putExtra("kosul", "kosul")
                }
            }
            "help" -> {
                //Yardım
                requireContext().launchActivity<HelpActivity> { }
            }
            "support" -> {
                //iletişime geç
                requireContext().launchActivity<HelpActivity> {
                    this.putExtra("support", "support")
                }
            }
            "instagram" -> {
                var uri = Uri.parse("http://instagram.com/apptravelfriend");
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
            "delete" -> {
                viewModel.getPresenter()?.showLoading()
                FirebaseHelper().getMyTrip { data ->
                    data.forEachIndexed { index, trips ->
                        TripFirebase().deleteTripAccount(
                            requireActivity(),
                            trips.documentKey!!
                        )
                    }
                    viewModel.getPresenter()?.hideLoading()
                    logout()
                }
            }
            "exit" -> {
                logout()
            }
        }
    }

    fun rewardListener() {
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(requireContext())
        mRewardedVideoAd?.loadAd(
            "ca-app-pub-7740710689946524/5882913386",
            AdRequest.Builder().build()
        )
        mRewardedVideoAd?.rewardedVideoAdListener = object : RewardedVideoAdListener {
            override fun onRewardedVideoAdClosed() {
                print("")
            }

            override fun onRewardedVideoAdLeftApplication() {
                print("")
            }

            override fun onRewardedVideoAdLoaded() {
                print("")
            }

            override fun onRewardedVideoAdOpened() {
                print("")
            }

            override fun onRewardedVideoCompleted() {
            }

            override fun onRewarded(reward: RewardItem?) {

            }

            override fun onRewardedVideoStarted() {

            }

            override fun onRewardedVideoAdFailedToLoad(p0: Int) {

            }
        }

    }

    fun logout() {
        if (PrefUtils.getUser().password.equals("facebook")) {
            LoginManager.getInstance().logOut()
        }
        if (PrefUtils.getUser().password.equals("google")) {
            LoginManager.getInstance().logOut()
        }
        PrefUtils.logOut()
        requireContext().launchActivity<LoginActivity> { }
        //Çıkış
    }


}
