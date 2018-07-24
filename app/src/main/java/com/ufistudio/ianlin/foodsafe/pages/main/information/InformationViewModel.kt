package com.ufistudio.ianlin.foodsafe.pages.main.information

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.ufistudio.ianlin.foodsafe.repository.viewModel.BaseViewModel
import com.ufistudio.ianlin.foodsafe.repository.Repository
import com.ufistudio.ianlin.foodsafe.repository.data.Category
import com.ufistudio.ianlin.foodsafe.repository.provider.preferences.SharedPreferencesProvider
import com.ufistudio.ianlin.foodsafe.repository.provider.resource.ResourceProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class InformationViewModel(application: Application,
                           private val compositeDisposable: CompositeDisposable,
                           private val repository: Repository,
                           private val preferences: SharedPreferencesProvider,
                           private val resource: ResourceProvider) : BaseViewModel(application, compositeDisposable, repository, preferences, resource) {

    val queryCategoryListSuccess = MutableLiveData<ArrayList<Category>>()
    val queryCategoryListProgress = MutableLiveData<Boolean>()
    val queryCategoryListError = MutableLiveData<Throwable>()

    fun queryCategoryList(){
        compositeDisposable.add(repository.getCategory()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { queryCategoryListProgress.value = true }
                .doFinally { queryCategoryListProgress.value = false }
                .subscribe({ queryCategoryListSuccess.value = it.data as ArrayList<Category> },
                        { queryCategoryListError.value = it}))
    }

}