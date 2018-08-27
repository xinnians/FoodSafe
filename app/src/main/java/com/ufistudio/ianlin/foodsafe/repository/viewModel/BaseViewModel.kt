package com.ufistudio.ianlin.foodsafe.repository.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.ufistudio.ianlin.foodsafe.repository.Repository
import com.ufistudio.ianlin.foodsafe.repository.provider.preferences.SharedPreferencesProvider
import com.ufistudio.ianlin.foodsafe.repository.provider.resource.ResourceProvider
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(application: Application,
                             private val compositeDisposable: CompositeDisposable) : AndroidViewModel(application) {

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}