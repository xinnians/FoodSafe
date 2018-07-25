package com.ufistudio.ianlin.foodsafe.pages.main.information

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.perf.metrics.AddTrace
import com.ufistudio.ianlin.foodsafe.AppInjector
import com.ufistudio.ianlin.foodsafe.R
import com.ufistudio.ianlin.foodsafe.constants.Page
import com.ufistudio.ianlin.foodsafe.pages.base.OnPageInteractionListener
import com.ufistudio.ianlin.foodsafe.pages.base.PaneView
import com.ufistudio.ianlin.foodsafe.pages.main.information.productDetail.ProductDetailFragment.Companion.DETAIL_DATA
import com.ufistudio.ianlin.foodsafe.repository.data.Category
import com.ufistudio.ianlin.foodsafe.repository.data.Product
import kotlinx.android.synthetic.main.fragment_information.*

class InformationFragment : PaneView<OnPageInteractionListener.Primary>(), OnPageInteractionListener.PrimaryView {

    private lateinit var mViewModel: InformationViewModel
    private lateinit var mPagerAdapter: InformationPagerAdapter

    companion object {
        fun NewInstance(): InformationFragment = InformationFragment()
        private val TAG = InformationFragment::class.simpleName
    }

    @AddTrace(name = "onCreateTrace", enabled = true)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = AppInjector.obtainViewModel(this)

        mViewModel.queryCategoryListSuccess.observe(this, Observer { it?.let { it1 -> onQueryCategoryListSuccess(it1) } })
        mViewModel.queryCategoryListProgress.observe(this, Observer { onQueryCategoryListProgress(it!!) })
        mViewModel.queryCategoryListError.observe(this, Observer { onQueryCategoryListError(it!!) })

        mViewModel.queryCategoryList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        initListener()
    }

    private fun initListener() {
        search_bar_start.setOnClickListener { addPage(Page.SEARCH, Bundle(), true) }
        tabView.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabView))
    }

    private fun initViewPager() {
        mPagerAdapter = InformationPagerAdapter(childFragmentManager)
        viewPager.adapter = mPagerAdapter
    }

    override fun pressBack() {
        getInteractionListener().pressBack()
    }

    override fun hideFullScreenOverlay() {
        getInteractionListener().hideFullScreenOverlay()
    }

    //進詳細頁
    override fun openDetailPage(item: Product) {
        val args = Bundle()
        args.putParcelable(DETAIL_DATA, item)
        addPage(Page.PRODUCT_DETAIL, args, true, true)
    }

    private fun onQueryCategoryListSuccess(list: ArrayList<Category>) {
        Log.d(TAG, "onQueryCategoryListSuccess call. ${list.size}")
        for (item in list) tabView.addTab(tabView.newTab().setText(item.name))
        mPagerAdapter.setItems(list)
    }

    private fun onQueryCategoryListProgress(isProgress: Boolean) {
        Log.d(TAG, "onQueryCategoryListProgress call. ${isProgress}")
        if (isProgress) {
            progressView.visibility = View.VISIBLE
            viewPager.visibility = View.GONE
        } else {
            progressView.visibility = View.GONE
            viewPager.visibility = View.VISIBLE
        }

    }

    private fun onQueryCategoryListError(throwable: Throwable) {
        Log.e(TAG, "onQueryCategoryListError call. ${throwable.message}")
    }

}