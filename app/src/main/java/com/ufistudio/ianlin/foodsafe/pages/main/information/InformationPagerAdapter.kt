package com.ufistudio.ianlin.foodsafe.pages.main.information

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import com.ufistudio.ianlin.foodsafe.constants.Page
import com.ufistudio.ianlin.foodsafe.pages.main.information.productList.ProductListFragment
import com.ufistudio.ianlin.foodsafe.repository.data.Category

class InformationPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var mItems: ArrayList<Category>? = null

    override fun getItem(position: Int): Fragment {
        val args = Bundle()
        mItems?.get(position)?.id?.let { args.putInt(ProductListFragment.PAGE_POSITION, it) }
        return Page.view(Page.PRODUCT_LIST, args)
    }

    override fun getCount(): Int {
        return if (mItems != null) mItems!!.size else 0
    }

    fun setItems(list: ArrayList<Category>) {
        this.mItems = list
        notifyDataSetChanged()
    }
}