package roadfriend.app.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import roadfriend.app.R
import roadfriend.app.databinding.ItemAdvertBinding
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.ui.base.BindingViewHolder


class TripsAdapter(
    var items: ArrayList<Trips> = arrayListOf()
    , var listener: ITripsClickListener
) :
    RecyclerView.Adapter<TripsAdapter.TripsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripsViewHolder {
        return TripsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_advert,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TripsViewHolder, position: Int) {

        holder.bind(items[position], listener)
    }

    override fun getItemCount() = items.size

    class TripsViewHolder(view: View) : BindingViewHolder<ItemAdvertBinding>(view) {
        fun bind(trips: Trips, listener: ITripsClickListener) {

        }
    }
}

interface ITripsClickListener {
    fun onClickUserDetail(model: Trips)
    fun onClickTripsDetail(model: Trips)
}