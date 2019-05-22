package com.example.atwatch

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Contest :RealmObject(){
    @PrimaryKey
    var id: Long = 0
    var title: String = ""
    var progress: String = ""
}
