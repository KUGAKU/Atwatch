package com.example.atwatch

import android.app.Application
import android.view.View
import io.realm.Realm

class AtwatchApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}