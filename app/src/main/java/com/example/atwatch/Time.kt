package com.example.atwatch

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Time : RealmObject() {
    @PrimaryKey
    var time_id: Long = 0
    var start: String = ""
    var end: String = ""
    var progress: String = ""
    var question_id: Long = 0
}
