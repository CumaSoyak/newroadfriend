/*
 * Copyright (c) 2021.
 *
 * Created by Yigit Can YILMAZ
 */

package roadfriend.app.utils.helper

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class GenericAdapterOld<T>(
    var itemResId: Int,
    var dataList: ArrayList<T> = arrayListOf(),
    var bindAction: ((view: View, item: T, position: Int) -> Unit)
) : RecyclerView.Adapter<GenericAdapterOld<T>.GenericViewHolder>() {

    inner class GenericViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: T, position: Int) {
            bindAction.invoke(itemView, item, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(itemResId, null, false)
        val vh = GenericViewHolder(view)
        vh.setIsRecyclable(false)
        return vh
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(item: T){
        dataList.add(item)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAll(collection: Collection<T>){
        dataList.addAll(collection)
        notifyDataSetChanged()
    }

    fun clear(){
        dataList.clear()
    }

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}