package com.ufistudio.ianlin.foodsafe.pages.main.topics

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ufistudio.ianlin.foodsafe.R
import com.ufistudio.ianlin.foodsafe.repository.data.Topic
import kotlinx.android.synthetic.main.item_topic.view.*

class TopicsListAdapter : RecyclerView.Adapter<TopicsListAdapter.ViewHolder>() {
    private var mItems: ArrayList<Topic>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (mItems != null) mItems!!.size else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mItems?.get(position)?.let {
            holder.bind(it)
        }

    }

    fun setData(data: ArrayList<Topic>) {
        mItems = data
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(it: Topic) {
            Glide.with(itemView.context)
                    .load(it.images?.get(0))
                    .into(itemView.image)
            itemView.category.text = it.title
            itemView.date.text = it.date
            itemView.title.text = it.title
        }
    }
}