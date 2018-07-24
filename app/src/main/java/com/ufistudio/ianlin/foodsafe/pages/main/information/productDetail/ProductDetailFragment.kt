package com.ufistudio.ianlin.foodsafe.pages.main.information.productDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ufistudio.ianlin.foodsafe.R
import com.ufistudio.ianlin.foodsafe.pages.base.OnPageInteractionListener
import com.ufistudio.ianlin.foodsafe.pages.base.PaneView

class ProductDetailFragment : PaneView<OnPageInteractionListener.Primary>(){

    companion object {
        fun NewInstance(): ProductDetailFragment = ProductDetailFragment()
        private val TAG = ProductDetailFragment::class.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

}