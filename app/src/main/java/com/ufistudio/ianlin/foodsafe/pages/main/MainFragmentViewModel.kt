package com.ufistudio.ianlin.foodsafe.pages.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.ufistudio.ianlin.foodsafe.repository.Repository
import io.reactivex.disposables.CompositeDisposable

class MainFragmentViewModel(application: Application,
                            private val compositeDisposable: CompositeDisposable,
                            private val repository: Repository) :AndroidViewModel(application){

}