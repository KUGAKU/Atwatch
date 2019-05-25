package com.example.atwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main4.*

class Main4Activity : AppCompatActivity() {
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        realm = Realm.getDefaultInstance()
        list.layoutManager = LinearLayoutManager(this)
        val contests = realm.where<Contest>().findAll()
        val adapter = ContestAdapter(contests)
        list.adapter = adapter
    }


    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
