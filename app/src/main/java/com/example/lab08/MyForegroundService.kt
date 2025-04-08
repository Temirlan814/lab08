package com.example.lab08

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat

class MyForegroundService : Service() {
    private lateinit var soundPlayer: MediaPlayer
    private val CHANNEL_ID = "music_channel_id"
    private val NOTIFICATION_ID = 101
    private val TAG = "MyForegroundService"

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, "Foreground Service Created", Toast.LENGTH_SHORT).show()
        try {
            soundPlayer = MediaPlayer.create(this, R.raw.song)
            soundPlayer.isLooping = false
            Log.d(TAG, "MediaPlayer created successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error creating MediaPlayer: ${e.message}")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand called")
        Toast.makeText(this, "Foreground Service Started", Toast.LENGTH_SHORT).show()

        createNotificationChannel()

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Music Player")
            .setContentText("Music is playing")
            .setSmallIcon(R.drawable.ic_music)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .build()

        startForeground(NOTIFICATION_ID, notification)

        try {
            if (!soundPlayer.isPlaying) {
                soundPlayer.start()
                Log.d(TAG, "MediaPlayer started")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error starting MediaPlayer: ${e.message}")
        }

        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Music Player Channel"
            val channelDescription = "Channel for Music Player Service"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, channelName, importance).apply {
                description = channelDescription
                setShowBadge(true)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            Log.d(TAG, "Notification channel created")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            if (soundPlayer.isPlaying) {
                soundPlayer.stop()
            }
            soundPlayer.release()
            Log.d(TAG, "MediaPlayer stopped and released")
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping MediaPlayer: ${e.message}")
        }
        Toast.makeText(this, "Foreground Service Stopped", Toast.LENGTH_SHORT).show()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}