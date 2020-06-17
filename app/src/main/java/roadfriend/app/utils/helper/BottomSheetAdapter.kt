package roadfriend.app.utils.helper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import roadfriend.app.R


class BottomSheetAdapter(
    val context: Context,
    var items: ArrayList<String> = arrayListOf(),
    val listener: IChooseListener
) :

    RecyclerView.Adapter<BottomSheetAdapter.BottomSheetViewHolder>() {

    var SELECTED_POSITION = -1
    var boolean: Boolean = true


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        return BottomSheetViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_choose_sheet_bottom,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        holder.text.text = items[position]

        if (position == SELECTED_POSITION) {
            holder.container.background =
                context.resources.getDrawable(R.drawable.custom_border_blue_button)
        } else {
            holder.container.background =
                context.resources.getDrawable(R.drawable.card_background)
        }
        holder.itemView.setOnClickListener {
            changeStatus(position, holder)
        }
    }

    fun changeStatus(
        position: Int,
        holder: BottomSheetViewHolder
    ) {
        if (position == SELECTED_POSITION) {
            SELECTED_POSITION = -1
            holder.container.background =
                context.resources.getDrawable(R.drawable.custom_border_blue_button)
            listener.choose(position, items[position])

        } else {
            listener.choose(position, items[position])
            holder.text.setTextColor(context.resources.getColor(R.color.border))
            SELECTED_POSITION = position
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = items.size

    fun add(list: ArrayList<String>) {
        items.addAll(list)
        notifyDataSetChanged()
    }

    class BottomSheetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.tvCar)
        val container: LinearLayout = view.findViewById(R.id.container)

    }

    interface IChooseListener {
        fun choose(position: Int, model: String)
    }

}

