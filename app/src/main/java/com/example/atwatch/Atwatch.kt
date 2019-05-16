package com.example.atwatch

import android.app.Application
import io.realm.Realm

class Atwatch : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}