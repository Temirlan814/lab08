package com.example.lab08

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import java.util.Random

class RandomCharacterService : Service() {
    private var isRandomGeneratorOn = false
    private val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()
    private val TAG = "RandomCharacterService"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(applicationContext, "Background Service Started", Toast.LENGTH_SHORT).show()
        Log.i(TAG, "Service started...")
        Log.i(TAG, "In OnStartCommand Thread ID is ${Thread.currentThread().id}")
        isRandomGeneratorOn = true

        Thread {
            startRandomGenerator()
        }.start()

        return START_STICKY
    }

    private fun startRandomGenerator() {
        while (isRandomGeneratorOn) {
            try {
                Thread.sleep(1000)
                if (isRandomGeneratorOn) {
                    val MIN = 0
                    val MAX = 25
                    val randomIdx = Random().nextInt(MAX) + MIN
                    val myRandomCharacter = alphabet[randomIdx]
                    Log.i(TAG, "Thread ID is ${Thread.currentThread().id}, Random Character is $myRandomCharacter")

                    val broadcastIntent = Intent()
                    broadcastIntent.action = "my.custom.action.tag.lab08"
                    broadcastIntent.putExtra("randomCharacter", myRandomCharacter)
                    sendBroadcast(broadcastIntent)
                }
            } catch (e: InterruptedException) {
                Log.i(TAG, "Thread Interrupted.")
            }
        }
    }

    private fun stopRandomGenerator() {
        isRandomGeneratorOn = false
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRandomGenerator()
        Toast.makeText(applicationContext, "Background Service Stopped", Toast.LENGTH_SHORT).show()
        Log.i(TAG, "Service Destroyed...")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}