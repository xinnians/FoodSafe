package com.ufistudio.ianlin.foodsafe.pages.main.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ufistudio.ianlin.foodsafe.R
import com.ufistudio.ianlin.foodsafe.pages.base.InteractionView
import com.ufistudio.ianlin.foodsafe.pages.base.OnPageInteractionListener

class NewsFragment: InteractionView<OnPageInteractionListener.MainView>() {

    companion object {
        fun NewInstance(): NewsFragment = NewsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var layout = inflater.inflate(R.layout.fragment_news,container,false)
        return layout
    }
}