package com.ufistudio.ianlin.foodsafe.pages.main.news

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.ufistudio.ianlin.foodsafe.repository.Repository
import com.ufistudio.ianlin.foodsafe.repository.data.NewsInfo
import com.ufistudio.ianlin.foodsafe.repository.viewModel.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class NewsViewModel(application: Application,
                    private val compositeDisposable: CompositeDisposable,
                    private val repository: Repository) : BaseViewModel(application, compositeDisposable) {

    val queryNewsInfoListSuccess = MutableLiveData<ArrayList<NewsInfo>>()
    val queryNewsInfoListProgress = MutableLiveData<Boolean>()
    val queryNewsInfoListError = MutableLiveData<Throwable>()

    fun queryNewsList() {
        compositeDisposable.add(repository.getNewsInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { queryNewsInfoListProgress.value = true }
                .doFinally { queryNewsInfoListProgress.value = false }
                .subscribe({ queryNewsInfoListSuccess.value = it.data as ArrayList<NewsInfo> },
                        { queryNewsInfoListError.value = it }))
    }
}