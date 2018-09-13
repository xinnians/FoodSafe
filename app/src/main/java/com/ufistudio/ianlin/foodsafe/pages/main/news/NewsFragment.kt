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
import com.ufistudio.ianlin.foodsafe.componets.EndLessOnScrollListener
import com.ufistudio.ianlin.foodsafe.pages.base.InteractionView
import com.ufistudio.ianlin.foodsafe.pages.base.OnPageInteractionListener
import com.ufistudio.ianlin.foodsafe.repository.data.NewsInfo
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : InteractionView<OnPageInteractionListener.Pane>() {
    private lateinit var mViewModel: NewsViewModel
    private lateinit var mRecyclerViewAdapter: NewsInfoListAdapter
    private lateinit var mEndLessOnScrollListener: EndLessOnScrollListener

    companion object {
        fun newInstance(): NewsFragment = NewsFragment()
        private val TAG = NewsFragment::class.simpleName
    }

    @AddTrace(name = "onCreateTrace", enabled = true)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = AppInjector.obtainViewModel(this)

        mViewModel.mQueryNewsInfoListSuccess.observe(this, Observer { it?.let { it1 -> onQueryNewsInfoListSuccess(it1) } })
        mViewModel.mQueryNewsInfoListProgress.observe(this, Observer { onQueryNewsInfoListProgress(it!!) })
        mViewModel.mQueryNewsInfoListError.observe(this, Observer { onQueryNewsInfoListError(it!!) })

        mViewModel.queryNewsList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initRefresh()
    }

    private fun initRefresh() {
        layout_swipe_refresh.setOnRefreshListener {
            mViewModel.refreshQueryNewsList()
            layout_swipe_refresh.isRefreshing = false
            mEndLessOnScrollListener.restore()
        }
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerViewAdapter = NewsInfoListAdapter()
        recyclerView.adapter = mRecyclerViewAdapter
        mEndLessOnScrollListener = object : EndLessOnScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(currentPage: Int) {
                mViewModel.queryNewsList()
            }
        }

        recyclerView.addOnScrollListener(mEndLessOnScrollListener)
    }

    private fun onQueryNewsInfoListSuccess(list: ArrayList<NewsInfo>) {
        mRecyclerViewAdapter.setData(list)
    }

    private fun onQueryNewsInfoListProgress(isProgress: Boolean) {

        Log.d(TAG, "onQueryCategoryListProgress call. ${isProgress}")
        if (isProgress) {
            progressView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            progressView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun onQueryNewsInfoListError(throwable: Throwable) {
        Log.d("Neo", "throwable = $throwable")
    }
}