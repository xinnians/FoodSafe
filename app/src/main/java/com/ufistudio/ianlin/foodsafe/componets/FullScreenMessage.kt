package com.ufistudio.ianlin.foodsafe.componets

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.ufistudio.ianlin.foodsafe.R

class FullScreenMessage : DialogFragment() {

    var mIsShowing: Boolean = false

    companion object {
        fun newInstance(): FullScreenMessage = FullScreenMessage()
        private val TAG = FullScreenMessage::class.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var window: Window? = null
        dialog?.let {
            it.requestWindowFeature(Window.FEATURE_NO_TITLE)
            window = it.window
        }
        window?.let { it.setBackgroundDrawableResource(android.R.color.transparent) }

        return inflater.inflate(R.layout.widget_full_screen_message, container, false)
    }

    fun showLoading(manager: FragmentManager?) {
        manager?.let { if(!isAdded && !mIsShowing) show(manager, TAG) }
    }

    override fun show(manager: FragmentManager?, tag: String?) {
        mIsShowing = true
        try {
            if (manager != null && tag != null) {
                this.showDialogAllowingStateLoss(manager, tag)
            }
        } catch (e: IllegalStateException) {
            Log.e(TAG, "[showLoading] ${e.message}")
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        mIsShowing = false
        super.onDismiss(dialog)
    }

    private fun showDialogAllowingStateLoss(fragmentManager: FragmentManager, tag: String) {
        var ft = fragmentManager.beginTransaction()
        ft.add(this, TAG)
        ft.commitAllowingStateLoss()
    }

    fun isShowing(): Boolean = mIsShowing

}