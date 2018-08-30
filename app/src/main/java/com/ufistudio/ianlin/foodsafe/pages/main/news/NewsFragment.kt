package com.ufistudio.ianlin.foodsafe.pages.main.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.perf.metrics.AddTrace
import com.ufistudio.ianlin.foodsafe.AppInjector
import com.ufistudio.ianlin.foodsafe.R
import com.ufistudio.ianlin.foodsafe.pages.base.InteractionView
import com.ufistudio.ianlin.foodsafe.pages.base.OnPageInteractionListener

class NewsFragment : InteractionView<OnPageInteractionListener.Pane>() {
    private lateinit var mViewModel: NewsViewModel
    companion object {
        fun NewInstance(): NewsFragment = NewsFragment()
        private val TAG = NewsFragment::class.simpleName
    }

    @AddTrace(name = "onCreateTrace", enabled = true)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = AppInjector.obtainViewModel(this)

        mViewModel.queryCategoryListSuccess.observe(this, Observer { it?.let { it1 -> onQueryCategoryListSuccess(it1) } })
        mViewModel.queryNewsInfoListProgress.observe(this, Observer { onQueryCategoryListProgress(it!!) })
        mViewModel.queryNewsInfoListError.observe(this, Observer { onQueryCategoryListError(it!!) })

        mViewModel.queryCategoryList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_news, container, false)
    }
}