package com.ufistudio.ianlin.foodsafe.pages.main.guide

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.perf.metrics.AddTrace
import com.ufistudio.ianlin.foodsafe.R
import com.ufistudio.ianlin.foodsafe.constants.Page
import com.ufistudio.ianlin.foodsafe.pages.base.OnPageInteractionListener
import com.ufistudio.ianlin.foodsafe.pages.base.PaneView
import kotlinx.android.synthetic.main.fragment_guide.*
import java.io.IOException

class GuideFragment : PaneView<OnPageInteractionListener.Guide>(), View.OnClickListener {
    private val TAG_DISCLAIMER_FILE: String = "disclaimer.txt"

    companion object {
        fun newInstance(): GuideFragment = GuideFragment()
        private val TAG = GuideFragment::class.simpleName
    }

    @AddTrace(name = "onCreateTrace", enabled = true)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_guide, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        item_topic.setOnClickListener(this)
        item_news.setOnClickListener(this)
        item_farmer.setOnClickListener(this)
        item_info.setOnClickListener(this)

        text_disclaimer.movementMethod = ScrollingMovementMethod()

        var text = ""
        try {
            val inputStream = context?.assets?.open(TAG_DISCLAIMER_FILE)
            val size = inputStream?.available()
            if (size != null) {
                val buffer = ByteArray(size)
                inputStream?.read(buffer)
                inputStream?.close()
                text = String(buffer)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        text_disclaimer.text = text
    }

    override fun onClick(v: View?) {
        var page: Int = 0

        when (v) {
            item_topic -> page = Page.TOPICS
            item_farmer -> page = Page.LOCAL_FARMER
            item_info -> page = Page.INFORMATION
            item_news -> page = Page.NEWS
        }
        getInteractionListener().goToMain(page)
    }

}