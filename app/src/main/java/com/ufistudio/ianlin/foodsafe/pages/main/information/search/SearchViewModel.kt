package com.ufistudio.ianlin.foodsafe.pages.main.information.search

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.ufistudio.ianlin.foodsafe.repository.viewModel.BaseViewModel
import com.ufistudio.ianlin.foodsafe.repository.Repository
import com.ufistudio.ianlin.foodsafe.repository.data.Product
import com.ufistudio.ianlin.foodsafe.repository.data.ProductList
import com.ufistudio.ianlin.foodsafe.repository.provider.preferences.SharedPreferencesProvider
import com.ufistudio.ianlin.foodsafe.repository.provider.resource.ResourceProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SearchViewModel(application: Application,
                      private val compositeDisposable: CompositeDisposable,
                      private val repository: Repository) : BaseViewModel(application, compositeDisposable) {

    var mSearchHistoryList: ArrayList<String>? = null

    val getSearchHistorySuccess = MutableLiveData<ArrayList<String>>()
    val getSearchHistoryProgress = MutableLiveData<Boolean>()
    val getSearchHistoryError = MutableLiveData<Throwable>()

    val queryProductListSuccess = MutableLiveData<ProductList>()
    val queryProductListProgress = MutableLiveData<Boolean>()
    val queryProductListError = MutableLiveData<Throwable>()

    companion object {
        val TAG = SearchViewModel::class.simpleName
    }

    fun getSearchHistoryList(dataType: String) {
        if (mSearchHistoryList != null) {
            getSearchHistoryProgress.value = false
            getSearchHistorySuccess.value = mSearchHistoryList
            return
        }
        repository.getSearchHistoryList(dataType)
                ?.delay(1, TimeUnit.SECONDS)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { getSearchHistoryProgress.value = true }
                ?.doFinally { getSearchHistoryProgress.value = false }
                ?.subscribe({
                    mSearchHistoryList = it
                    getSearchHistorySuccess.value = it
                }, { getSearchHistoryError.value = it })?.let { compositeDisposable.add(it) }
    }

    private fun saveSearchHistoryList(keyword: String, dataType: String) {
        Log.e(TAG,"[saveSearchHistoryList] keyword:$keyword, dataType:$dataType")
        if (mSearchHistoryList == null)
            mSearchHistoryList = ArrayList()

        mSearchHistoryList = mSearchHistoryList?.let {
            if((it.size > 0 && it[0] != keyword) || it.size == 0){
                it.add(0, keyword)
            }
            if (it.size > 20) {
                ArrayList(it.subList(0, 20))
            } else it
        }
        repository.saveSearchHistoryList(mSearchHistoryList!!, dataType)
        getSearchHistorySuccess.value = mSearchHistoryList
    }

    fun queryProductList(keyword: String, page: Int = 1, dataType: String) {
        Log.e(TAG,"[queryProductList] keyword:$keyword, dataType:$dataType, page:$page")
        saveSearchHistoryList(keyword, dataType)
        compositeDisposable.add(repository.getProduct(keyword, page, dataType)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { if (page == 1) queryProductListProgress.value = true }
                .doFinally { if (page == 1) queryProductListProgress.value = false }
                .subscribe({
                    queryProductListSuccess.value = ProductList(it.data
                            ?: ArrayList(), page != 1, it.total)
                },
                        { queryProductListError.value = it }))
    }
}