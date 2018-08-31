package com.ufistudio.ianlin.foodsafe.pages.main.news

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ufistudio.ianlin.foodsafe.R
import com.ufistudio.ianlin.foodsafe.repository.data.NewsInfo
import kotlinx.android.synthetic.main.item_news_info.view.*

class NewsInfoListAdapter : RecyclerView.Adapter<NewsInfoListAdapter.ViewHolder>() {

    private var mItems: ArrayList<NewsInfo>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_info, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = if (mItems != null) mItems!!.size else 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mItems?.get(position)?.let {
            holder.bind(it)
        }
    }

    fun setData(data: ArrayList<NewsInfo>) {
        mItems = data
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(it: NewsInfo) {
            itemView.description.text = it.title
            itemView.date.text = it.date

            Glide.with(itemView.context)
                    .load(it.images?.get(0))
                    .into(itemView.image)
        }
    }
}