package com.ufistudio.ianlin.foodsafe

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.ufistudio.ianlin.foodsafe.pages.main.information.InformationViewModel
import com.ufistudio.ianlin.foodsafe.pages.main.information.productList.ProductListViewModel
import com.ufistudio.ianlin.foodsafe.pages.main.information.search.SearchViewModel
import com.ufistudio.ianlin.foodsafe.pages.main.information.temporarily.TemporarilyViewModel
import com.ufistudio.ianlin.foodsafe.pages.main.news.NewsViewModel
import com.ufistudio.ianlin.foodsafe.pages.main.topics.TopicsViewModel
import com.ufistudio.ianlin.foodsafe.repository.Repository
import com.ufistudio.ianlin.foodsafe.repository.provider.preferences.SharedPreferencesProvider
import com.ufistudio.ianlin.foodsafe.repository.provider.resource.ResourceProvider
import io.reactivex.disposables.CompositeDisposable


class ViewModelFactory(private val application: Application,
                       private val repository: Repository,
                       private val preferences: SharedPreferencesProvider,
                       private val resource: ResourceProvider) : ViewModelProvider.NewInstanceFactory() {

    //ViewModel需要的model再打進去

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return with(modelClass) {
            when {
                isAssignableFrom(InformationViewModel::class.java) -> InformationViewModel(application, CompositeDisposable(), repository)
                isAssignableFrom(ProductListViewModel::class.java) -> ProductListViewModel(application, CompositeDisposable(), repository)
                isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel(application, CompositeDisposable(), repository)
                isAssignableFrom(NewsViewModel::class.java) -> NewsViewModel(application, CompositeDisposable(), repository)
                isAssignableFrom(TopicsViewModel::class.java) -> TopicsViewModel(application, CompositeDisposable(), repository)
                isAssignableFrom(TemporarilyViewModel::class.java) -> TemporarilyViewModel(application, CompositeDisposable(), repository)
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            } as T
        }
    }
}