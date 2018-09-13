package com.ufistudio.ianlin.foodsafe.pages.main.topics

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
import com.ufistudio.ianlin.foodsafe.repository.data.Topic
import kotlinx.android.synthetic.main.fragment_topics.*

class TopicsFragment : InteractionView<OnPageInteractionListener.Pane>() {
    private lateinit var mViewModel: TopicsViewModel
    private lateinit var mRecyclerViewAdapter: TopicsListAdapter
    private lateinit var mEndLessOnScrollListener: EndLessOnScrollListener

    companion object {
        fun newInstance(): TopicsFragment = TopicsFragment()
        private val TAG = TopicsFragment::class.simpleName
    }

    @AddTrace(name = "onCreateTrace", enabled = true)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = AppInjector.obtainViewModel(this)

        mViewModel.mQueryTopicsSuccess.observe(this, Observer { it?.let { it -> onQueryTopicListSuccess(it) } })
        mViewModel.mQueryTopicsProgress.observe(this, Observer { it?.let { it -> onQueryTopicListProgress(it) } })
        mViewModel.mQueryTopicsError.observe(this, Observer { it?.let { it -> onQueryTopicListError(it) } })

        mViewModel.queryTopicsList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_topics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initRefresh()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerViewAdapter = TopicsListAdapter()
        recyclerView.adapter = mRecyclerViewAdapter

        mEndLessOnScrollListener = object : EndLessOnScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(currentPage: Int) {
                mViewModel.queryTopicsList()
            }
        }
        recyclerView.addOnScrollListener(mEndLessOnScrollListener)
    }

    private fun initRefresh() {
        layout_swipe_refresh.setOnRefreshListener {
            mViewModel.refreshQueryTopicsList()
            layout_swipe_refresh.isRefreshing = false
            mEndLessOnScrollListener.restore()
        }
    }

    private fun onQueryTopicListSuccess(list: ArrayList<Topic>) {
        mRecyclerViewAdapter.setData(list)
    }

    private fun onQueryTopicListProgress(isPregress: Boolean) {
        Log.d(TAG, "onQueryTopicListProgress ${isPregress}")
        if (isPregress) {
            progressView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            progressView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun onQueryTopicListError(throwable: Throwable) {
        Log.d(TAG, "throwable = $throwable")
    }
}