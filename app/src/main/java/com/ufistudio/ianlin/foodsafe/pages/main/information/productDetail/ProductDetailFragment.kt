package com.ufistudio.ianlin.foodsafe.pages.main.information.productDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ufistudio.ianlin.foodsafe.R
import com.ufistudio.ianlin.foodsafe.pages.base.OnPageInteractionListener
import com.ufistudio.ianlin.foodsafe.pages.base.PaneView
import com.ufistudio.ianlin.foodsafe.repository.data.Product
import kotlinx.android.synthetic.main.fragment_product_detail.*

class ProductDetailFragment : PaneView<OnPageInteractionListener.Primary>() {

    companion object {
        fun NewInstance(): ProductDetailFragment = ProductDetailFragment()
        private val TAG = ProductDetailFragment::class.simpleName

        const val DETAIL_DATA = "com.ufistudio.ianlin.foodsafe.pages.main.information.productDetail.detail_data"
    }

    private var mItem: Product? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            Log.e(TAG,"arguments : $it")
            if(it.containsKey(DETAIL_DATA)){
                mItem = arguments?.getParcelable(DETAIL_DATA) as Product
                mItem?.let { initView(it) }
                initListener(mItem)
            }
        }
    }

    private fun initListener(item: Product?) {
        checkReportLink.setOnClickListener {
            item?.let {
                val uri = Uri.parse(it.inspection_reports?.first())
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent) }
        }
        btn_back.setOnClickListener { activity?.onBackPressed() }
    }

    private fun initView(item: Product) {
        Glide.with(context).load(item.images?.first()).into(image_product)
        name.text = item.name
        weight.text = item.description
        category.text = item.category?.name
        company.text = item.company
        checkDateContent.text = item.inspection_date
        checkDateContent_again.text = item.inspection_date
        categoryContent_again.text = item.category?.name
    }

}