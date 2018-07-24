package com.ufistudio.ianlin.foodsafe.repository.provider.preferences

import android.content.SharedPreferences

interface AppSharedPreferences {
    fun sharedPreferences(): SharedPreferences
}