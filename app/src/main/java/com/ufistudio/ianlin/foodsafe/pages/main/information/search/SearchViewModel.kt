package com.ufistudio.ianlin.foodsafe.pages.main.information.search

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.ufistudio.ianlin.foodsafe.repository.viewModel.BaseViewModel
import com.ufistudio.ianlin.foodsafe.repository.Repository
import com.ufistudio.ianlin.foodsafe.repository.data.Product
import com.ufistudio.ianlin.foodsafe.repository.provider.preferences.SharedPreferencesProvider
import com.ufistudio.ianlin.foodsafe.repository.provider.resource.ResourceProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SearchViewModel(application: Application,
                      private val compositeDisposable: CompositeDisposable,
                      private val repository: Repository,
                      private val preferences: SharedPreferencesProvider,
                      private val resource: ResourceProvider) : BaseViewModel(application, compositeDisposable, repository, preferences, resource) {

    var mSearchHistoryList: ArrayList<String>? = null

    val getSearchHistorySuccess = MutableLiveData<ArrayList<String>>()
    val getSearchHistoryProgress = MutableLiveData<Boolean>()
    val getSearchHistoryError = MutableLiveData<Throwable>()

    val queryProductListSuccess = MutableLiveData<ArrayList<Product>>()
    val queryProductListProgress = MutableLiveData<Boolean>()
    val queryProductListError = MutableLiveData<Throwable>()

    fun getSearchHistoryList() {
        if (mSearchHistoryList != null) {
            getSearchHistoryProgress.value = false
            getSearchHistorySuccess.value = mSearchHistoryList
            return
        }
        repository.getSearchHistoryList()
                ?.delay(1,TimeUnit.SECONDS)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { getSearchHistoryProgress.value = true }
                ?.doFinally { getSearchHistoryProgress.value = false }
                ?.subscribe({
                    mSearchHistoryList = it
                    getSearchHistorySuccess.value = it
                }, { getSearchHistoryError.value = it })?.let { compositeDisposable.add(it) }
    }

    fun saveSearchHistoryList(keyword: String) {
        if (mSearchHistoryList == null)
            mSearchHistoryList = ArrayList()

        mSearchHistoryList = mSearchHistoryList?.let {
            it.add(0,keyword)
            if (it.size > 20) {
                ArrayList(it.subList(0, 20))
            } else it
        }
        repository.saveSearchHistoryList(mSearchHistoryList!!)
        getSearchHistorySuccess.value = mSearchHistoryList
    }

    fun queryProductList(keyword: String) {
        saveSearchHistoryList(keyword)
        compositeDisposable.add(repository.getProduct(keyword)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { queryProductListProgress.value = true }
                .doFinally { queryProductListProgress.value = false }
                .subscribe({ queryProductListSuccess.value = it.data },
                        { queryProductListError.value = it }))
    }
}