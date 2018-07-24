package com.ufistudio.ianlin.foodsafe.pages.base

import android.os.Bundle
import android.support.annotation.IdRes
import com.ufistudio.ianlin.foodsafe.pages.main.MainFragment

interface OnPageInteractionListener {

    interface Base {
        fun pressBack()
        fun showFullScreenLoading()
        fun hideFullScreenOverlay()
    }

    interface Pane : Base {
        fun switchPage(@IdRes container: Int, page: Int, args: Bundle, addToBackStack: Boolean, withAnimation: Boolean)
        fun addPage(@IdRes container: Int, page: Int, args: Bundle, addToBackStack: Boolean, withAnimation: Boolean)
    }

    interface Primary : Pane

    interface Main : Pane {
        fun setNavigationViewPosition(@IdRes id: Int)
        fun setPagerController(controller: MainFragment.PagerController)
        fun clearPagerController()
    }

    /*--------------------------------------------------------------------------------------------*/
    /* Fragment <-> Fragment */
    interface MainView : Base {
    }

    interface PrimaryView : Base {
        fun openDetailPage()
    }
}