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
        val current_time = intent?.getStringExtra("time")
        if (contestId != -1L) {//(-1Lではない＝デフォルト値でない＝同一コンテストの異なるレベルの問題の時に発火)
            val contest = realm.where<Contest>()
                .equalTo("id", contestId).findFirst()
            contest_title_input.setText(contest?.title)

            realm.executeTransaction{ db: Realm ->
                val contest = db.where<Contest>().equalTo("id",contestId).findFirst()
                if (contest?.progress?.isEmpty()!!){//もし合計時間が未登録なら＝A問題の解答終了時間を挿入してあげる処理.//正常動作確認済み
                    contest?.progress = current_time.toString()
                    progress_text.setText(current_time.toString())
                }
                else{ //もう既に合計時間が登録してあるなら＝既存の解答合計時間に今回の解答時間をプラスした値を挿入してあげる.
                    var sum_time: String? = contest?.progress
                    var current_time: String? = current_time

                    for (i:Int in 0..(sum_time?.length!!-1)){//文字列検査と型変換処理
                        if ( sum_time[i].equals(":")){
                            sum_time[i] = '0'
                        }
                    }
                    for (i:Int in 0..(current_time?.length!!-1)){//文字列検査と型変換処理
                        if ( current_time[i].equals(":")){
                            current_time[i] = '0'
                        }
                    }
                    var result: Int = sum_time.toInt() + current_time.toInt()
                    contest?.progress = result.toString()
                    progress_text.setText(result.toString())
                }

            }

            //progress_text.setText()
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

private operator fun String.set(i: Int, value: Char) {

}
