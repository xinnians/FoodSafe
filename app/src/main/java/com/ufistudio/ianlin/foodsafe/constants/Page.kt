package com.ufistudio.ianlin.foodsafe.constants

import android.os.Bundle
import android.support.v4.app.Fragment
import com.ufistudio.ianlin.foodsafe.pages.main.guide.GuideFragment
import com.ufistudio.ianlin.foodsafe.pages.main.information.InformationFragment
import com.ufistudio.ianlin.foodsafe.pages.main.information.productDetail.ProductDetailFragment
import com.ufistudio.ianlin.foodsafe.pages.main.information.productList.ProductListFragment
import com.ufistudio.ianlin.foodsafe.pages.main.news.NewsFragment
import com.ufistudio.ianlin.foodsafe.pages.main.information.search.SearchFragment
import com.ufistudio.ianlin.foodsafe.pages.main.topics.TopicsFragment
import com.ufistudio.ianlin.foodsafe.utils.views.WebViewFragment
import java.lang.IllegalArgumentException

object Page {

    const val PAGE = "page"
    const val ARG_PAGE = "com.example.ianlin.mvvmian.constants.Page.ARG_PAGE"

    const val INVALID_PAGE = -1

    const val INFORMATION = 1001
    const val PRODUCT_LIST = 1002
    const val NEWS = 1003
    const val SEARCH = 1004
    const val PRODUCT_DETAIL = 1005
    const val TOPICS = 1006

    const val WEB_VIEW = 2001

    const val GUIDE = 3001

    /*--------------------------------------------------------------------------------------------*/
    /* Helpers */
    fun tag(page: Int): String = "page_$page"

    fun view(page: Int, args: Bundle): Fragment {
        var result: Fragment

        when (page) {
            INFORMATION -> result = InformationFragment.NewInstance()
            PRODUCT_LIST -> result = ProductListFragment.NewInstance()
            NEWS -> result = NewsFragment.newInstance()
            SEARCH -> result = SearchFragment.NewInstance()
            PRODUCT_DETAIL -> result = ProductDetailFragment.NewInstance()
            TOPICS -> result = TopicsFragment.newInstance()
            WEB_VIEW -> result = WebViewFragment.newInstance()
            GUIDE-> result =GuideFragment.newInstance()
            else -> throw IllegalArgumentException("No match view! page = $page")
        }

        args.putInt(ARG_PAGE, page)
        putData(result, args)

        return result
    }

    private fun putData(fragment: Fragment, data: Bundle) {
        var args = fragment.arguments;
        if (args == null) {
            fragment.arguments = data
        } else {
            args.putAll(data)
        }
    }
}