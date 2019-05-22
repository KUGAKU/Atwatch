package com.example.atwatch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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


        val contest_id = intent.getLongExtra("contestId", 0)
        val question_id = intent.getLongExtra("questionId",0)
        val question = realm.where<Question>().equalTo("question_id",question_id).findFirst()
        val contest = realm.where<Contest>().equalTo("id",contest_id).findFirst()
        contestTitle.text = contest?.title
        textView2.text = question?.question //工夫の必要あり(全ての問題でAが表示されている// )



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
        }

        back_button.setOnClickListener { view ->
            val intent = Intent(this,Main2Activity::class.java)
            intent.putExtra("contest_id",contest?.id)
            startActivity(intent)
        }

        //cancel_button.setOnClickListener {
        //
        //}
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