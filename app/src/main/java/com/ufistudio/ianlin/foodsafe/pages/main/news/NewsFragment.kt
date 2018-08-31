package com.ufistudio.ianlin.foodsafe.pages.main.news

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.perf.metrics.AddTrace
import com.ufistudio.ianlin.foodsafe.AppInjector
import com.ufistudio.ianlin.foodsafe.R
import com.ufistudio.ianlin.foodsafe.pages.base.InteractionView
import com.ufistudio.ianlin.foodsafe.pages.base.OnPageInteractionListener
import com.ufistudio.ianlin.foodsafe.repository.data.NewsInfo
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : InteractionView<OnPageInteractionListener.Pane>() {
    private lateinit var mViewModel: NewsViewModel
    private lateinit var mRecyclerViewAdapter: NewsInfoListAdapter

    companion object {
        fun NewInstance(): NewsFragment = NewsFragment()
        private val TAG = NewsFragment::class.simpleName
    }

    @AddTrace(name = "onCreateTrace", enabled = true)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = AppInjector.obtainViewModel(this)

        mViewModel.queryNewsInfoListSuccess.observe(this, Observer { it?.let { it1 -> onQueryNewsInfoListSuccess(it1) } })
        mViewModel.queryNewsInfoListProgress.observe(this, Observer { onQueryNewsInfoListProgress(it!!) })
        mViewModel.queryNewsInfoListError.observe(this, Observer { onQueryNewsInfoListError(it!!) })

        mViewModel.queryNewsList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

    }

    private fun initRecyclerView() {
        recyclerView_content.layoutManager = LinearLayoutManager(context)
        mRecyclerViewAdapter = NewsInfoListAdapter()
        recyclerView_content.adapter = mRecyclerViewAdapter
    }

    private fun onQueryNewsInfoListSuccess(list: ArrayList<NewsInfo>) {
            Log.d("Neo", "data = " + list.get(0).title)
        mRecyclerViewAdapter.setData(list)
    }

    private fun onQueryNewsInfoListProgress(isProgress: Boolean) {

        Log.d(TAG, "onQueryCategoryListProgress call. ${isProgress}")
        if (isProgress) {
            progressView.visibility = View.VISIBLE
            recyclerView_content.visibility = View.GONE
        } else {
            progressView.visibility = View.GONE
            recyclerView_content.visibility = View.VISIBLE
        }
    }

    private fun onQueryNewsInfoListError(throwable: Throwable) {
        Log.d("Neo", "throwable = $throwable")
    }
}