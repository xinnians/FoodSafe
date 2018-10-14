package com.ufistudio.ianlin.foodsafe.repository

import android.app.Application
import com.ufistudio.ianlin.foodsafe.repository.data.CategoryResponse
import com.ufistudio.ianlin.foodsafe.repository.data.NewsResponse
import com.ufistudio.ianlin.foodsafe.repository.data.ProductResponse
import com.ufistudio.ianlin.foodsafe.repository.data.TopicsResponse
import com.ufistudio.ianlin.foodsafe.repository.provider.preferences.PreferencesKey
import com.ufistudio.ianlin.foodsafe.repository.provider.preferences.PreferencesKey.SEARCH_HISTORY
import com.ufistudio.ianlin.foodsafe.repository.provider.preferences.SharedPreferencesProvider
import com.ufistudio.ianlin.foodsafe.repository.remote.FoodSafeAPI
import com.ufistudio.ianlin.foodsafe.repository.remote.RemoteAPI
import com.ufistudio.ianlin.foodsafe.utils.MiscUtils
import io.reactivex.Single

class Repository(private var application: Application, private val sharedPreferencesProvider: SharedPreferencesProvider) {

    val TAG = Repository::class.simpleName

    init {
        RemoteAPI.init(application)
    }

    fun getCategory(): Single<CategoryResponse> {
        return FoodSafeAPI.getInstance()!!.categoryList()
    }

    fun getProduct(search: Any, page: Int = 1): Single<ProductResponse> {
        val searchFields = when (search) {
            is Int -> "category_id:"
            is String -> "name:"
            else -> "name:"
        }
        return FoodSafeAPI.getInstance()!!.productList(searchFields.plus("like"), searchFields.plus(search), page)
    }

    fun getNewsInfo(page: Int): Single<NewsResponse> {
        return FoodSafeAPI.getInstance()!!.newsInfoList(page)
    }

    fun getTopics(page: Int): Single<TopicsResponse> {
        return FoodSafeAPI.getInstance()!!.topicList(page)
    }

    // local

    fun getSearchHistoryList(): Single<ArrayList<String>>? {
        val jsonString = sharedPreferencesProvider.sharedPreferences().getString(PreferencesKey.SEARCH_HISTORY, "")
        return Single.just(MiscUtils.parseJSONList(jsonString))
    }

    fun saveSearchHistoryList(list: ArrayList<String>?) {
        val jsonString = MiscUtils.toJSONString(list)
        sharedPreferencesProvider.sharedPreferences().edit().putString(SEARCH_HISTORY, jsonString).apply()
    }

}