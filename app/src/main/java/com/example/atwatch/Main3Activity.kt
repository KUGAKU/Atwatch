package com.example.atwatch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main3.*

class Main3Activity : AppCompatActivity() {
    private lateinit var realm: Realm
    val handler = Handler()
    var timeValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        realm = Realm.getDefaultInstance()

        stop_button.visibility = View.INVISIBLE
        val contest_id = intent.getLongExtra("contestId", 0)
        val question_id = intent.getLongExtra("questionId",0)
        val question = realm.where<Question>().equalTo("question_id",question_id).findFirst()
        val contest = realm.where<Contest>().equalTo("id",contest_id).findFirst()
        contestTitle.text = contest?.title
        textView2.text = question?.question



        val runnable = object : Runnable {
            override fun run() {
                timeValue++
                timeToText(timeValue)?.let { //timeToTextで表示データを作る
                    timeText.text = it //作った表示データに更新
                }
                handler.postDelayed(this, 1000) //1秒ごとにpost
            }
        }

        countUpStart_button.setOnClickListener {
            handler.post(runnable)
            //val nowid = intent.getLongExtra("contestId", 0)
            stop_button.visibility = View.VISIBLE
            countUpStart_button.visibility = View.INVISIBLE
            realm.executeTransaction{ db: Realm ->
                val time_maxId = db.where<Time>().max("time_id")
                val time_nextId = (time_maxId?.toLong() ?: 0L) + 1
                val time = db.createObject<Time>(time_nextId)
                time.question_id = question_id
            }

        }

        back_button.setOnClickListener { view ->
            val intent = Intent(this,Main2Activity::class.java)
            intent.putExtra("contest_id",contest?.id)
            realm.executeTransaction{ db: Realm ->
                intent.putExtra("time", db.where<Time>().equalTo("question_id",question_id).findFirst()?.time)
            }
            startActivity(intent)
        }

        stop_button.setOnClickListener {
            handler.removeCallbacks(runnable)
            countUpStart_button.visibility = View.VISIBLE
            stop_button.visibility = View.INVISIBLE

            realm.executeTransaction{ db: Realm ->
                val time = db.where<Time>().equalTo("question_id",question_id).findFirst()
                time?.time = timeText.text.toString()
            }
        }
    }


    private fun timeToText(time: Int = 0): String? {
        return if (time < 0) {
            null
        } else if (time == 0) {
            "00:00:00"
        } else {
            val h = time / 3600
            val m = time % 3600 / 60
            val s = time % 60
            "%1$02d:%2$02d:%3$02d".format(h, m, s) // 整形
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}