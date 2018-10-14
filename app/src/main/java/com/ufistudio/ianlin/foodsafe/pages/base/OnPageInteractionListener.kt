package com.ufistudio.ianlin.foodsafe.pages.base

import android.os.Bundle
import android.support.annotation.IdRes
import com.ufistudio.ianlin.foodsafe.repository.data.Product

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

    interface NewsView {
        fun openWebView(url: String)
    }

    interface WebView : Pane
    /*--------------------------------------------------------------------------------------------*/
    /* Fragment <-> Fragment */

    interface PrimaryView : Base {
        fun openDetailPage(item: Product)
    }
}