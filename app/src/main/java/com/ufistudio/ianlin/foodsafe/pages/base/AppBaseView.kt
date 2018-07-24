package com.ufistudio.ianlin.foodsafe.pages.base

import android.content.Context

interface AppBaseView {
    fun getActivityContext(): Context?
    fun onBackPressed(): Boolean
    fun isActivite(): Boolean
    fun showFullScreenLoading()
    fun dismissFullScreenLoading()
}