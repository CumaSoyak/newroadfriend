package roadfriend.app.ui.add.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import roadfriend.app.R
import roadfriend.app.data.local.model.choosestaus.ChooseStatus
import roadfriend.app.databinding.ItemAddStationBinding
import roadfriend.app.databinding.ItemChooseStatusBinding
 import roadfriend.app.ui.base.BindingViewHolder

class ChooseStatusAdapter(
    var items: ArrayList<ChooseStatus> = arrayListOf(),
    val listener: IChooseListener
) :
    RecyclerView.Adapter<ChooseStatusAdapter.ChooseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseViewHolder {
        return ChooseViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_choose_status,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChooseViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun add(list: ArrayList<ChooseStatus>) {
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        items.removeAt(position)
        notifyDataSetChanged()
    }

    class ChooseViewHolder(view: View) : BindingViewHolder<ItemChooseStatusBinding>(view) {
        fun bind(station: ChooseStatus) {
            binding.status = station
        }
    }

}

interface IChooseListener {
    fun removeStation(position: Int)
}