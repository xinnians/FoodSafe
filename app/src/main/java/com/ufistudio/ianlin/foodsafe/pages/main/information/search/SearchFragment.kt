package com.ufistudio.ianlin.foodsafe.pages.main.information.search

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.ufistudio.ianlin.foodsafe.AppInjector
import com.ufistudio.ianlin.foodsafe.R
import com.ufistudio.ianlin.foodsafe.componets.EndLessOnScrollListener
import com.ufistudio.ianlin.foodsafe.pages.base.OnPageInteractionListener
import com.ufistudio.ianlin.foodsafe.pages.base.PaneView
import com.ufistudio.ianlin.foodsafe.pages.main.information.productList.ProductListAdapter
import com.ufistudio.ianlin.foodsafe.repository.data.Product
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : PaneView<OnPageInteractionListener.Primary>(){

    private lateinit var mHistoryAdapter: SearchHistoryAdapter
    private lateinit var mResultAdapter: ProductListAdapter
    private lateinit var mViewModel: SearchViewModel

    companion object {
        fun NewInstance(): SearchFragment = SearchFragment()
        private val TAG = SearchFragment::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    private fun initListener() {
        // Cancel Button - go back
        search_bar_cancel.setOnClickListener { activity?.onBackPressed() }
        // Clear Button - clear editText's text
        search_bar_clearText.setOnClickListener {
            editText.text.clear()
            mViewModel.getSearchHistoryList()
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
            when(actionId){
                EditorInfo.IME_ACTION_SEARCH -> mViewModel.queryProductList(v?.text.toString())
            }
            true
        }


    }

    private fun initView() {
        // history's view
        list_history.layoutManager = GridLayoutManager(context, 2)
        mHistoryAdapter = SearchHistoryAdapter { data: String -> onHistoryItemClick(data) }
        list_history.adapter = mHistoryAdapter
        mViewModel.getSearchHistoryList()

        // result's view
        list_result.layoutManager = LinearLayoutManager(context)
        mResultAdapter = ProductListAdapter { data: Product -> onResultItemClick(data) }
        list_result.adapter = mResultAdapter
        list_result.addOnScrollListener(object : EndLessOnScrollListener(list_result.layoutManager as LinearLayoutManager){
            override fun onLoadMore(currentPage: Int) {
                Toast.makeText(context,"onLoadMore $currentPage",Toast.LENGTH_SHORT).show()
            }
        })
        layout_swipe_refresh.setOnRefreshListener {
            mViewModel.queryProductList(editText.text.toString())
            layout_swipe_refresh.isRefreshing = false
        }

    }

    private fun onResultItemClick(data: Product) {

    }

    // call search api
    private fun onHistoryItemClick(keyword: String) {
        editText.setText(keyword)
        mViewModel.queryProductList(keyword)
    }

    private fun onGetSearchHistorySuccess(list: ArrayList<String>){
        mHistoryAdapter.setItems(list)
    }

    private fun onGetSearchHistoryProgress(isShow: Boolean){
        layout_history.visibility = View.VISIBLE
        layout_result.visibility = View.INVISIBLE
        if(isShow){
            progressBar.visibility = View.VISIBLE
            list_history.visibility = View.INVISIBLE
        }else{
            progressBar.visibility = View.INVISIBLE
            list_history.visibility = View.VISIBLE
        }
    }

    private fun onGetSearchHistoryError(throwable: Throwable){

    }

    private fun onQueryProductListSuccess(list: ArrayList<Product>){
        text_result.text = String.format(getString(R.string.search_result_count),list.size)
        mResultAdapter.setItems(list)
    }

    private fun onQueryProductListProgress(isShow: Boolean){
        layout_history.visibility = View.INVISIBLE
        layout_result.visibility = View.VISIBLE
        if(isShow){
            progressBar.visibility = View.VISIBLE
            list_result.visibility = View.INVISIBLE
        }else{
            progressBar.visibility = View.INVISIBLE
            list_result.visibility = View.VISIBLE
        }
    }

    private fun onQueryProductListError(throwable: Throwable){

    }
}