package com.ufistudio.ianlin.foodsafe.pages

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import com.ufistudio.ianlin.foodsafe.R
import com.ufistudio.ianlin.foodsafe.constants.Page
import com.ufistudio.ianlin.foodsafe.pages.base.OnPageInteractionListener
import com.ufistudio.ianlin.foodsafe.pages.base.PaneViewActivity
import com.ufistudio.ianlin.foodsafe.pages.main.information.productList.ProductListFragment
import com.ufistudio.ianlin.foodsafe.pages.main.information.temporarily.TemporarilyFragment
import com.ufistudio.ianlin.foodsafe.utils.ActivityUtils.clearFragmentBackStack
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : PaneViewActivity(), OnPageInteractionListener.Primary {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        disableShiftMode(navigation)
        var intent = intent

        var args = intent.extras
        var page = args.getInt("page")
//        intent?.let {
//            page = it.getIntExtra(PAGE,page)
//            args = it.getBundleExtra(EX)
//        }
        switchPage(page, args)
    }

    /**
     * 移除 BottomNavigationView 當item超過3個以上的動畫，以避免非當前item不會顯示title
     */
    @SuppressLint("RestrictedApi")
    fun disableShiftMode(navigationView: BottomNavigationView) {
        var menuView = navigationView.getChildAt(0) as BottomNavigationMenuView
        try {
            val shiftingMode = menuView::class.java.getDeclaredField("mShiftingMode")
            shiftingMode.isAccessible = true
            shiftingMode.setBoolean(menuView, false)
            shiftingMode.isAccessible = false

            for (i in 0..menuView.childCount) {
                var itemView = menuView.getChildAt(i)?.let {
                    it as BottomNavigationItemView
                }
                itemView?.setShiftingMode(false)
                itemView?.setChecked(itemView.itemData.isChecked)
            }

        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_information -> {
                clearFragmentBackStack(supportFragmentManager)
                switchPage(R.id.fragment_container, Page.INFORMATION, Bundle(), true, false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_news -> {
                clearFragmentBackStack(supportFragmentManager)
                switchPage(R.id.fragment_container, Page.NEWS, Bundle(), true, false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_topic -> {
                clearFragmentBackStack(supportFragmentManager)
                var bundle: Bundle = Bundle()
                bundle.putInt(TemporarilyFragment.PAGE_POSITION, 1)
                bundle.putString(TemporarilyFragment.PAGE_TITLE, getString(R.string.title_topic))
                switchPage(R.id.fragment_container, Page.TEMPORARILY, bundle, true, false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_local_farmer -> {
                clearFragmentBackStack(supportFragmentManager)
                var bundle: Bundle = Bundle()
                bundle.putInt(TemporarilyFragment.PAGE_POSITION, 1)
                bundle.putString(TemporarilyFragment.PAGE_TITLE, getString(R.string.title_local_farmer))
                switchPage(R.id.fragment_container, Page.TEMPORARILY, bundle, true, false)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    /**
     * 切換頁面
     * @page 傳進來的page代號
     * @bundle 需要傳遞的bundle
     */
    private fun switchPage(page: Int, bundle: Bundle) {
        var index: Int = 0
        when (page) {
            Page.NEWS -> index = 1
            Page.INFORMATION -> index = 0
            Page.TOPICS -> index = 2
        }

        switchPage(R.id.fragment_container, page, bundle, true, false)
        navigation.menu.getItem(index).isChecked = true
    }

}
