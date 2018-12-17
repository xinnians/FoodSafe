package com.ufistudio.ianlin.foodsafe.pages.main.information.search

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.ufistudio.ianlin.foodsafe.AppInjector
import com.ufistudio.ianlin.foodsafe.R
import com.ufistudio.ianlin.foodsafe.componets.EndLessOnScrollListener
import com.ufistudio.ianlin.foodsafe.constants.Page
import com.ufistudio.ianlin.foodsafe.pages.base.OnPageInteractionListener
import com.ufistudio.ianlin.foodsafe.pages.base.PaneView
import com.ufistudio.ianlin.foodsafe.pages.main.information.productDetail.ProductDetailFragment.Companion.DETAIL_DATA
import com.ufistudio.ianlin.foodsafe.pages.main.information.productList.ProductListAdapter
import com.ufistudio.ianlin.foodsafe.repository.data.Product
import com.ufistudio.ianlin.foodsafe.repository.data.ProductList
import kotlinx.android.synthetic.main.fragment_search.*
import android.view.inputmethod.InputMethodManager
import com.ufistudio.ianlin.foodsafe.constants.Constants
import com.ufistudio.ianlin.foodsafe.pages.main.information.InformationFragment


class SearchFragment : PaneView<OnPageInteractionListener.Primary>() {

    private lateinit var mHistoryAdapter: SearchHistoryAdapter
    private lateinit var mResultAdapter: ProductListAdapter
    private lateinit var mViewModel: SearchViewModel
    private var mType: String = Constants.DataType.products.toString()

    companion object {
        fun NewInstance(): SearchFragment = SearchFragment()
        private val TAG = SearchFragment::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mType = arguments?.getString(InformationFragment.PAGE_TYPE) ?: ""

        mViewModel = AppInjector.obtainViewModel(this)

        mViewModel.getSearchHistorySuccess.observe(this, Observer { it?.let { it1 -> onGetSearchHistorySuccess(it1) } })
        mViewModel.getSearchHistoryProgress.observe(this, Observer { it?.let { it1 -> onGetSearchHistoryProgress(it1) } })
        mViewModel.getSearchHistoryError.observe(this, Observer { it?.let { it1 -> onGetSearchHistoryError(it1) } })

        mViewModel.queryProductListSuccess.observe(this, Observer { it?.let { it1 -> onQueryProductListSuccess(it1) } })
        mViewModel.queryProductListProgress.observe(this, Observer { it?.let { it1 -> onQueryProductListProgress(it1) } })
        mViewModel.queryProductListError.observe(this, Observer { it?.let { it1 -> onQueryProductListError(it1) } })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    override fun onPause() {
        super.onPause()
        editText.clearFocus()
    }

    override fun onDestroyView() {
        list_history?.adapter = null
        list_result?.adapter = null
        super.onDestroyView()
    }

    private fun initListener() {
        // Cancel Button - go back
        search_bar_cancel.setOnClickListener { activity?.onBackPressed() }
        // Clear Button - clear editText's text
        search_bar_clearText.setOnClickListener {
            editText.text.clear()
            mViewModel.getSearchHistoryList(mType)
        }
        // editText's action
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val inputText = s.toString()
                search_bar_clearText.visibility = if (inputText.isNotEmpty()) View.VISIBLE else View.INVISIBLE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
        editText.setOnEditorActionListener { v, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    mViewModel.queryProductList(v?.text.toString(),dataType = mType)
                    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view?.windowToken, 0)
                }
            }
            true
        }
    }

    private fun initView() {
        // history's view
        list_history.layoutManager = GridLayoutManager(context, 2)
        mHistoryAdapter = SearchHistoryAdapter { data: String -> onHistoryItemClick(data) }
        list_history.adapter = mHistoryAdapter
        mViewModel.getSearchHistoryList(mType)

        // result's view
        list_result.layoutManager = LinearLayoutManager(context)
        mResultAdapter = ProductListAdapter { data: Product -> onResultItemClick(data) }
        list_result.adapter = mResultAdapter
        list_result.addOnScrollListener(object : EndLessOnScrollListener(list_result.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(currentPage: Int) {
                Log.e(TAG, "[onLoadMore] currentPage:$currentPage")
                mViewModel.queryProductList(editText.text.toString(), currentPage,mType)
            }
        })
        layout_swipe_refresh.setOnRefreshListener {
            mViewModel.queryProductList(editText.text.toString(),dataType = mType)
            layout_swipe_refresh.isRefreshing = false
        }
    }

    private fun onResultItemClick(data: Product) {
        var args = Bundle()
        args.putParcelable(DETAIL_DATA, data)
        addPage(Page.PRODUCT_DETAIL, args, true, true)
    }

    // call search api
    private fun onHistoryItemClick(keyword: String) {
        editText.setText(keyword)
        mViewModel.queryProductList(keyword,dataType = mType)
    }

    private fun onGetSearchHistorySuccess(list: ArrayList<String>) {
        Log.e(TAG,"[onGetSearchHistorySuccess] list:$list")
        mHistoryAdapter.setItems(list)
    }

    private fun onGetSearchHistoryProgress(isShow: Boolean) {
        layout_history.visibility = View.VISIBLE
        layout_result.visibility = View.INVISIBLE
        if (isShow) {
            progressBar.visibility = View.VISIBLE
            list_history.visibility = View.INVISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
            list_history.visibility = View.VISIBLE
        }
    }

    private fun onGetSearchHistoryError(throwable: Throwable) {

    }

    private fun onQueryProductListSuccess(list: ProductList) {
        if (list.data.size == 0 && !list.isAdd) {
            text_result.visibility = View.INVISIBLE
            list_result.visibility = View.INVISIBLE
            text_zero_result.visibility = View.VISIBLE
        } else {
            text_zero_result.visibility = View.INVISIBLE
            text_result.visibility = View.VISIBLE
            list_result.visibility = View.VISIBLE
            text_result.text = String.format(getString(R.string.search_result_count), list.total)
        }
        mResultAdapter.setItems(list.data, list.isAdd)
    }

    private fun onQueryProductListProgress(isShow: Boolean) {
        layout_history.visibility = View.INVISIBLE
        layout_result.visibility = View.VISIBLE
        if (isShow) {
            progressBar.visibility = View.VISIBLE
            list_result.visibility = View.INVISIBLE
            text_result.visibility = View.INVISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun onQueryProductListError(throwable: Throwable) {
        Log.e(TAG, "onQueryProductListError call. ${throwable.message}")
    }
}