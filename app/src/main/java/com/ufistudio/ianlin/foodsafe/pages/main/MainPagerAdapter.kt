package com.ufistudio.ianlin.foodsafe.pages.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.ufistudio.ianlin.foodsafe.constants.Page

class MainPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val TOTAL_PAGE_COUNT = 4

    override fun getItem(position: Int): Fragment {
        var args = Bundle()
//        args.putInt(PAGE_POSITION,position)
//        return when(position){
//            0 -> Page.view(Page.INFORMATION, args)
//            else -> Page.view(Page.NEWS, args)
//        }
        return Page.view(Page.NEWS, args)
    }

    override fun getCount(): Int {
        return TOTAL_PAGE_COUNT
    }
}