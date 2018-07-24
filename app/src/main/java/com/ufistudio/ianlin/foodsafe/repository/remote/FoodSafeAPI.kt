package com.ufistudio.ianlin.foodsafe.repository.remote

import com.ufistudio.ianlin.foodsafe.repository.data.CategoryResponse
import com.ufistudio.ianlin.foodsafe.repository.data.DefaultResponse
import com.ufistudio.ianlin.foodsafe.repository.data.ProductResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class FoodSafeAPI : RemoteAPI() {

    companion object {
        private val TAG = FoodSafeAPI::class.simpleName
        private var sInstance: FoodSafeAPI? = null
        private lateinit var mService: FoodSafeService

        //一頁的listView顯示幾個
        private const val LISTVIEW_PAGECOUNT = 10

        fun getInstance(): FoodSafeAPI? {
            if (sInstance == null) {
                synchronized(FoodSafeAPI::class) {
                    if (sInstance == null) {
                        sInstance = FoodSafeAPI()
                    }
                }
            }
            return sInstance
        }
    }

    init {
        val url = "http://150.116.195.116:8000"
        val client = getOkHttpClient()

        val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()

        mService = retrofit.create(FoodSafeService::class.java)
    }


    /*--------------------------------------------------------------------------------------------*/
    /* APIs */

    fun test(): Single<DefaultResponse> = mService.test()

    fun categoryList(): Single<CategoryResponse> = mService.getCategory()

    fun productList(field: String, search: String,page: Int): Single<ProductResponse> = mService.getProductList(field, search,LISTVIEW_PAGECOUNT,page)

}