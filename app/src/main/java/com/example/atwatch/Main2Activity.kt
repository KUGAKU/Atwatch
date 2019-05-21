package com.example.atwatch

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
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
        val intent =  Intent(this,Main3Activity::class.java)

        save_button.setOnClickListener { view: View ->
            realm.executeTransaction { db: Realm ->
                val maxId = db.where<Contest>().max("id")
                val nextId = (maxId?.toLong() ?: 0L) + 1
                val contest = db.createObject<Contest>(nextId)
                contest.title = contest_title_input.text.toString()
                val question_maxId = db.where<Question>().max("question_id")
                val question_nextId = (question_maxId?.toLong() ?:0L) + 1
                val question = db.createObject<Question>(question_nextId)
                question.question = task.getSelectedItem() as String;
                question.contest_id = nextId
                intent.putExtra("contestId", db.where<Contest>().max("id"))

            }
            Snackbar.make(view, "追加しました", Snackbar.LENGTH_SHORT)
                .setAction("戻る") {finish()}
                .setActionTextColor(Color.BLUE)
                .show()

            startActivity(intent)
        }

        //detail_button.setOnClickListener { view: View ->
        //    val intent =  Intent(this,Main3Activity::class.java)
        //    startActivity(intent)
        //}

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
