package roadfriend.app.ui.maps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import roadfriend.app.R
import roadfriend.app.data.local.model.MapsModel
import roadfriend.app.data.remote.model.city.City
import roadfriend.app.data.remote.model.maps.MapsResponse
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.ui.base.BaseFragment
import roadfriend.app.ui.maps.adapter.LocationAdapter
import roadfriend.app.ui.tripdetail.TripDetailActivity
import roadfriend.app.utils.extensions.gone
import roadfriend.app.utils.extensions.launchActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import kotlinx.android.synthetic.main.item_admob.view.*
import kotlinx.android.synthetic.main.map_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MapFragment : BaseFragment(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks {
    override val layoutId: Int?
        get() = R.layout.map_fragment

    private val viewModel by viewModel<MapViewModel>()
    var map: GoogleMap? = null
    private var marker: Marker? = null
    private var listPosition: Int = 0

    val mapModel: MapsModel by lazy { arguments?.getParcelable<MapsModel>("maps") as MapsModel }

    lateinit var tripsModel: ArrayList<Trips>

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI(view: View) {
        if (!mapModel.type.equals("tripDetail")) {
            val adRequest = AdRequest.Builder().build()
            iAdView.adView.loadAd(adRequest)
        }

    }

    override fun initListener() {
        tripsModel = mapModel.trips
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapsFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mapVisit()
        initActivityType()

    }

    companion object {
        fun newInstance() = MapFragment()
    }


    fun initActivityType() {
        if (arguments?.getString("tripDetailActivity").equals("tripDetailActivity")) {
            viewPager.gone()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (map != null) {
            map!!.clear()
        }
        map = googleMap
        //  map?.mapType = GoogleMap.MAP_TYPE_SATELLITE
        map!!.uiSettings.isZoomControlsEnabled = true
        map!!.setOnMarkerClickListener(this)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(40.91987000, 33.85427000), 4.1f))
        map!!.setOnMapLoadedCallback {
            try {
                initBottomSlider(tripsModel)
                directions(0)
                markerInit(0)
            } catch (e: Exception) {
                print("" + e.message)
            }
        }


    }


    fun initBottomSlider(tripsModel: ArrayList<Trips>) {

        viewPager.setClipToPadding(false);
        viewPager.setPadding(40, 0, 40, 0);
        viewPager.setPadding(60, 8, 60, 8);
        val adapter = LocationAdapter(
            activity!!.applicationContext,
            tripsModel,
            object : LocationAdapter.ViewPagerItemClick {
                override fun onItemClickListener(trips: Trips, view: View, position: Int) {
                    context?.launchActivity<TripDetailActivity> {
                        putExtra("maps", mapModel)
                        putExtra("data", trips)
                    }
                }
            })
        viewPager.adapter = adapter
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
                listPosition = position
                markerInit(position)
            }
        })
    }

    fun markerInit(position: Int) {
        map?.clear()
        directions(position)
        var stationCount: Int = 0
        var mCityList: ArrayList<City> = arrayListOf()
        mCityList.add(tripsModel.get(position).startCity!!)
        mCityList.add(tripsModel.get(position).endCity!!)
        mCityList.forEachIndexed { index, city ->

            val locationDrawable: Drawable
            val cityStatu: String
            val tripsLocation = LatLng(
                city.latitude,
                city.longitude
            )

//            if (index == 0) {
//                /**start*/
//                locationDrawable = resources.getDrawable(R.drawable.ic_start_location)
//                cityStatu = "Start"
//            } else if (index == tripsModel.get(position).city.size - 1) {
//                /**end*/
//                locationDrawable = resources.getDrawable(R.drawable.ic_start_location)
//                cityStatu = "End"
//            } else {
//                /**station*/
//                locationDrawable = resources.getDrawable(R.drawable.ic_station_location)
//                stationCount++
//                cityStatu = stationCount.toString() + "." + " Station"
//            }
            marker = map?.addMarker(
                MarkerOptions()
                    .position(tripsLocation)
                    //                    .snippet("Cuma")
                    .icon(
                        BitmapDescriptorFactory.fromBitmap(
                            createCustomMarker(
                                city.name!!
                            )
                        )
                    )
            )
            marker?.showInfoWindow()
            marker?.hideInfoWindow()
        }

    }

    fun directions(position: Int) {
        var from = ""
        var to = ""
        var zoomLang: LatLng? = null
        var zoomLocationList: ArrayList<LatLng> = arrayListOf()

        var mCityList: ArrayList<City> = arrayListOf()
        mCityList.add(tripsModel.get(position).startCity!!)
        mCityList.add(tripsModel.get(position).endCity!!)

        mCityList.forEachIndexed { index, city ->
            if (index == 0) {
                /**start*/
                from = city.latitude.toString() + "," + city.longitude.toString()
            } else {
                /**end*/
                to = city.latitude.toString() + "," + city.longitude.toString()
            }
            zoomLang = LatLng(city.latitude, city.longitude)
            zoomLocationList.add(zoomLang!!)

        }
        centerMapZooming(map!!, zoomLocationList)

        val apiServices = RetrofitClient.apiServices()
        viewModel.getPresenter()?.showLoading()
        apiServices.getDirection(from, to, "AIzaSyC60rrlZZSSVhIfop-vsDzYaoayDwS5fFo")
            .enqueue(object : Callback<MapsResponse> {
                override fun onResponse(
                    call: Call<MapsResponse>,
                    response: Response<MapsResponse>
                ) {
                    drawPolyline(response.body()!!)
                }

                override fun onFailure(call: Call<MapsResponse>, t: Throwable) {
                }
            })
    }

    fun centerMapZooming(mMap: GoogleMap, latLng: ArrayList<LatLng>) {
        var builder = LatLngBounds.builder()
        latLng.forEach {
            builder.include(it)
        }
        var bounds: LatLngBounds = builder.build()
        var cameraUpdate: CameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 130)
        mMap.moveCamera(cameraUpdate)

    }


    private fun createCustomMarker(city: String): Bitmap {
        val marker =
            (context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.marker_icon,
                null
            )

        val markerTitle = marker.findViewById(R.id.ivMarkerTitle) as TextView
        markerTitle.text = city

        val markerImage = marker.findViewById(R.id.ivMarkerImage) as ImageView
        //  markerImage.setImageDrawable(image)

        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        marker.layoutParams = ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT)
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(
            marker.measuredWidth,
            marker.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        marker.draw(canvas)
        return bitmap
    }

    override fun onConnected(p0: Bundle?) {

    }


    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onMarkerClick(myMarker: Marker?): Boolean {
        if (mapModel.type.equals("home")) {

        }
        return false
    }

    private fun drawPolyline(data: MapsResponse) {
        viewModel.getPresenter()?.hideLoading()
        if (!data.routes.isNullOrEmpty()) {
            val shape = data.routes?.get(0)?.overviewPolyline?.points
            val polyline = PolylineOptions()
                .addAll(PolyUtil.decode(shape))
                .width(5f)
                .color(Color.RED)
            map!!.addPolyline(polyline)
        }
    }

    private interface ApiServices {
        @GET("maps/api/directions/json")
        fun getDirection(
            @Query("origin") origin: String,
            @Query("destination") destination: String,
            @Query("key") apiKey: String
        ): Call<MapsResponse>

        @GET("maps/api/directions/json")
        fun getDirectionWaypoints(
            @Query("origin") origin: String,
            @Query("waypoints") waypoints: String,
            @Query("destination") destination: String,
            @Query("key") apiKey: String
        ): Call<MapsResponse>
    }

    private object RetrofitClient {
        fun apiServices(): ApiServices {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://maps.googleapis.com/")
                .build()

            return retrofit.create<ApiServices>(ApiServices::class.java)
        }
    }

    fun mapDirection(context: Context, tripsModel: ArrayList<Trips>) {
//        val size = tripsModel.get(0).city.size - 1
//        val latituteStart = tripsModel.get(0).city.get(0).latitude
//        val longituteStart = tripsModel.get(0).city.get(0).longitude
//        val latituteEnd = tripsModel.get(0).city.get(size).latitude
//        val longituteEnd = tripsModel.get(0).city.get(size).latitude
//        val strUri =
//            "https://www.google.com/maps/dir/?api=1&origin=$latituteStart,$longituteStart&destination=$latituteEnd,$longituteEnd&travelmode=driving"
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
//
//        intent.setPackage("com.google.android.apps.maps")
//
//        context.startActivity(intent)
    }

    private fun mapVisit() {
        ivTransparent.setOnTouchListener(View.OnTouchListener { v, event ->
            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    nestedScrollView.requestDisallowInterceptTouchEvent(true)
                    false
                }

                MotionEvent.ACTION_UP -> {
                    nestedScrollView.requestDisallowInterceptTouchEvent(false)
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    nestedScrollView.requestDisallowInterceptTouchEvent(true)
                    false
                }

                else -> true
            }
        })
    }
}
