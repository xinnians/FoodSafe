package com.ufistudio.ianlin.foodsafe.pages.main.information.productList

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.ufistudio.ianlin.foodsafe.repository.viewModel.BaseViewModel
import com.ufistudio.ianlin.foodsafe.repository.Repository
import com.ufistudio.ianlin.foodsafe.repository.data.ProductList
import com.ufistudio.ianlin.foodsafe.repository.provider.preferences.SharedPreferencesProvider
import com.ufistudio.ianlin.foodsafe.repository.provider.resource.ResourceProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProductListViewModel(application: Application,
                           private val compositeDisposable: CompositeDisposable,
                           private val repository: Repository,
                           private val preferences: SharedPreferencesProvider,
                           private val resource: ResourceProvider) : BaseViewModel(application, compositeDisposable, repository, preferences, resource) {

    val queryListSuccess = MutableLiveData<ProductList>()
    val queryListProgress = MutableLiveData<Boolean>()
    val queryListError = MutableLiveData<Throwable>()

    private var mProductList: ProductList? = null

    fun queryProductList(categoryId: Int, page: Int = 1) {

        if (mProductList != null && page == 1) {
            queryListSuccess.value = mProductList
            return
        }
        compositeDisposable.add(repository.getProduct(categoryId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { if (page == 1) queryListProgress.value = true }
                .doFinally { if (page == 1) queryListProgress.value = false }
                .subscribe({
                    if (page == 1)
                        mProductList = ProductList(it.data ?: ArrayList(), false)
                    queryListSuccess.value = ProductList(it.data ?: ArrayList(), page != 1)
                },
                        { queryListError.value = it })
        )
    }
}