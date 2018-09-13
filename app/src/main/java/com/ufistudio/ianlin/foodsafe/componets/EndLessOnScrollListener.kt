package com.ufistudio.ianlin.foodsafe.componets

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

abstract class EndLessOnScrollListener(linearLayoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    private var mLinearLayoutManager: LinearLayoutManager = linearLayoutManager
    private var totalItemCount: Int = 0
    private var previousTotal = 0
    private var visibleItemCount = 0
    private var firstVisibleItem = 0
    private var loading = true
    private var currentPage = 1

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView?.childCount ?: 0
        totalItemCount = mLinearLayoutManager.itemCount
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition()

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem) {
            currentPage++
            onLoadMore(currentPage)
            loading = true
        }
    }

    abstract fun onLoadMore(currentPage: Int)

    fun restore() {
        currentPage = 0
        totalItemCount = 0
        previousTotal = 0
        visibleItemCount = 0
        firstVisibleItem = 0
        currentPage = 1
    }
}



