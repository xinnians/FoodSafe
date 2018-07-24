package com.ufistudio.ianlin.foodsafe.repository.data

data class Category(val id: Int,
                    val name: String)

data class Product(val id: Int,
                   val categoryId: Int,
                   val company: String,
                   val name: String,
                   val description: String,
                   val url: String,
                   val images: ArrayList<String>,
                   val inspection_reports: ArrayList<String>,
                   val inspection_date: String,
                   val category: Category)

data class ProductList(val data: ArrayList<Product>? = ArrayList(),
                       val isAdd: Boolean = false)