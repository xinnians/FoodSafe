package com.ufistudio.ianlin.foodsafe.repository.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(val id: Int,
                    val name: String,
                    val type: String) : Parcelable

@Parcelize
data class Product(val id: Int?,
                   val categoryId: Int?,
                   val name: String?,
                   val spec: String?,
                   val description: String?,
                   val company: String?,
                   val warining_sign_text: String?,
                   val url: String?,
                   val images: ArrayList<String>?,
                   val inspection_date: String?,
                   val inspection_subject: String?,
                   val inspection_items: ArrayList<String>?,
                   val inspection_reports: ArrayList<String>?,
                   val category: Category?) : Parcelable

data class ProductList(val data: ArrayList<Product>,
                       val isAdd: Boolean = false,
                       val total: Int = 0)

@Parcelize
data class NewsInfo(val id: Int?,
                    val image: String?,
                    val updated_at: String?,
                    val created_at: String?,
                    val title: String?,
                    val date: String?,
                    val url: String?) : Parcelable

@Parcelize
data class Topic(val id: Int?,
                 val title: String?,
                 val images: ArrayList<String>?,
                 val date: String?,
                 val updated_at: String?,
                 val created_at: String?,
                 val category: String?) : Parcelable