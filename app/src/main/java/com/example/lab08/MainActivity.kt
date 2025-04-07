package com.example.lab08

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var randomCharacterEditText: EditText
    private lateinit var broadcastReceiver: BroadcastReceiver
    private lateinit var serviceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        randomCharacterEditText = findViewById(R.id.editText_randomCharacter)
        broadcastReceiver = MyBroadcastReceiver()
        serviceIntent = Intent(applicationContext, RandomCharacterService::class.java)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.button_start -> {
                startService(serviceIntent)
            }
            R.id.button_stop -> {
                stopService(serviceIntent)
                randomCharacterEditText.setText("")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter().apply {
            addAction("my.custom.action.tag.lab6")
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            // Android 12 (API 31) or higher
            registerReceiver(broadcastReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            // Older Android versions
            registerReceiver(broadcastReceiver, intentFilter)
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }

    inner class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                val data = intent.getCharExtra("randomCharacter", '?')
                randomCharacterEditText.setText(data.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}