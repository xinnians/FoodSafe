package com.ufistudio.ianlin.foodsafe.pages.main.topics

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.ufistudio.ianlin.foodsafe.repository.Repository
import com.ufistudio.ianlin.foodsafe.repository.data.NewsInfo
import com.ufistudio.ianlin.foodsafe.repository.data.Topic
import com.ufistudio.ianlin.foodsafe.repository.viewModel.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class TopicsViewModel(application: Application,
                      private val mCompositeDisposable: CompositeDisposable,
                      private val mRepository: Repository) : BaseViewModel(application, mCompositeDisposable) {

    val mQueryTopicsSuccess = MutableLiveData<ArrayList<Topic>>()
    val mQueryTopicsProgress = MutableLiveData<Boolean>()
    val mQueryTopicsError = MutableLiveData<Throwable>()

    private var mPage: Int = 1
    private var mLastPage: Int = -1
    private var mTopicList: ArrayList<Topic>? = null

    fun queryTopicsList() {
        if (mLastPage != -1 && mPage > mLastPage) {
            return
        }

        mCompositeDisposable.add(mRepository.getTopics(mPage)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mQueryTopicsProgress.value = true }
                .doFinally { mQueryTopicsProgress.value = false }
                .subscribe({
                    mLastPage = it.last_page
                    val list: ArrayList<Topic> = it.data as ArrayList<Topic>
                    mPage++

                    if (mTopicList == null) {
                        mTopicList = list
                    } else {
                        mTopicList?.addAll(list)
                    }

                    mQueryTopicsSuccess.value = mTopicList
                }, {
                    mQueryTopicsError.value = it
                })
        )
    }

    fun refreshQueryTopicsList() {
        mPage = 0
        mTopicList = null
        queryTopicsList()
    }
}