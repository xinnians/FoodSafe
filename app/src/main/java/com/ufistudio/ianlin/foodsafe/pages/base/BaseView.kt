package com.ufistudio.ianlin.foodsafe.pages.base

import android.content.Context
import android.support.v4.app.Fragment

abstract class BaseView : Fragment(), AppBaseView{

    private val TAG = BaseView::class.simpleName

    override fun getActivityContext(): Context? = context

    override fun onBackPressed(): Boolean = false

    override fun isActivite(): Boolean {
        if (!isAdded)
            return false
        if (activity == null)
            return false
        if (activity!!.isFinishing)
            return false
        return true
    }
}