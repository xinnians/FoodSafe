package com.ufistudio.ianlin.foodsafe.repository.data

interface Base{
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
                           val data: ArrayList<Product> = ArrayList(),
                           val current_page: Int,
                           val last_page: Int) : Base