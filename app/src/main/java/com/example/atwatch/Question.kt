package com.example.atwatch

import io.realm.RealmObject

open class Question : RealmObject(){
    var question_id: Long = 0
    var question: String = ""
    var level: String = ""
}