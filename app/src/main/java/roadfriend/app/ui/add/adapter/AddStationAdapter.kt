package roadfriend.app.ui.add.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import roadfriend.app.R
import roadfriend.app.data.remote.model.city.City
import roadfriend.app.databinding.ItemAddStationBinding
 import roadfriend.app.ui.base.BindingViewHolder

class AddStationAdapter(
    var items: ArrayList<City> = arrayListOf(),
    val listener: IRemoveStationListener
) :
    RecyclerView.Adapter<AddStationAdapter.AddStationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddStationViewHolder {
        return AddStationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_add_station,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AddStationViewHolder, position: Int) {
        holder.bind(items[position])
        holder.binding.removeStation.setOnClickListener {
            listener.removeStation(position)
        }
    }

    override fun getItemCount() = items.size

    fun add(list: ArrayList<City>) {
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        items.removeAt(position)
        notifyDataSetChanged()
    }

    class AddStationViewHolder(view: View) : BindingViewHolder<ItemAddStationBinding>(view) {
        fun bind(station: City) {
            binding.station = station
        }
    }

}

interface IRemoveStationListener {
    fun removeStation(position: Int)
}