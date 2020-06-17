package roadfriend.app.ui.tripdetail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import roadfriend.app.R
import roadfriend.app.databinding.ItemTripsStationListBinding
import roadfriend.app.ui.base.BindingViewHolder

class TripsListAdapter(
    var items: ArrayList<String> = arrayListOf()
) :
    RecyclerView.Adapter<TripsListAdapter.TripsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripsViewHolder {
        return TripsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_trips_station_list,
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: TripsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun add(list: ArrayList<String>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class TripsViewHolder(view: View) : BindingViewHolder<ItemTripsStationListBinding>(view) {
        fun bind(notification: String) {
           // binding.tripsStation = notification
        }
    }
}
