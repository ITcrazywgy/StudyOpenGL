package com.study.studyopengl

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.study.studyopengl.widge.StateChangeListener
import kotlinx.android.synthetic.main.activity_pkm.*

class PkmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pkm)
        var frameRate = 25
        pkmView.setPath("assets/zip/cc.zip")
        pkmView.setStateChangeListener { _, currentState ->
            if (currentState == StateChangeListener.STOP) {
                frameRate += 5
                pkmView.setFrameRate(frameRate)
                pkmView.start()
            }
        }
        pkmView.setFrameRate(frameRate)
        pkmView.start()
    }
}
