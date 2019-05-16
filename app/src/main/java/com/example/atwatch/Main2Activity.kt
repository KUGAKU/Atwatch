package com.example.atwatch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        realm = Realm.getDefaultInstance()

        save_button.setOnClickListener { view: View ->
            realm.executeTransaction { db: Realm ->
                val maxId = db.where<Contest>().max("id")
                val nextId = (maxId?.toLong() ?: 0L) + 1
                val contest = db.createObject<Contest>(nextId)
                contest.title = contesttitle.text.toString()
            }
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
