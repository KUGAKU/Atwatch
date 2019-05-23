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


        val contestId = intent?.getLongExtra("contest_id", -1L)
        val all_time = intent?.getStringExtra("time")
        if (contestId != -1L) {//(-1Lではない＝デフォルト値でない＝同一コンテストの異なるレベルの問題の時に発火)
            val contest = realm.where<Contest>()
                .equalTo("id", contestId).findFirst()
            contest_title_input.setText(contest?.title)
            progress_text.setText(all_time.toString())
        }
        val intent = Intent(this, Main3Activity::class.java)
            save_button.setOnClickListener { view: View ->
                when (contestId) {
                    -1L -> { //新規登録

                        realm.executeTransaction { db: Realm ->
                            val maxId = db.where<Contest>().max("id")
                            val nextId = (maxId?.toLong() ?: 0L) + 1
                            val contest = db.createObject<Contest>(nextId)
                            contest.title = contest_title_input.text.toString()
                            val question_maxId = db.where<Question>().max("question_id")
                            val question_nextId = (question_maxId?.toLong() ?: 0L) + 1
                            val question = db.createObject<Question>(question_nextId)
                            question.question = task.getSelectedItem() as String;
                            question.contest_id = nextId
                            intent.putExtra("contestId", db.where<Contest>().max("id"))
                            intent.putExtra("questionId",db.where<Question>().max("question_id"))
                        }
                        Snackbar.make(view, "追加しました", Snackbar.LENGTH_SHORT)
                            .setAction("戻る") { finish() }
                            .setActionTextColor(Color.BLUE)
                            .show()
                        startActivity(intent)
                    }
                    else -> { //同一コンテストの異なるレベルの問題の時に発火
                        realm.executeTransaction{ db: Realm ->
                            val question_maxId = db.where<Question>().max("question_id")
                            val question_nextId = (question_maxId?.toLong() ?: 0L) + 1
                            val question = db.createObject<Question>(question_nextId)
                            question.question = task.getSelectedItem() as String;
                            question.contest_id = contestId!!
                            intent.putExtra("contestId", db.where<Contest>().max("id"))
                            intent.putExtra("questionId",db.where<Question>().max("question_id"))
                        }
                        Snackbar.make(view, "別のレベルの問題を選択！", Snackbar.LENGTH_SHORT)
                            .setAction("戻る") { finish() }
                            .setActionTextColor(Color.BLUE)
                            .show()
                        startActivity(intent)
                        }
                }
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
