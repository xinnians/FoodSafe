package com.ufistudio.ianlin.foodsafe.pages.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ufistudio.ianlin.foodsafe.componets.FullScreenMessage

abstract class BaseActivity : AppCompatActivity(), OnPageInteractionListener.Base {

    private val TAG = BaseActivity::class.simpleName

    private var mFullScreenMessage: FullScreenMessage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initFullScreenMessage()
    }

    override fun pressBack() {
        if (isActivityDestroying())
            return
        onBackPressed()
    }

    override fun showFullScreenLoading() {
        if (isActivityDestroying()) return
        else mFullScreenMessage?.let { it.showLoading(supportFragmentManager) }
    }

    override fun hideFullScreenOverlay() {
        if (isActivityDestroying() || mFullScreenMessage == null || !mFullScreenMessage?.isShowing()!!)
            return
        mFullScreenMessage?.dismissAllowingStateLoss()
    }

    /*--------------------------------------------------------------------------------------------*/
    /* Internal helpers */
    fun isActivityDestroying(): Boolean {
        if (isFinishing)
            return true
        return false
    }

    private fun initFullScreenMessage() {
        mFullScreenMessage = FullScreenMessage.newInstance()
        mFullScreenMessage?.isCancelable = false
    }
}
