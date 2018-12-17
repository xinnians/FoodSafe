package com.ufistudio.ianlin.foodsafe.pages

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.view.View
import com.ufistudio.ianlin.foodsafe.R
import com.ufistudio.ianlin.foodsafe.constants.Constants
import com.ufistudio.ianlin.foodsafe.constants.Page
import com.ufistudio.ianlin.foodsafe.pages.base.OnPageInteractionListener
import com.ufistudio.ianlin.foodsafe.pages.base.PaneViewActivity
import com.ufistudio.ianlin.foodsafe.pages.main.information.InformationFragment.Companion.PAGE_TYPE
import com.ufistudio.ianlin.foodsafe.pages.main.information.productList.ProductListFragment
import com.ufistudio.ianlin.foodsafe.pages.main.information.temporarily.TemporarilyFragment
import com.ufistudio.ianlin.foodsafe.utils.ActivityUtils.clearFragmentBackStack
import kotlinx.android.synthetic.main.activity_main.*
import yalantis.com.sidemenu.model.SlideMenuItem

class MainActivity : PaneViewActivity(), OnPageInteractionListener.Primary {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        var intent = intent

        var args = intent.extras
        var page = args.getInt("page")
//        intent?.let {
//            page = it.getIntExtra(PAGE,page)
//            args = it.getBundleExtra(EX)
//        }
        switchPage(page, args)
    }

    private fun init(){

        layout_information.setOnClickListener {
            clearFragmentBackStack(supportFragmentManager)
            val bundle: Bundle = Bundle()
            bundle.putString(PAGE_TYPE, Constants.DataType.products.toString())
            switchPage(R.id.fragment_container, Page.INFORMATION, bundle, true, false)
            setButtonClick(view_icon_information)}

        layout_topic.setOnClickListener {
            clearFragmentBackStack(supportFragmentManager)
//            var bundle: Bundle = Bundle()
//            bundle.putInt(TemporarilyFragment.PAGE_POSITION, 1)
//            bundle.putString(TemporarilyFragment.PAGE_TITLE, getString(R.string.title_topic))
//            switchPage(R.id.fragment_container, Page.TEMPORARILY, bundle, true, false)
            val bundle: Bundle = Bundle()
            bundle.putString(PAGE_TYPE, Constants.DataType.goods.toString())
            switchPage(R.id.fragment_container, Page.INFORMATION, bundle, true, false)
            setButtonClick(view_icon_topic)}

        layout_local_farmer.setOnClickListener {
            clearFragmentBackStack(supportFragmentManager)
//            var bundle: Bundle = Bundle()
//            bundle.putInt(TemporarilyFragment.PAGE_POSITION, 1)
//            bundle.putString(TemporarilyFragment.PAGE_TITLE, getString(R.string.title_local_farmer))
//            switchPage(R.id.fragment_container, Page.TEMPORARILY, bundle, true, false)
            val bundle: Bundle = Bundle()
            bundle.putString(PAGE_TYPE, Constants.DataType.local.toString())
            switchPage(R.id.fragment_container, Page.INFORMATION, bundle, true, false)
            setButtonClick(view_icon_local_farmer)}

        layout_news.setOnClickListener {
            clearFragmentBackStack(supportFragmentManager)
            switchPage(R.id.fragment_container, Page.NEWS, Bundle(), true, false)
            setButtonClick(view_icon_news)}

    }

    /**
     * 切換頁面
     * @page 傳進來的page代號
     * @bundle 需要傳遞的bundle
     */
    private fun switchPage(page: Int, bundle: Bundle) {
        when (page) {
            Page.NEWS -> setButtonClick(view_icon_news)
            Page.INFORMATION -> {
                setButtonClick(view_icon_information)
                bundle.putString(PAGE_TYPE, Constants.DataType.products.toString())
            }
            Page.TOPICS -> {
                setButtonClick(view_icon_topic)
                bundle.putString(PAGE_TYPE, Constants.DataType.goods.toString())
            }
            Page.LOCAL_FARMER -> {
                setButtonClick(view_icon_local_farmer)
                bundle.putString(PAGE_TYPE, Constants.DataType.local.toString())
            }
        }



        switchPage(R.id.fragment_container, page, bundle, true, false)

    }

    private fun setButtonClick(view: View){
        view_icon_information.setImageResource(if(view == view_icon_information) R.drawable.btn_data_act else R.drawable.btn_data_pas)
        view_icon_topic.setImageResource(if(view == view_icon_topic) R.drawable.btn_safe_act else R.drawable.btn_safe_pas)
        view_icon_local_farmer.setImageResource(if(view == view_icon_local_farmer) R.drawable.btn_farm_act else R.drawable.btn_farm_pas)
        view_icon_news.setImageResource(if(view == view_icon_news) R.drawable.btn_news_act else R.drawable.btn_news_pas)
    }

}
