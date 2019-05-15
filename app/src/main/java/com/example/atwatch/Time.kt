package com.example.atwatch

import io.realm.annotations.PrimaryKey

open class Time {
    @PrimaryKey
    var time_id: Long = 0
    var start: Int = 0
    var end: Int = 0
    var progress: Int = 0
}