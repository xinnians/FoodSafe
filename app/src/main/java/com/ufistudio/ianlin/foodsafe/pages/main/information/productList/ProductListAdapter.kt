package com.ufistudio.ianlin.foodsafe.pages.main.information.productList

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ufistudio.ianlin.foodsafe.R
import com.ufistudio.ianlin.foodsafe.repository.data.Product
import kotlinx.android.synthetic.main.item_information_list.view.*

class ProductListAdapter(private val clickListener: (Product) -> Unit) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    private var mItems: ArrayList<Product>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_information_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = if (mItems != null) mItems!!.size else 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mItems?.get(position)?.let {
            holder.bind(it, clickListener)
            holder.itemView.tag = it
        }
    }

    fun setItems(items: ArrayList<Product>?, isAdd: Boolean = false) {
        if (!isAdd)
            this.mItems = items
        else
            items?.let { this.mItems?.addAll(it) }
        notifyDataSetChanged()
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: Product, clickListener: (Product) -> Unit) {
            Glide.with(itemView.image.context).load(data.images.first()).into(itemView.image)
            itemView.name.text = data.name
            itemView.weight.text = data.description
            itemView.category.text = data.category.name
            itemView.company.text = data.company
            itemView.checkDate.text = data.inspection_date
            itemView.setOnClickListener { clickListener(data) }
        }
    }
}