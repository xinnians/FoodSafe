package com.ufistudio.ianlin.foodsafe

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
        val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .debuggable(true)
                .build()
        Fabric.with(fabric)
    }
}