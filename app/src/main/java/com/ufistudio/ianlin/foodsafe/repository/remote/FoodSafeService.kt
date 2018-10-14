package com.ufistudio.ianlin.foodsafe.repository.remote

import com.ufistudio.ianlin.foodsafe.repository.data.CategoryResponse
import com.ufistudio.ianlin.foodsafe.repository.data.NewsResponse
import com.ufistudio.ianlin.foodsafe.repository.data.ProductResponse
import com.ufistudio.ianlin.foodsafe.repository.data.TopicsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodSafeService {

    @GET("api/categories")
    fun getCategory(): Single<CategoryResponse>

    @GET("api/products")
    fun getProductList(@Query("searchFields") searchFields: String,
                       @Query("search") name: String,
                       @Query("limit") limit: Int,
                       @Query("page") page: Int): Single<ProductResponse>

    @GET("api/knowledge")
    fun getNewsInfo(@Query("page") page: Int)
            : Single<NewsResponse>

    @GET("api/topics")
    fun getTopics(@Query("page") page: Int)
            : Single<TopicsResponse>

}