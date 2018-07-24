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
import com.ufistudio.ianlin.foodsafe.utils.ActivityUtils.clearFragmentBackStack
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : PaneViewActivity(), OnPageInteractionListener.Primary {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        disableShiftMode(navigation)

        var intent = intent
        var page = Page.INFORMATION
        var args: Bundle = Bundle()

//        intent?.let {
//            page = it.getIntExtra(PAGE,page)
//            args = it.getBundleExtra(EX)
//        }

        switchPage(R.id.fragment_container,page,args,true,false)
    }

    /**
     * 移除 BottomNavigationView 當item超過3個以上的動畫，以避免非當前item不會顯示title
     */
    @SuppressLint("RestrictedApi")
    fun disableShiftMode(navigationView: BottomNavigationView){
        var menuView = navigationView.getChildAt(0) as BottomNavigationMenuView
        try{
            val shiftingMode = menuView::class.java.getDeclaredField("mShiftingMode")
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);

            for (i in 0..menuView.childCount){
                var itemView = menuView.getChildAt(i)?.let {
                    it as BottomNavigationItemView
                }
                itemView?.setShiftingMode(false)
                itemView?.setChecked(itemView.itemData.isChecked)
            }

        }catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }catch (e: IllegalAccessException){
            e.printStackTrace()
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_information -> {
                clearFragmentBackStack(supportFragmentManager)
                switchPage(R.id.fragment_container,Page.INFORMATION, Bundle(),true,false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_news -> {
                clearFragmentBackStack(supportFragmentManager)
                switchPage(R.id.fragment_container,Page.NEWS,Bundle(),true,false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_pingtung_local_farmer -> {
                clearFragmentBackStack(supportFragmentManager)
                switchPage(R.id.fragment_container,Page.NEWS,Bundle(),true,false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_video_area -> {
                clearFragmentBackStack(supportFragmentManager)
                switchPage(R.id.fragment_container,Page.NEWS,Bundle(),true,false)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}
