package com.example.atwatch

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Question : RealmObject(){
    @PrimaryKey
    var question_id: Long = 0
    var question: String = ""
    var level: String = ""
    var contest_id: Long = 0
}