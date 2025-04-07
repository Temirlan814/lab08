package com.example.lab08

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat

class ForegroundActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foreground)

        findViewById<Button>(R.id.btn_start_music).setOnClickListener {
            ContextCompat.startForegroundService(
                this,
                Intent(this, MusicService::class.java)
            )
        }

        findViewById<Button>(R.id.btn_stop_music).setOnClickListener {
            stopService(Intent(this, MusicService::class.java))
        }

        findViewById<Button>(R.id.btn_next).setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
    }
}