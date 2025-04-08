package com.example.lab08

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var randomCharacterEditText: EditText
    private lateinit var broadcastReceiver: BroadcastReceiver
    private lateinit var backgroundServiceIntent: Intent
    private lateinit var foregroundServiceIntent: Intent

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startForegroundService()
        } else {
            Toast.makeText(
                this,
                "Notification permission denied. Cannot start foreground service properly.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        randomCharacterEditText = findViewById(R.id.editText_randomCharacter)

        broadcastReceiver = MyBroadcastReceiver()
        backgroundServiceIntent = Intent(applicationContext, RandomCharacterService::class.java)
        foregroundServiceIntent = Intent(this, MyForegroundService::class.java)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.button_start -> {
                startService(backgroundServiceIntent)
            }
            R.id.button_stop -> {
                stopService(backgroundServiceIntent)
                randomCharacterEditText.setText("")
            }
            R.id.button_start_foreground -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // Для Android 13+ проверяем разрешение на показ уведомлений
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        startForegroundService()
                    } else {
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                } else {
                    startForegroundService()
                }
            }
            R.id.button_stop_foreground -> {
                stopService(foregroundServiceIntent)
            }
            R.id.button_next_activity -> {
                startActivity(Intent(this, SecondActivity::class.java))
            }
        }
    }

    private fun startForegroundService() {
        ContextCompat.startForegroundService(this, foregroundServiceIntent)
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter()
        intentFilter.addAction("my.custom.action.tag.lab08")
        registerReceiver(broadcastReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)
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