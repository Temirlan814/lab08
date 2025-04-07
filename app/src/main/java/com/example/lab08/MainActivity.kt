package com.example.lab08

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    private lateinit var randomCharacterEditText: EditText
    private lateinit var serviceIntent: Intent
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val data = it.getCharExtra("randomCharacter", '?')
                randomCharacterEditText.setText(data.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        randomCharacterEditText = findViewById(R.id.editText_randomCharacter)
        serviceIntent = Intent(applicationContext, RandomCharacterService::class.java)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.button_start -> startService(serviceIntent)
            R.id.button_end -> {
                stopService(serviceIntent)
                randomCharacterEditText.text.clear()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter("my.custom.action.tag.lab6"))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }
}