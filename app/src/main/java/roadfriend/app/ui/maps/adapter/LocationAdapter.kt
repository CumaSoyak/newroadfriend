package roadfriend.app.ui.maps.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import roadfriend.app.R
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.utils.OptionData


class LocationAdapter(
    private val context: Context,
    private val trips: ArrayList<Trips>,
    val viewPagerItemClick: ViewPagerItemClick
) : PagerAdapter() {

    private var layoutInflater: LayoutInflater? = null

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return trips.size
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = layoutInflater!!.inflate(R.layout.dialog_marker, null)

        val model = trips[position]

        val tvUserStatu = v.findViewById<View>(R.id.tvUserStatu) as TextView
        val btnDetail = v.findViewById<View>(R.id.btnDetail) as Button

        val startCity = v.findViewById<TextView>(R.id.startCity)
        val endCity = v.findViewById<TextView>(R.id.endCity)
        val price = v.findViewById<TextView>(R.id.tvPrice)

        startCity.text = model.startCity.name
        endCity.text = model.endCity.name

        price.text = model.callPrice()

        tvUserStatu.text = OptionData.tripatus(trips[position].status)
        btnDetail.setOnClickListener {
            viewPagerItemClick.onItemClickListener(trips[position], it, position)
        }
        val vp = container as ViewPager
        vp.addView(v, 0)
        return v
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val v = `object` as View
        vp.removeView(v)
    }

    interface ViewPagerItemClick {
        fun onItemClickListener(trips: Trips, view: View, position: Int)
    }
}