package com.ufistudio.ianlin.foodsafe.pages.main.information

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import com.ufistudio.ianlin.foodsafe.AppInjector
import com.ufistudio.ianlin.foodsafe.R
import com.ufistudio.ianlin.foodsafe.constants.Constants
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

class InformationFragment : PaneView<OnPageInteractionListener.Primary>(), OnPageInteractionListener.PrimaryView, ViewAnimator.ViewAnimatorListener, ScreenShotable {

    private lateinit var mViewModel: InformationViewModel
    private lateinit var mPagerAdapter: InformationPagerAdapter
    private lateinit var mViewAnimator: ViewAnimator<SlideMenuItem>
    private var mBitmap: Bitmap? = null
    private var mActionBarDrawerToggle: ActionBarDrawerToggle? = null
    private var mSelectPosition = 1
    private var mType: String = Constants.DataType.products.toString()

    companion object {
        fun NewInstance(): InformationFragment = InformationFragment()
        private val TAG = InformationFragment::class.simpleName
        const val PAGE_TYPE = "com.example.ianlin.mvvmian.pages.main.information.page_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mType = arguments?.getString(PAGE_TYPE) ?: ""

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
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        mActionBarDrawerToggle?.let { drawer_layout.addDrawerListener(it) }
        mViewAnimator = ViewAnimator(activity as AppCompatActivity, getConformSlideMenuItems(mType), this, drawer_layout, this)

        val title = when (mType) {
            Constants.DataType.products.toString() -> getString(R.string.title_information)
            Constants.DataType.goods.toString() -> getString(R.string.title_topic)
            Constants.DataType.local.toString() -> getString(R.string.title_local_farmer)
            else -> getString(R.string.title_information)
        }

        search_bar_title.text = title
    }

    private fun initListener() {
        search_bar_start.setOnClickListener {
            val bundle: Bundle = Bundle()
            bundle.putString(PAGE_TYPE, mType)
            addPage(Page.SEARCH, bundle, true)
        }
        btn_nav_menu.setOnClickListener {
            //            mViewAnimator.showMenuContent()
            drawer_layout.openDrawer(Gravity.LEFT)
        }

        mActionBarDrawerToggle = object : ActionBarDrawerToggle(activity as Activity, drawer_layout, R.string.app_name, R.string.app_name) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                Log.e(TAG, "onDrawerClosed")
                left_drawer.removeAllViews()
                left_drawer.invalidate()
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                Log.e(TAG, "onDrawerSlide")
                if (slideOffset > 0.6 && left_drawer.childCount == 0) {
                    Log.e(TAG, "onDrawerSlide:slideOffset > 0.6 && left_drawer.childCount == 0")
                    mViewAnimator.showMenuContent(mSelectPosition)
                }
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                Log.e(TAG, "onDrawerOpened")
            }

            override fun onDrawerStateChanged(newState: Int) {
                super.onDrawerStateChanged(newState)
                Log.e(TAG, "onDrawerStateChanged:$newState")
            }
        }
    }

    private fun initViewPager() {
        mPagerAdapter = InformationPagerAdapter(childFragmentManager)
        viewPager.adapter = mPagerAdapter
        viewPager.setOnDragListener { v, event -> false }
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                mSelectPosition = position + 1
            }

        })
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
//        for (item in list) tabView.addTab(tabView.newTab().setText(item.name))

        val categoryList: ArrayList<Category> = arrayListOf()

        list.filter { category -> category.type == mType }
                .forEach { categoryList.add(it) }

        Log.d(TAG, "list filter result: $categoryList")

        mPagerAdapter.setItems(categoryList)
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

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        mActionBarDrawerToggle?.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (mActionBarDrawerToggle?.onOptionsItemSelected(item) == true) {
            return true
        }
        return super.onOptionsItemSelected(item)
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
        when (slideMenuItem?.name) {
            "close" -> return screenShotable!!
            "0" -> viewPager.setCurrentItem(0, true)
            "1" -> viewPager.setCurrentItem(1, true)
            "2" -> viewPager.setCurrentItem(2, true)
            "3" -> viewPager.setCurrentItem(3, true)
            "4" -> viewPager.setCurrentItem(4, true)
            "5" -> viewPager.setCurrentItem(5, true)
            else -> return screenShotable!!
        }

        return screenShotable!!
    }

    override fun getBitmap(): Bitmap = mBitmap!!

    override fun takeScreenShot() {
        container_frame.run {
            val bitmap = Bitmap.createBitmap(container_frame.width,
                    container_frame.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            container_frame.draw(canvas)
            mBitmap = bitmap
        }
    }

    private fun getConformSlideMenuItems(type: String): ArrayList<SlideMenuItem> {

        Log.e(TAG, "getConformSlideMenuItems : $type")

        return when (type) {
            Constants.DataType.products.toString() ->
                arrayListOf(SlideMenuItem("close", R.drawable.btn_menu_close),
                        SlideMenuItem("0", R.drawable.btn_menu_flour),
                        SlideMenuItem("1", R.drawable.btn_menu_rice),
                        SlideMenuItem("2", R.drawable.btn_menu_noodles),
                        SlideMenuItem("3", R.drawable.btn_menu_oil),
                        SlideMenuItem("4", R.drawable.btn_menu_dairy),
                        SlideMenuItem("5", R.drawable.btn_menu_egg))
            Constants.DataType.goods.toString() ->
                arrayListOf(SlideMenuItem("close", R.drawable.btn_menu_close),
                        SlideMenuItem("0", R.drawable.btn_menu_vegetables),
                        SlideMenuItem("1", R.drawable.btn_menu_wheat),
                        SlideMenuItem("2", R.drawable.btn_menu_seasoning),
                        SlideMenuItem("3", R.drawable.btn_menu_drink),
                        SlideMenuItem("4", R.drawable.btn_menu_snack),
                        SlideMenuItem("5", R.drawable.btn_menu_functional))
            Constants.DataType.local.toString() ->
                arrayListOf(SlideMenuItem("close", R.drawable.btn_menu_close),
                        SlideMenuItem("0", R.drawable.btn_menu_vegetables),
                        SlideMenuItem("1", R.drawable.btn_menu_snack),
                        SlideMenuItem("2", R.drawable.btn_menu_wheat))
            else -> ArrayList()
        }
    }
}