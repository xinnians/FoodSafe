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

    private var mPage: Int = 1
    private var mLastPage: Int = -1
    private var mNewsInfoList: ArrayList<NewsInfo>? = null

    fun queryNewsList() {
        if (mLastPage != -1 && mPage > mLastPage) {
            return
        }

        compositeDisposable.add(repository.getNewsInfo(mPage)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    queryNewsInfoListProgress.value = true
                }
                .doFinally { queryNewsInfoListProgress.value = false }
                .subscribe(
                        {
                            mLastPage = it.last_page
                            val list: ArrayList<NewsInfo> = it.data as ArrayList<NewsInfo>
                            mPage++

                            if (mNewsInfoList == null) {
                                mNewsInfoList = list
                            } else {
                                mNewsInfoList?.addAll(mNewsInfoList?.size!!, list)
                            }

                            queryNewsInfoListSuccess.value = mNewsInfoList
                        },
                        { queryNewsInfoListError.value = it })
        )
    }

    fun refreshQueryNewsList() {
        mPage = 0
        mNewsInfoList = null
        queryNewsList()
    }
}