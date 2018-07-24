package com.ufistudio.ianlin.foodsafe.pages.main.information.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ufistudio.ianlin.foodsafe.R
import kotlinx.android.synthetic.main.item_search_history.view.*

class SearchHistoryAdapter(private val clickListener: (String) -> Unit) : RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>() {

    private var mItems: List<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = if (mItems != null) mItems!!.size else 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mItems?.get(position)?.let {
            holder.bind(it, clickListener)
            holder.itemView.tag = it
        }
    }

    fun setItems(items: List<String>) {
        this.mItems = items
        notifyDataSetChanged()
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: String, clickListener: (String) -> Unit) {
            itemView.name.text = data
            itemView.setOnClickListener { clickListener(data) }
        }
    }
}