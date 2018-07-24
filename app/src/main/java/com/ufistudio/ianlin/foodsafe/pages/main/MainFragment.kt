package com.ufistudio.ianlin.foodsafe.pages.main

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ufistudio.ianlin.foodsafe.R
import com.ufistudio.ianlin.foodsafe.pages.base.OnPageInteractionListener
import com.ufistudio.ianlin.foodsafe.pages.base.PaneView
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : PaneView<OnPageInteractionListener.Main>(), OnPageInteractionListener.MainView {

    companion object {
        fun NewInstance(): MainFragment = MainFragment()

        val TAG = MainFragment::class.simpleName
    }

    lateinit var mainPagerAdapter: MainPagerAdapter
    lateinit var pagerController: PagerController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var layout = inflater.inflate(R.layout.fragment_main, container, false)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    private fun initViewPager() {
        mainPagerAdapter = MainPagerAdapter(childFragmentManager)
        viewPager?.adapter = mainPagerAdapter
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                val id: Int = when (position) {
                    0 -> R.id.navigation_information
                    1 -> R.id.navigation_news
                    2 -> R.id.navigation_pingtung_local_farmer
                    3 -> R.id.navigation_video_area
                    else -> R.id.navigation_information
                }
                getInteractionListener().setNavigationViewPosition(id)
            }

        })
        pagerController = object : PagerController {
            override fun setPagePosition(position: Int) {
                viewPager.setCurrentItem(position,true)
            }
        }
        getInteractionListener().setPagerController(pagerController)
    }

    override fun onDestroy() {
        super.onDestroy()
        getInteractionListener().clearPagerController()
    }

    override fun pressBack() = getInteractionListener().pressBack()

    override fun hideFullScreenOverlay() = getInteractionListener().hideFullScreenOverlay()

    interface PagerController {
        fun setPagePosition(position: Int)
    }

}
