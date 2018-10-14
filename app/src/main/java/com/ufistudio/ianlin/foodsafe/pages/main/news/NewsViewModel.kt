package com.ufistudio.ianlin.foodsafe.pages.main.news

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.ufistudio.ianlin.foodsafe.repository.Repository
import com.ufistudio.ianlin.foodsafe.repository.data.NewsInfo
import com.ufistudio.ianlin.foodsafe.repository.viewModel.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class NewsViewModel(application: Application,
                    private val mCompositeDisposable: CompositeDisposable,
                    private val mRepository: Repository) : BaseViewModel(application, mCompositeDisposable) {

    val mQueryNewsInfoListSuccess = MutableLiveData<ArrayList<NewsInfo>>()
    val mQueryNewsInfoListProgress = MutableLiveData<Boolean>()
    val mQueryNewsInfoListError = MutableLiveData<Throwable>()

    private var mPage: Int = 1
    private var mLastPage: Int = -1
    private var mNewsInfoList: ArrayList<NewsInfo>? = null

    fun queryNewsList() {
        if (mLastPage != -1 && mPage > mLastPage) {
            return
        }

        mCompositeDisposable.add(mRepository.getNewsInfo(mPage)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    mQueryNewsInfoListProgress.value = true
                }
                .doFinally { mQueryNewsInfoListProgress.value = false }
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

                            mQueryNewsInfoListSuccess.value = mNewsInfoList
                        },
                        { mQueryNewsInfoListError.value = it })
        )
    }

    fun refreshQueryNewsList() {
        mPage = 0
        mNewsInfoList = null
        queryNewsList()
    }
}