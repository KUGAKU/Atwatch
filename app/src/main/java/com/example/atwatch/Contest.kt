package com.example.atwatch

import io.realm.RealmObject

open class Contest :RealmObject(){
    var id: Long = 0
    var title: String = ""
}