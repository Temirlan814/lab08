package com.example.lab08

// MainActivity.kt
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class MainActivity : AppCompatActivity() {
    private lateinit var randomCharacterEditText: EditText
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.getIntExtra("digit", -1).takeIf { it != -1 }?.let { digit ->
                randomCharacterEditText.append(digit.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        randomCharacterEditText = findViewById(R.id.editText_randomCharacter)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.button_start -> startService(Intent(this, RandomCharacterService::class.java))
            R.id.button_end -> {
                stopService(Intent(this, RandomCharacterService::class.java))
                randomCharacterEditText.text.clear()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcastReceiver, IntentFilter("RANDOM_DIGIT_EVENT"))
    }

    override fun onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        super.onStop()
    }
}