package com.ufistudio.ianlin.foodsafe.repository.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(val id: Int,
                    val name: String) : Parcelable

@Parcelize
data class Product(val id: Int?,
                   val categoryId: Int?,
                   val company: String?,
                   val name: String?,
                   val description: String?,
                   val url: String?,
                   val images: ArrayList<String>?,
                   val inspection_reports: ArrayList<String>?,
                   val inspection_date: String?,
                   val category: Category?) : Parcelable

data class ProductList(val data: ArrayList<Product>,
                       val isAdd: Boolean = false,
                       val total: Int = 0)

@Parcelize
data class NewsInfo(val id: Int?,
                    val images: ArrayList<String>?,
                    val updated_at: String?,
                    val created_at: String?,
                    val title: String?,
                    val date: String?,
                    val action: String?) : Parcelable