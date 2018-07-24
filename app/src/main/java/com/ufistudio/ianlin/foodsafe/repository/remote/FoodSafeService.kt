package com.ufistudio.ianlin.foodsafe.repository.remote

import com.ufistudio.ianlin.foodsafe.repository.data.CategoryResponse
import com.ufistudio.ianlin.foodsafe.repository.data.DefaultResponse
import com.ufistudio.ianlin.foodsafe.repository.data.ProductResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodSafeService {

    @GET("test")
    fun test(): Single<DefaultResponse>

    @GET("api/categories")
    fun getCategory(): Single<CategoryResponse>

    @GET("api/products")
    fun getProductList(@Query("searchFields") searchFields: String,
                       @Query("search") name: String,
                       @Query("limit") limit: Int,
                       @Query("page") page: Int): Single<ProductResponse>

}