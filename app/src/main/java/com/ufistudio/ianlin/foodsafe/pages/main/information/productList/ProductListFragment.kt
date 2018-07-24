package com.ufistudio.ianlin.foodsafe.pages.main.information.productList

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ufistudio.ianlin.foodsafe.AppInjector
import com.ufistudio.ianlin.foodsafe.R
import com.ufistudio.ianlin.foodsafe.componets.EndLessOnScrollListener
import com.ufistudio.ianlin.foodsafe.pages.base.InteractionView
import com.ufistudio.ianlin.foodsafe.pages.base.OnPageInteractionListener
import com.ufistudio.ianlin.foodsafe.repository.data.Product
import com.ufistudio.ianlin.foodsafe.repository.data.ProductList
import kotlinx.android.synthetic.main.fragment_product_list.*

class ProductListFragment : InteractionView<OnPageInteractionListener.PrimaryView>() {

    private lateinit var mViewModel: ProductListViewModel
    private lateinit var mAdapter: ProductListAdapter
    private var mPosition: Int = 0

    companion object {
        fun NewInstance(): ProductListFragment = ProductListFragment()
        private val TAG = ProductListFragment::class.simpleName
        const val PAGE_POSITION = "com.example.ianlin.mvvmian.pages.main.information.page_position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = AppInjector.obtainViewModel(this)

        mViewModel.queryListSuccess.observe(this, Observer { it?.let { it1 -> onQueryProductListSuccess(it1) } })
        mViewModel.queryListProgress.observe(this, Observer { onQueryProductListProgress(it!!) })
        mViewModel.queryListError.observe(this, Observer { onQueryProductListError(it!!) })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        mPosition = arguments?.getInt(PAGE_POSITION, 0)!!
        mViewModel.queryProductList(mPosition)
    }

    override fun onDestroyView() {
        recyclerView?.adapter = null
        super.onDestroyView()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = ProductListAdapter { data: Product -> itemClick(data) }
        recyclerView.adapter = mAdapter
        recyclerView.addOnScrollListener(object : EndLessOnScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(currentPage: Int) {
                mViewModel.queryProductList(mPosition, currentPage)
            }
        })
        layout_swipe_refresh.setOnRefreshListener {
            mViewModel.queryProductList(mPosition)
            layout_swipe_refresh.isRefreshing = false
        }
    }

    //進詳細頁
    private fun itemClick(data: Product) {
        getInteractionListener().openDetailPage(data)
    }

    private fun onQueryProductListSuccess(list: ProductList) {
        Log.e(TAG, "onQueryProductListSuccess call. List.size:${list.data.size}, isAdd:${list.isAdd}")
        if (list.data.size == 0) {
            recyclerView.visibility = View.GONE
            text_zero_result.visibility = View.VISIBLE
        } else {
            text_zero_result.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
        mAdapter.setItems(list.data, list.isAdd)
    }

    private fun onQueryProductListProgress(isProgress: Boolean) {
        Log.e(TAG, "onQueryProductListProgress call. $isProgress")
        if (isProgress) {
            progressBar.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun onQueryProductListError(throwable: Throwable) {
        Log.e(TAG, "onQueryProductListError call. ${throwable.message}")
    }
}