package com.ufistudio.ianlin.foodsafe.repository.data

interface Base {
    val code: Int
    val message: String
}

data class DefaultResponse(override val code: Int,
                           override val message: String) : Base

data class CategoryResponse(override val code: Int,
                            override val message: String,
                            val data: List<Category>) : Base

data class ProductResponse(override val code: Int,
                           override val message: String,
                           val data: ArrayList<Product>? = ArrayList(),
                           val current_page: Int,
                           val last_page: Int,
                           val total: Int) : Base

data class NewsResponse(override val code: Int,
                        override val message: String,
                        val data: List<NewsInfo>,
                        val last_page: Int) : Base

data class TopicsResponse(override val code: Int,
                          override val message: String,
                          val data: List<Topic>,
                          val last_page: Int) : Base