package com.ufistudio.ianlin.foodsafe.pages.main.information

import android.app.Activity
import android.arch.lifecycle.Observer
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
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
import yalantis.com.sidemenu.interfaces.Resourceble
import yalantis.com.sidemenu.interfaces.ScreenShotable
import yalantis.com.sidemenu.model.SlideMenuItem
import yalantis.com.sidemenu.util.ViewAnimator

class InformationFragment : PaneView<OnPageInteractionListener.Primary>(), OnPageInteractionListener.PrimaryView, ViewAnimator.ViewAnimatorListener,ScreenShotable {

    private lateinit var mViewModel: InformationViewModel
    private lateinit var mPagerAdapter: InformationPagerAdapter
    private lateinit var mViewAnimator: ViewAnimator<SlideMenuItem>
    private var mBitmap: Bitmap? = null

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
        drawer_layout.setScrimColor(Color.TRANSPARENT)
        left_drawer.setOnClickListener { drawer_layout.closeDrawers() }
        val menuItem: SlideMenuItem = SlideMenuItem("test", R.drawable.btn_nav_back)
        var list = java.util.ArrayList<SlideMenuItem>()
        for(i in 1..6){
            list.add(menuItem)
        }
        drawer_layout.addDrawerListener(object : ActionBarDrawerToggle(activity as Activity,drawer_layout,R.string.app_name,R.string.app_name){
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                Log.e(TAG,"onDrawerClosed")
                left_drawer.removeAllViews()
                left_drawer.invalidate()
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                Log.e(TAG,"onDrawerSlide")
                if(slideOffset > 0.6 && left_drawer.childCount == 0){
                    Log.e(TAG,"onDrawerSlide:slideOffset > 0.6 && left_drawer.childCount == 0")
                    mViewAnimator.showMenuContent()
                }
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                Log.e(TAG,"onDrawerOpened")
            }
        })
        mViewAnimator = ViewAnimator(activity as AppCompatActivity,list,this,drawer_layout,this)
    }

    private fun initListener() {
        search_bar_start.setOnClickListener {
//            addPage(Page.SEARCH, Bundle(), true) }
            Log.e(TAG,"search_bar_start click")
//            mViewAnimator.showMenuContent()
            drawer_layout.openDrawer(Gravity.LEFT)
        }
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

    override fun disableHomeButton() {
    }

    override fun enableHomeButton() {
        drawer_layout.closeDrawers()
    }

    override fun addViewToContainer(view: View?) {
        left_drawer.addView(view)
    }

    override fun onSwitch(slideMenuItem: Resourceble?, screenShotable: ScreenShotable?, position: Int): ScreenShotable {
        return screenShotable!!
    }

    override fun getBitmap(): Bitmap = mBitmap!!

    override fun takeScreenShot() {
//        val thread = object : Thread() {
//            override fun run() {
//                val bitmap = Bitmap.createBitmap(container_frame.width,
//                        container_frame.height, Bitmap.Config.ARGB_8888)
//                val canvas = Canvas(bitmap)
//                container_frame.draw(canvas)
//                mBitmap = bitmap
//            }
//        }
//        thread.start()

        container_frame.run{
            val bitmap = Bitmap.createBitmap(container_frame.width,
                    container_frame.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            container_frame.draw(canvas)
            mBitmap = bitmap
        }
    }
}