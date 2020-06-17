package roadfriend.app.ui.searchcity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import roadfriend.app.R
import roadfriend.app.data.remote.model.city.City
import roadfriend.app.databinding.ItemStationListBinding
import roadfriend.app.ui.base.BindingViewHolder

class SearchListAdapter(
    var items: ArrayList<City> = arrayListOf(),
    val iSearchCityListener: ISearchListener
) :
    RecyclerView.Adapter<SearchListAdapter.SearchListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListViewHolder {
        return SearchListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_station_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchListViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            iSearchCityListener.onClickItem(items[position])
        }
    }

    override fun getItemCount() = items.size

    fun filterList(list: ArrayList<City>) {
        items = list
        notifyDataSetChanged()
    }

    fun add(list: ArrayList<City>) {
        items.addAll(list)
        notifyDataSetChanged()
    }

    class SearchListViewHolder(view: View) : BindingViewHolder<ItemStationListBinding>(view) {
        fun bind(station: City) {
        }
    }


}

interface ISearchListener {
    fun onClickItem(station: City)
}