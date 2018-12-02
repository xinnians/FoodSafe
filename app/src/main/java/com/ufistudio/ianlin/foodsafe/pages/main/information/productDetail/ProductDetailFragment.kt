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
        private val DATA_EMPTY = "——"
    }

    private var mItem: Product? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            Log.e(TAG, "arguments : $it")
            if (it.containsKey(DETAIL_DATA)) {
                mItem = arguments?.getParcelable(DETAIL_DATA) as Product
                mItem?.let { initView(it) }
                initListener(mItem)
            }
        }
    }

    private fun initListener(item: Product?) {

        btn_back.setOnClickListener { activity?.onBackPressed() }

        item?.let { product ->
            product.inspection_reports?.let { reports ->
                if (reports.first().isNotEmpty()) {
                    checkReportLink.setOnClickListener {
                        val uri = Uri.parse(reports.first())
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }
                    setReportBlockVisibility(View.VISIBLE)
                    return
                } else {
                    setReportBlockVisibility(View.GONE)
                }
            }
        }

        setReportBlockVisibility(View.GONE)

    }

    private fun initView(item: Product) {
        Glide.with(context).load(item.images?.first()).into(image_product)
        name.text = item.name
        weight.text = item.spec
        category.text = item.category?.name
        company.text = item.company

        val inspectionDate = item.inspection_date ?: DATA_EMPTY
        var inspectionItems = ""

        item.inspection_items?.let { inspection_items ->
            for (i in 0 until inspection_items.size) {
                inspectionItems += inspection_items[i]
                inspectionItems += "\n"
            }
        }

        if (inspectionItems.isEmpty()) inspectionItems = DATA_EMPTY

        if (inspectionDate == DATA_EMPTY && inspectionItems == DATA_EMPTY) {
            setInspectionBlockVisibility(View.GONE)
        } else {
            setInspectionBlockVisibility(View.VISIBLE)
        }

        checkDateContent_again.text = inspectionDate
        ckeckItemsContent.text = inspectionItems

    }

    private fun setReportBlockVisibility(visibility: Int) {
        checkReportLink.visibility = visibility
        block_gray4.visibility = visibility
    }

    private fun setInspectionBlockVisibility(visibility: Int) {
        layout_inspection.visibility = visibility
    }

}